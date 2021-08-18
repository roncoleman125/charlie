/*
 Copyright (c) 2014 Ron Coleman

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package charlie;

import charlie.actor.ClientAuthenticator;
import charlie.card.Hid;
import charlie.actor.Courier;
import charlie.actor.Arriver;
import charlie.audio.Effect;
import charlie.audio.SoundFactory;
import charlie.card.Card;
import charlie.card.Hand;
import charlie.dealer.Seat;
import charlie.util.Play;
import charlie.plugin.IAdvisor;
import charlie.server.Ticket;
import charlie.view.ATable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import charlie.util.Constant;
import java.io.File;
import java.lang.reflect.InvocationTargetException;


/**
 * This class implements the main game frame.
 * @author Ron Coleman
 */
public class GameFrame extends javax.swing.JFrame {
    static {
        System.getProperties().setProperty("LOGFILE","log-client.out");
    }
    protected final Logger LOG = Logger.getLogger(GameFrame.class);
    protected final Integer MY_PORT = 2345;
    protected Courier courier;
    protected ATable table;
    protected boolean connected = false;
    protected final String COURIER_ACTOR = "COURIER";
    protected final String SOUND_EFFECTS_PROPERTY = "charlie.sounds.enabled";
    protected final List<Hid> hids = new ArrayList<>();
    protected final HashMap<Hid,Hand> hands = new HashMap<>();
    protected int handIndex = 0;
    protected boolean trucking = true;
    protected boolean dubblable;
    protected IAdvisor advisor;
    protected Hand dealerHand;
//    private Properties props;
    protected boolean manuallyControlled = true;
    
    /**
     * Is a hand "split-able
     * @author Dan Blossom
     * @date 10/2017
     */
    protected boolean splittable = false;

    /**
     * Constructor
     */
    public GameFrame() {
        // Outputs CWD log4j uses where log4j.properties needs to be.
        // See http://logging.apache.org/log4j/1.2/faq.html#noconfig
//        System.err.println(Thread
//                .currentThread()
//                .getContextClassLoader()
//                .getResource("."));
        
        LOG.info("client started");
        
        initComponents();

        init();
        
        LOG.info("init done");
    }

    /**
     * Initializes the frame.
     */
    protected final void init() {
        // Establish the title
        this.setTitle("Charlie2");
        
        // Get the ATable on to the grame
        table = new ATable(this, this.surface);

        surface.add(table);

        // Position the frame
        this.setLocationRelativeTo(null);

        // Initially we can't deal or play
        enableDeal(false);

        enablePlay(false);
        
        // Initialize the sounds
        this.soundsCheckBox.setSelected(true);
        
        this.soundsCheckBox.addItemListener(new ItemListener() {    
             @Override
             public void itemStateChanged(ItemEvent e) {                 
                JCheckBox checkBox = (JCheckBox) e.getItem();
                if(checkBox.isSelected())
                    SoundFactory.enable(true);
                else
                    SoundFactory.enable(false);
             }    
          });    

        // Load the rest of the configuration
        loadConfig();
    }
    
    /**
     * Loads the configuration.
     */
    protected void loadConfig() {
        try {
            // Get the properties
            Properties props = System.getProperties();
            props.load(new FileInputStream("charlie.props"));  
            
            // Disable sounds, if configured as such
            String value = props.getProperty("charlie.sounds");
            if(value != null && value.equals("off")) {
                soundsCheckBox.setEnabled(false);
                soundsCheckBox.setSelected(false);
                SoundFactory.enable(false);
            }
            
            // Check for sound files folder
            File f = new File("audio");
            if(!f.exists() || !f.isDirectory()) {
                JOptionPane.showMessageDialog(this,
                        "Could not find audio files.",
                        "Status",
                        JOptionPane.ERROR_MESSAGE); 
                System.exit(1);
            
            }
            
            // Load the advisor
            loadAdvisor();
        } catch(IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Could not find or load charlie.props.",
                    "Status",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
    /**
     * Loads the advisor.
     */
    protected void loadAdvisor() {
        try {
            String className = System.getProperty(Constant.PLUGIN_ADVISOR);

            if (className == null)
                return;
             
            LOG.info("advisor plugin detected: "+className);
            Class<?> clazz = Class.forName(className);

            this.advisor = (IAdvisor) clazz.getDeclaredConstructor().newInstance();
            
            LOG.info("loaded advisor successfully");              
        } catch (ClassNotFoundException |
                InstantiationException |
                IllegalAccessException |
                NoSuchMethodException |
                SecurityException |
                IllegalArgumentException |
                InvocationTargetException ex) {
            LOG.error(ex.toString());
        }
    }
    
    /**
     * Confirm play with advisor.
     * @param hid Hand id for our hand
     * @param play Play we're making
     * @return True if advising, false otherwise
     */
    protected boolean confirmed(Hid hid,Play play) {
        if(!this.adviseCheckBox.isSelected() || advisor == null || dealerHand.size() < 2)
            return true;
        
        Hand myHand = hands.get(hid);

        Play advice = advisor.advise(myHand,dealerHand.getCard(1));

        if(advice == Play.NONE)
            return true;
        
        if (this.adviseCheckBox.isSelected() && advice != play) {
            SoundFactory.play(Effect.BAD_PLAY);
            
            Object[] options = {
                play,
                "Cancel"};
            String msg = "<html>I suggest...<font color=\"blue\" size=\"4\">" +
                    advice +
                    ".</font>";
            int n = JOptionPane.showOptionDialog(this,
                    msg,
                    "Advisor",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    options,
                    options[1]);

            if (n == 1) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * Connects to server
     * @param panel Panel courier perceives.
     * @return True if connected, false if connect attempt fails.
     */
    private boolean connect(ATable panel) {
        Ticket ticket = new ClientAuthenticator().send("abc", "def");
        
        if(ticket == null)
            return false;
        
        LOG.info("login successful");
        
        // Start courier to receive messages from dealer --
        // NOTE: we must start courier before sending arrival message otherwise
        // ready message will come before any actor can receive it.
        courier = new Courier(panel);
        courier.start();
        
        // Let house know we've arrived then wait for READY to begin playing
        new Arriver(ticket).send(); 

        synchronized (panel) {
            try {
                panel.wait(5000);

                Double bankroll = ticket.getBankroll();

                panel.setBankroll(bankroll);

                LOG.info("connected to courier with bankroll = " + bankroll);

            } catch (InterruptedException ex) {
                LOG.info("failed to connect to server: " + ex);

                failOver();

                return false;
            }
        }
        return true;
    }
    
    /**
     * Receives a card.
     * @param hid Hand id
     * @param card Card
     * @param handValues Hand values for the hand.
     */
    public void deal(Hid hid, Card card, int[] handValues) {      
        Hand hand = hands.get(hid);
        
        if(hand == null) {
            hand = new Hand(hid);
            
            hands.put(hid, hand);
            
            if(hid.getSeat() == Seat.DEALER)
                this.dealerHand = hand;
        }
            
        hand.hit(card);
        
        // For now, it will enable the split button
        // Only call if it's our hand
        // Do not like this here 
        if(hid.getSeat() == Seat.YOU){
            this.enableSplitButton(hid);
        }
    }

    /**
     * Enables dealing in which player can bet but not play (hit, stay, etc.).
     * @param state State
     */
    public void enableDeal(boolean state) {
        this.dealButton.setEnabled(state);

        this.table.enableBetting(state);

        this.hitButton.setEnabled(false);

        this.stayButton.setEnabled(false);

        this.splitButton.setEnabled(false);

        this.ddownButton.setEnabled(false);
    }

    /**
     * Enables play (hit, stay, etc.)
     * @param state State
     */
    public void enablePlay(boolean state) {
        this.hitButton.setEnabled(state && trucking && manuallyControlled);

        this.stayButton.setEnabled(state && trucking && manuallyControlled);

        this.ddownButton.setEnabled(state && dubblable && trucking && manuallyControlled);
        
        this.splitButton.setEnabled(state && splittable && trucking && manuallyControlled);
    }

//    /**
//     * Enables state, i.e., the player is able to make a play.
//     * @param state State
//     */
//    public void enableTrucking(boolean state) {
//        this.trucking = state;
//    }

    /**
     * Recovers in event we fail catastrophically.
     */
    protected void failOver() {

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        surface = new javax.swing.JPanel();
        accessButton = new javax.swing.JButton();
        dealButton = new javax.swing.JButton();
        hitButton = new javax.swing.JButton();
        stayButton = new javax.swing.JButton();
        ddownButton = new javax.swing.JButton();
        splitButton = new javax.swing.JButton();
        adviseCheckBox = new javax.swing.JCheckBox();
        soundsCheckBox = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        org.jdesktop.layout.GroupLayout surfaceLayout = new org.jdesktop.layout.GroupLayout(surface);
        surface.setLayout(surfaceLayout);
        surfaceLayout.setHorizontalGroup(
            surfaceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );
        surfaceLayout.setVerticalGroup(
            surfaceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 401, Short.MAX_VALUE)
        );

        accessButton.setText("Login");
        accessButton.setToolTipText("");
        accessButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accessButtonActionPerformed(evt);
            }
        });

        dealButton.setText("Deal");
        dealButton.setActionCommand(" Bet ");
        dealButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dealButtonActionPerformed(evt);
            }
        });

        hitButton.setText(" Hit ");
        hitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hitButtonActionPerformed(evt);
            }
        });

        stayButton.setText(" Stay");
        stayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stayButtonActionPerformed(evt);
            }
        });

        ddownButton.setText("DDown");
        ddownButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ddownButtonActionPerformed(evt);
            }
        });

        splitButton.setText("Split");
        splitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                splitButtonActionPerformed(evt);
            }
        });

        adviseCheckBox.setText("Advise");
        adviseCheckBox.setEnabled(false);
        adviseCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adviseCheckBoxActionPerformed(evt);
            }
        });

        soundsCheckBox.setText("Sounds");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(surface, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(soundsCheckBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 67, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(adviseCheckBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 168, Short.MAX_VALUE)
                        .add(splitButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(ddownButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(hitButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(stayButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(dealButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(accessButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(surface, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(accessButton)
                    .add(dealButton)
                    .add(hitButton)
                    .add(stayButton)
                    .add(ddownButton)
                    .add(splitButton)
                    .add(adviseCheckBox)
                    .add(soundsCheckBox))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    /**
     * Handles case were the player presses the "access" button.
     * @param evt Button press event.
     */
    private void accessButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accessButtonActionPerformed
        final GameFrame frame = this;
        
        // If we're not connected to server, try to connect.
        if (!connected) {
            frame.accessButton.setEnabled(false);
            
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    connected = frame.connect(table);
                    
                    if (connected) {
                        // Prime the audio player
                        SoundFactory.prime();

                        JOptionPane.showMessageDialog(frame,
                                "Successfully connected to server.",
                                "Status",
                                JOptionPane.INFORMATION_MESSAGE);

                        frame.accessButton.setText("Logout");
                        
                        if (table.autopilotEngaged()) {
                            table.startAutopilot();

                            frame.manuallyControlled = false;
                        }
                        
                        frame.enableDeal(manuallyControlled);
                        
                        if(advisor != null)
                            frame.adviseCheckBox.setEnabled(manuallyControlled);
                        
                    } else {
                        JOptionPane.showMessageDialog(frame,
                                "Failed to connect to server.",
                                "Status",
                                JOptionPane.ERROR_MESSAGE);
                    }

                    frame.accessButton.setEnabled(true);
                }
            });
        } else {
            // If we're connected, check before quitting in case this is an accident.
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    SoundFactory.play(Effect.ADIOS);
                    
                    Object[] options = { "YES", "Cancel" };
                    int n = JOptionPane.showOptionDialog(frame,
                            "Sure you want to quit game?",
                            "Confirm",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.WARNING_MESSAGE,
                            null,
                            options,
                            options[1]);
                    
                    if(n == 0)
                        System.exit(0);
                }
            });
        }

    }//GEN-LAST:event_accessButtonActionPerformed

    /**
     * Player presses deal button.
     * @param evt Button press event.
     */
    private void dealButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dealButtonActionPerformed
        // Clear hands
        hids.clear();
        
        hands.clear();

        this.handIndex = 0;

        //this.dubblable = true;
        this.setdubblable(true);
        
        
        final GameFrame frame = this;
//        SwingUtilities.invokeLater(new Runnable() {
        new Thread(new Runnable() {
            @Override
            public void run() {        
                // Clear the table and shuffle the cards.
                frame.table.clear();
                
                // Get new bet
                Integer amt = table.getBetAmt();

                if (amt <= 0) {
                    SoundFactory.play(Effect.NO_BET);
                    JOptionPane.showMessageDialog(frame,
                            "Minimum bet is $5.",
                            "Status",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Disable starting new game
                enableDeal(false);
                
                // Shuffle the cards
                frame.table.shuffle();

                // Get player side wager on table
                Integer sideAmt = table.getSideAmt();

                // Send bets to dealer which starts the game.
                Hid hid = courier.bet(amt, sideAmt);

                hids.add(hid);
                
                hands.put(hid, new Hand(hid));
            }
        }).start();

    }//GEN-LAST:event_dealButtonActionPerformed
    
    /**
     * Handles case where player presses stay button.
     * @param evt Button press event.
     */
    private void stayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stayButtonActionPerformed
        final GameFrame frame = this;
                
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Hid hid = hids.get(frame.handIndex);

                if (!confirmed(hid, Play.STAY))
                    return;
                
                // Disable further play since this is a STAY
                enablePlay(false);
                
                courier.stay(hids.get(frame.handIndex));
            }
        });        
    }//GEN-LAST:event_stayButtonActionPerformed

    /**
     * Handles case where player presses hit button.
     * @param evt Button press event.
     */    
    private void hitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hitButtonActionPerformed
        final GameFrame frame = this;
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Hid hid = hids.get(frame.handIndex);
                
                if(!confirmed(hid,Play.HIT))
                    return;
                
                // NOTE: this isables double down on all hids and will have to be
                // fixed when splitting hids
                //frame.dubblable = false;
                frame.setdubblable(false);

                // Disable play until the card arrives
                enablePlay(false);

                courier.hit(hid);
            }
        });

    }//GEN-LAST:event_hitButtonActionPerformed

    /**
     * Handles case where player presses double-down button.
     * @param evt Button press event.
     */    
    private void ddownButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ddownButtonActionPerformed
       final GameFrame frame = this;

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Hid hid = hids.get(frame.handIndex);

                if (!confirmed(hid, Play.DOUBLE_DOWN))
                    return;

                SoundFactory.play(Effect.DOUBLE_DOWN);
                
                // Disable further playing since this is double-down
                enablePlay(false);

                // No further dubbling until the next bet made
                //dubblable = false;
                frame.setdubblable(false);
                
                // Double the bet in the myHand using a copy since this
                // is a transient bet.
                hid.dubble();

                // Send this off to the dealer
                courier.dubble(hid);

                // Double the bet on the table
                table.dubble(hid);
            }
        });
    }//GEN-LAST:event_ddownButtonActionPerformed

    /**
     * Handles case where player changes the advise state.
     * @param evt Button press event.
     */    
    private void adviseCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adviseCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_adviseCheckBoxActionPerformed
    /**
     * Handles case where player presses split button
     * added 10/20/2017
     * @author Dan Blossom
     * @param evt split button press event
     */
    private void splitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_splitButtonActionPerformed

        final GameFrame frame = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // get active hand
                Hid hid = hids.get(frame.handIndex);
                
                if (!confirmed(hid, Play.SPLIT))
                    return;
                
                SoundFactory.play(Effect.SPLIT);
                
                // no more splits this go.
                splitButton.setEnabled(false);

                // tell the dealer we requested a split and provide an HID
                courier.split(hid);
            }
        });
    }//GEN-LAST:event_splitButtonActionPerformed

    /**
     * A helper function to determine if a hand can be split
     * @param hid
     */
    public void enableSplitButton(Hid hid){
        
        if(hid.getSeat() != Seat.YOU){
            this.splittable = false;
            return;
        }
        
        Hand hand = hands.get(hid);
        
        // If the hand is a pair && it hasn't been part of a split
        this.splittable = hand.isPair() && !hid.getSplit();
    }
    
    /**
     * Once ATable receives successful split from dealer
     * This method is called to update GameFrame hand/hids
     * @param newHid the new hid to be added
     * @param origHid the original hid to be edited
     */
    public void split(Hid newHid, Hid origHid){
        
        this.hids.add(newHid);
        
        // Create two hands from cards.
        Hand newHandLeft = new Hand(origHid);
        Hand newHandRight = new Hand(newHid);
        
        // Hit each and with one of the split cards
        Card leftCard = hands.get(origHid).getCard(0);
        Card rightCard = hands.get(origHid).getCard(1);
        
        newHandLeft.hit(leftCard);
        newHandRight.hit(rightCard);
        
        // Replace the original hand with the left
        hands.remove(origHid);
        hands.put(origHid, newHandLeft);
        
        // Add the new hand.
        hands.put(newHid, newHandRight);
    }
    
    /**
     * Increment the hand index.
     * 
     * I think a "set" and HID is a better solution than ArrayList
     */
    public void updateHandIndex(){
        if(handIndex < hids.size()){
            handIndex++;
        }         
    }
    
    /**
     * Sets the double variable true/false
     * @param state - the state to make 'dubblable'
     */
    public void setdubblable(boolean state){
       this.dubblable = state; 
    }
    
    /**
     * Main starting point of app.
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GameFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton accessButton;
    private javax.swing.JCheckBox adviseCheckBox;
    private javax.swing.JButton ddownButton;
    private javax.swing.JButton dealButton;
    private javax.swing.JButton hitButton;
    private javax.swing.JCheckBox soundsCheckBox;
    private javax.swing.JButton splitButton;
    private javax.swing.JButton stayButton;
    private javax.swing.JPanel surface;
    // End of variables declaration//GEN-END:variables
}
