/*
 Copyright (c) Ron Coleman

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

import charlie.audio.Effect;
import charlie.audio.SoundFactory;
import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.util.Play;
import charlie.view.ATable;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * This class implements the game client. It replaces the one developed from the outset
 * using the NB gui builder with one built by the IntelliJ gui builder.
 * @author Ron.Coleman
 */
public class GameClient extends AbstractGameFrame {
//    protected static Logger LOG = null;
    public final static String TITLE = "Charlie 3.1";

    //
    // These are from the gui builder.
    //
    private JPanel topmostPanel;
    private JCheckBox soundsCheckBox;
    private JCheckBox adviseCheckBox;
    private JButton stayButton;
    private JButton dealButton;
    private JButton loginButton;
    private JButton hitButton;
    private JButton splitButton;
    private JPanel surface;
    private JButton ddownButton;

    final AbstractGameFrame frame = this;

    /**
     * Constructor
     */
    public GameClient() {
        // Override default log file name in log4j.properties with the log file for this program.
        System.getProperties().setProperty("LOGFILE","log-client.out");

        // Launch the logger which causes log4j.properties to be read.
        LOG = Logger.getLogger(GameClient.class);

        // Topmost is where the GUI builder begins...but loading of the form is unclear.
        setContentPane(topmostPanel);

        // Add the ATable as the main panel -- ATable does pretty much the rest at runtime
        table = new ATable(this, topmostPanel);
        topmostPanel.add(table);

        setTitle(TITLE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // These values just...work based on the NB gui builder
        setSize(695,500);

        // Makes the frame appear (typically) in the middle of the desktop
        setLocationRelativeTo(null);
        setVisible(true);

        // Logging can now start in earnest
        LOG.info("client started");

        initComponents();

        init();
    }

    /**
     * Initializes the gui state.
     */
    private void init() {
        // Makes the icon on the title bar and the program tray.
        try {
            //setIconImage(ImageIO.read(new File("images/ace-card-png-clipart-2772840239.png")));
            setIconImage(ImageIO.read(new File("images/myace-6.png")));
        }
        catch(Exception e) {
            System.out.println(e);
        }

        // Initially we can't deal or play because the server might not be running.
        enableDeal(false);

        enablePlay(false);

        // Initializes the sounds as ready to play.
        this.soundsCheckBox.setSelected(true);
    }


    /**
     * Initializes the different gui components.
     */
    private void initComponents() {
        // We don't necessarily care for this style of adding action listeners but
        // it's what IntelliJ gave us...
        loginButton.addActionListener(new ActionListener() {
            /**
             * Logs in or logs out, depending on the state.
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                // If we're not connected to server, try to connect.
                if (!connected) {
                    loginButton.setEnabled(false);

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

                                loginButton.setText("Logout");

                                if (table.autopilotEngaged()) {
                                    table.startAutopilot();

                                    frame.manuallyControlled = false;
                                }

                                frame.enableDeal(manuallyControlled);

                                if(advisor != null)
                                    adviseCheckBox.setEnabled(manuallyControlled);

                            } else {
                                JOptionPane.showMessageDialog(frame,
                                        "Failed to connect to server.",
                                        "Status",
                                        JOptionPane.ERROR_MESSAGE);
                            }

                            loginButton.setEnabled(true);
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
            }
        });

        dealButton.addActionListener(new ActionListener() {
            /**
             * Causes a deal from the shoe.
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear hands
                hids.clear();

                hands.clear();

                handIndex = 0;

                setDubblable(true);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Clear the table and shuffle the cards.
                        table.clear();

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
                        table.shuffle();

                        // Get player side wager on table
                        Integer sideAmt = table.getSideAmt();

                        // Send bets to dealer which starts the game.
                        Hid hid = courier.bet(amt, sideAmt);

                        hids.add(hid);

                        hands.put(hid, new Hand(hid));
                    }
                }).start();
            }
        });

        stayButton.addActionListener(new ActionListener() {
            /**
             * Requests a stay from the dealer.
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Hid hid = hids.get(frame.handIndex);

                        if (!isAdvisingConfirmed(hid, Play.STAY))
                            return;

                        // Disable further play since this is a STAY
                        enablePlay(false);

                        courier.stay(hids.get(frame.handIndex));
                    }
                });
            }
        });

        hitButton.addActionListener(new ActionListener() {
            /**
             * Requests a hit from the dealer.
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Hid hid = hids.get(frame.handIndex);

                        if(!isAdvisingConfirmed(hid,Play.HIT))
                            return;

                        // NOTE: this enables double down on all hids and will have to be
                        // fixed when splitting hids
                        //frame.dubblable = false;
                        setDubblable(false);

                        // Disable play until the card arrives
                        enablePlay(false);

                        courier.hit(hid);
                    }
                });
            }
        });

        splitButton.addActionListener(new ActionListener() {
            /**
             * Requests a split from the dealer.
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        // get active hand
                        Hid hid = hids.get(frame.handIndex);

                        if (!isAdvisingConfirmed(hid, Play.SPLIT))
                            return;

                        SoundFactory.play(Effect.SPLIT);

                        // no more splits this go.
                        splitButton.setEnabled(false);

                        // tell the dealer we requested a split and provide an HID
                        courier.split(hid);
                    }
                });
            }
        });

        ddownButton.addActionListener(new ActionListener() {
            /**
             * Requests a double-down from the dealer.
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Hid hid = hids.get(frame.handIndex);

                        if (!isAdvisingConfirmed(hid, Play.DOUBLE_DOWN))
                            return;

                        SoundFactory.play(Effect.DOUBLE_DOWN);

                        // Disable further playing since this is double-down
                        enablePlay(false);

                        // No further dubbling until the next bet made
                        //dubblable = false;
                        frame.setDubblable(false);

                        // Double the bet in the myHand using a copy since this
                        // is a transient bet.
                        hid.dubble();

                        // Send this off to the dealer
                        courier.dubble(hid);

                        // Double the bet on the table
                        table.dubble(hid);
                    }
                });
            }
        });

        // Loads the plugins
        loadConfig();
    }
    public static void main(String[] main) {
        // Set the L&F which must be done before instantiating any components.
        try {
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Launches the GUI but...I don't understand how it finds the form since it's not referenced
        // in the code anywhere.
        new GameClient();
    }

    /**
     * Enables the deal and other buttons.
     * @param state If true, the appropriate buttons will be enabled.
     */
    @Override
    public void enableDeal(boolean state) {
        this.dealButton.setEnabled(state);

        this.table.enableBetting(state);

        this.hitButton.setEnabled(false);

        this.stayButton.setEnabled(false);

        this.splitButton.setEnabled(false);

        this.ddownButton.setEnabled(false);
    }

    /**
     * Enables play.
     * @param state If true, enables buttons appropriately.
     */
    @Override
    public void enablePlay(boolean state) {
        this.hitButton.setEnabled(state && trucking && manuallyControlled);

        this.stayButton.setEnabled(state && trucking && manuallyControlled);

        this.ddownButton.setEnabled(state && dubblable && trucking && manuallyControlled);

        this.splitButton.setEnabled(state && splittable && trucking && manuallyControlled);
    }

    /**
     * Splits an old hand into a new one.
     * @param newHid Target hand
     * @param origHid Source hand
     */
    @Override
    public void split(Hid newHid, Hid origHid) {

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
     * Updates the hand index.
     */
    @Override
    public void updateHandIndex() {
        if(handIndex < hids.size()){
            handIndex++;
        }
    }

    /**
     * Makes double-able possible.
     * @param state If true, does so
     */
    @Override
    public void setDubblable(boolean state) {
        this.dubblable = state;
    }

    /**
     * Deals a card to a hand.
     * @param hid Hand id
     * @param card Card going to the hand
     * @param handValues Hard and soft hand values
     */
    @Override
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
     * Enables a split for a hand.
     * @param hid Hand id of the hand.
     */
    public void enableSplitButton(Hid hid){

        if(hid.getSeat() != Seat.YOU){
            this.splittable = false;
            return;
        }

        Hand hand = hands.get(hid);

        // If the hand is a pair && it hasn't been part of a split
        this.splittable = hand.isPair() && !hid.isSplit();
    }

    /**
     * Tests if the hand is confirmed for advising.
     * @param hid Hand id
     * @param play Play to confirm
     * @return True if the advice is confirmed -- this causes
     */
    @Override
    protected boolean isAdvisingConfirmed(Hid hid, Play play) {
//        return false;
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
     * Loads the configuration, including the plugins.
     */
    @Override
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

}

