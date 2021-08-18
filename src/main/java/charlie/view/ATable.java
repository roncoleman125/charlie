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
package charlie.view;

import charlie.plugin.IUi;
import charlie.view.sprite.TurnIndicator;
import charlie.GameFrame;
import charlie.actor.Courier;
import charlie.audio.Effect;
import charlie.audio.SoundFactory;
import charlie.card.Hid;
import charlie.card.Card;
import charlie.card.HoleCard;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import javax.swing.JPanel;
import charlie.util.Point;
import charlie.dealer.Seat;
import charlie.plugin.ISideBetView;
import charlie.util.Constant;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import charlie.plugin.ILogan;

/**
 * This class is the main table panel.
 *
 * @author Ron Coleman
 */
public final class ATable extends JPanel implements Runnable, IUi, MouseListener {
    private final Logger LOG = Logger.getLogger(ATable.class);
    protected Random ran = new Random();
    protected String[] huey = {"Huey"};
    protected String[] dewey = {"Dewey"};
    protected AHandsManager you = new AHandsManager("You", new Point(250, 225));
    protected AHandsManager dealer = new AHandsManager("Dealer", new Point(225, 0));
    protected AHandsManager right = new AHandsManager(huey[ran.nextInt(huey.length)], new Point(465, 150));
    protected AHandsManager left = new AHandsManager(dewey[ran.nextInt(dewey.length)], new Point(25, 150));
    protected AHandsManager[] handsManager = {you, dealer, right, left};
    protected TurnIndicator turnSprite = new TurnIndicator();
    protected AHand turn = null;
    protected HashMap<Seat, AHandsManager> seats = new HashMap<Seat, AHandsManager>() {
        {
            put(Seat.YOU, you);
            put(Seat.RIGHT, right);
            put(Seat.LEFT, left);
            put(Seat.DEALER, dealer);
        }
    };
    private final HashMap<Seat, AMoneyManager> monies = new HashMap<Seat, AMoneyManager>() {
        {
            put(Seat.YOU, new AMoneyManager());
            put(Seat.RIGHT, new ABotMoneyManager());
            put(Seat.LEFT, new ABotMoneyManager());
        }
    };
    
    protected HashMap<Hid, AHand> manos = new HashMap<>();
    protected Thread gameLoop;
    protected static Color COLOR_FELT = new Color(0, 153, 100);
    protected final int DELAY = 50;
    protected final GameFrame frame;
    protected boolean bettable = false;
    protected boolean gameOver = true;
    protected boolean shufflePending = false;
    protected boolean trucking = true;
    protected int shoeSize;
    protected Image instrImg;
    protected Image shoeImg;
    protected Image trayImg;
    protected ABurnCard burnCard = new ABurnCard();
    // These keep track of state for playing sounds
    protected int numHands;
    protected int loses;
    protected int pushes;
    protected int wins;
    protected int blackjacks;
    protected int charlies;
    protected int busts;
    protected ISideBetView sideBetView;
    protected Properties props; 
    protected ILogan logan;
    private Card holeCard;
    private int[] holeValues;
    protected Courier courier;

    /**
     * Constructor
     *
     * @param frame Main game frame
     * @param parent Parent panel containing this one.
     */
    public ATable(GameFrame frame, JPanel parent) {
        this.frame = frame;

        setSize(parent.getWidth(), parent.getHeight());

        init();
    }

    /**
     * Initializes custom table components.
     */
    public void init() {
        setBackground(COLOR_FELT);

        setDoubleBuffered(true);

        this.addMouseListener(this);

        this.addNotify();

        this.instrImg = new ImageIcon(Constant.DIR_IMGS + "dealer-stands-0.png").getImage();
        this.shoeImg = new ImageIcon(Constant.DIR_IMGS + "shoe-0.png").getImage();
        this.trayImg = new ImageIcon(Constant.DIR_IMGS + "tray-0.png").getImage();
        
        this.loadConfig();
    }
    
    /**
     * Clears table of old bets, etc.
     */
    public void clear() {
        wins = loses = pushes = blackjacks = charlies = busts = 0;
        
        for (AHandsManager animator : seats.values()) {
            animator.clear();
        }
        
        for (Hid hid : manos.keySet()) {
            AMoneyManager money = monies.get(hid.getSeat());

            // Skip dealer since it doesn't have a money manager
            if (money == null)
                continue;
            
            // MUST BE IN THIS ORDER.... 
            // Otherwise, 'unsplit' might cause table amount to report wrong
            // TODO: Fix unsplit to be a bit more robust
            money.unsplit();
            money.undubble();
        }
        
        if(sideBetView != null)
            sideBetView.starting();     
        
        holeCard = null;
        
        holeValues = null;
    }

    /**
     * Gets the main upBet amount on the table.<br>
     * This should only be requested when making a upBet but before the table
     * has been clearBeted
     *
     * @return Bet amount
     */
    public Integer getBetAmt() {
        AMoneyManager money = this.monies.get(Seat.YOU);
        
        Integer amt = money.getWager();
        
        return amt;
    }
    
    /**
     * Gets the side upBet amount on the table.
     * @return Side upBet amount
     */
    public Integer getSideAmt() {
        int amt = 0;
        
        if(this.sideBetView != null)
            amt = this.sideBetView.getAmt();
        
        LOG.info("side bet = "+amt);
        
        return amt;
    }

    /**
     * Makes the paint method get invoked.
     */
    @Override
    public void addNotify() {
        super.addNotify();

        gameLoop = new Thread(this);

        gameLoop.start();
    }

    /**
     * Paints the display some time after repainted invoked.
     *
     * @param g Graphics context
     */
    @Override
    public synchronized void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;

        // Render the paraphenelia
        g2d.drawImage(this.instrImg, 140, 208, this);
        g2d.drawImage(this.shoeImg, 540, 5, this);
        g2d.drawImage(this.trayImg, 430, 5, this);

        // Render the upBet on the table
        this.monies.get(Seat.YOU).render(g2d);

        // Render the hands
        for (int i = 0; i < handsManager.length; i++) {
            handsManager[i].render(g2d);
        }
        
        // Render the side upBet
        if(sideBetView != null)
            sideBetView.render(g2d);

        // Render the burn card
        if(burnCard.isVisible())
            burnCard.render(g2d);
        
        if(logan != null)
            logan.render(g2d);
        
        // Java tool related stuff
        Toolkit.getDefaultToolkit().sync();

        g.dispose();
    }

    /**
     * Updates the table.
     */
    public synchronized void update() {
        // Update every hand at the table
        for (int i = 0; i < handsManager.length; i++) {
            handsManager[i].update();
        }
        
        // Update the side upBet
        if(sideBetView != null)
            sideBetView.update(); 
        
        // If it's my turn, I didn't break, and my cards have landed,
        // then enable to play
        if (turn != null
                && turn.hid.getSeat() == Seat.YOU
                && !turn.isBroke()
                && you.isReady()
                && dealer.isReady()
                && !gameOver
                && trucking) {
            // "trucking" => I'm running
            trucking = false;

            // Enable play buttons
            frame.enablePlay(true);
        }
        
        burnCard.update();
        
        if(logan != null)
            logan.update();
    }

    /**
     * Repaints the display
     */
    public void render() {
        repaint();
    }

    /**
     * Runs the game loop
     */
    @Override
    public void run() {
        long beforeTime = System.currentTimeMillis();

        while (true) {
            update();

            render();

            long timeDiff = System.currentTimeMillis() - beforeTime;
            
            double sleep = (DELAY - timeDiff)/1000.0;

            if (sleep < 0) {
                sleep = 2;
            }
            
            timeout(sleep);

            beforeTime = System.currentTimeMillis();
        }
    }

    /**
     * Sets the amount.
     * @param amt Bankroll
     */
    public void setBankroll(Double amt) {
        this.monies.get(Seat.YOU).setBankroll(amt);
    }

    /**
     * Enables upBetting (i.e., the keys work)
     *
     * @param betting True or false
     */
    public void enableBetting(boolean betting) {
        bettable = betting;
    }

    /**
     * Double upBet on the table.
     * @param hid Hand id
     */
    public void dubble(Hid hid) {
        AMoneyManager money = this.monies.get(hid.getSeat());

        money.dubble(hid);
    }

    /**
     * Sets the turn for a hand.
     * @param hid Hand id
     */
    @Override
    public void turn(final Hid hid) {
        AHand hand = manos.get(hid);

        if (hid.getSeat() == Seat.DEALER) {
            // Reveal dealer's hole card
            hand.get(0).flip();
            
            // Inform gety since we bypassed sending this to logan during the deal
            // This is really only important at this stage for counting cards.
            if(logan != null)
                logan.deal(hid, holeCard, holeValues);
            
            // Disable the "turn" signal
            // Note: "turn" will be null on dealer blackjack in which case
            // nobody has played.
            if (turn != null)
                turn.enablePlaying(false);
            
            // Disable player input
            this.frame.enablePlay(false);
        } else {           
            // Disable old hand
            if (turn != null)
                turn.enablePlaying(false);

            // Enable new hand
            turn = hand;
            
            turn.enablePlaying(true);

            // If turn is NOT my hand, disable my hand
            boolean enable = true;
            
            // ONLY update player hand
            if(hid.getSplit()){
                // update hand index
                this.frame.updateHandIndex();
                this.frame.setdubblable(true);
                this.frame.enablePlay(true); // this needed? 
            }

            if (hid.getSeat() != Seat.YOU) {
                enable = false;
            }

            if (logan == null) {
                this.frame.enablePlay(enable);
            }
            else {
                new Thread(new Runnable() { 
                    @Override
                    public void run() {
                        logan.play(hid);
                    }
                }).start();

            }
            
            SoundFactory.play(Effect.TURN);
        }
    }

    /**
     * Receives a hit for a hand.
     * @param hid Hand id
     * @param card Card hitting the hand
     * @param handValues Hand values
     */
    @Override
    public synchronized void deal(final Hid hid, final Card card, final int[] handValues) {
        if(hid.getSeat() == Seat.YOU)
            trucking = true;
        
        SoundFactory.play(Effect.DEAL);

        AHand hand = manos.get(hid);
 
        hand.setValues(handValues);

        // If card is null, this is not a "real" hit but only
        // updating the respective hand value.
        if (card == null)
            return;

        // Convert card to an animated card and hit the hand
        ACard acard = ACard.animate(card);

        hand.hit(acard);
        
        // Let the advisor, if it exists, know what's going on
        frame.deal(hid, card, handValues);
        
        // Let Logan, if it exists, know what's going on except for the hole card
        // which we'll send to Logan when it's the dealer's turn.
        if(card instanceof HoleCard) {
            this.holeValues = handValues;
            this.holeCard = card;
        }
        
        if (logan != null && !(card instanceof HoleCard)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    logan.deal(hid, card, handValues);
                }
            }).start();
        }
    }

    /**
     * Updates a hand with a break outcome.
     * @param hid Hand id
     */
    @Override
    public void bust(Hid hid) {
        LOG.info("BUST for hid = "+hid+" amt = "+hid.getAmt());
        
        AHand hand = manos.get(hid);

        hand.setOutcome(AHand.Outcome.Bust);

        AMoneyManager money = this.monies.get(hid.getSeat());

        money.decrease(hid.getAmt());

        if (hid.getSeat() != Seat.DEALER) {
            loses++;
            busts++;
            SoundFactory.play(Effect.TOUGH);
        }
        
        if(sideBetView != null)
            sideBetView.ending(hid);
        
        if(logan != null)
            logan.bust(hid);
    }

    /**
     * Updates hand with winning outcome.
     * @param hid Hand id
     */
    @Override
    public void win(Hid hid) {        
        LOG.info("WIN for hid = "+hid+" amt = "+hid.getAmt());
        
        AHand hand = manos.get(hid);

        hand.setOutcome(AHand.Outcome.Win);

        AMoneyManager money = this.monies.get(hid.getSeat());

        money.increase(hid.getAmt());

        if(hid.getSeat() != Seat.DEALER)
            wins++;

        if(sideBetView != null)
            sideBetView.ending(hid);
        
        if(logan != null)
            logan.win(hid);
    }

    /**
     * Updates hand with loosing outcome.
     * @param hid Hand id
     */
    @Override
    public void lose(Hid hid) {
        LOG.info("LOSE for hid = "+hid+" amt = "+hid.getAmt());
        
        AHand hand = manos.get(hid);

        hand.setOutcome(AHand.Outcome.Lose);

        AMoneyManager money = this.monies.get(hid.getSeat());

        money.decrease(hid.getAmt());
        
        if(hid.getSeat() != Seat.DEALER)
            ++loses;
        
        if(sideBetView != null)
            sideBetView.ending(hid);
        
        if(logan != null)
            logan.lose(hid);
    }

    /**
     * Updates hand with push outcome.
     * @param hid Hand id
     */
    @Override
    public void push(Hid hid) {
        LOG.info("PUSH for hid = "+hid+" amt = "+hid.getAmt());
        
        AHand hand = manos.get(hid);

        hand.setOutcome(AHand.Outcome.Push);
        
        // Play puch sound only once
        if(hid.getSeat() != Seat.DEALER && pushes == 0) {
            ++pushes;
            SoundFactory.play(Effect.PUSH);
        }

        if(sideBetView != null)
            sideBetView.ending(hid);
        
        if(logan != null)
            logan.push(hid);
    }

    /**
     * Updates hand with Blackjack outcome.
     * @param hid Hand id
     */
    @Override
    public void blackjack(Hid hid) {
        LOG.info("BJ for hid = "+hid+" amt = "+hid.getAmt());
        
        AHand hand = manos.get(hid);

        hand.setOutcome(AHand.Outcome.Blackjack);

        AMoneyManager money = this.monies.get(hid.getSeat());

        money.increase(hid.getAmt());

        if (hid.getSeat() != Seat.DEALER) {
            SoundFactory.play(Effect.BJ);
            wins++;
            blackjacks++;
        }
        
        if(sideBetView != null)
            sideBetView.ending(hid);
        
        if(logan != null)
            logan.blackjack(hid);
    }

    /**
     * Updates hand with Charlie outcome.
     * @param hid Hand id
     */
    @Override
    public void charlie(Hid hid) {
        LOG.info("CHARLIE for hid = "+hid);
        
        AHand hand = manos.get(hid);

        hand.setOutcome(AHand.Outcome.Charlie);

        AMoneyManager money = this.monies.get(hid.getSeat());

        money.increase(hid.getAmt());

        if(hid.getSeat() != Seat.DEALER) {
            SoundFactory.play(Effect.CHARLIE);
            wins++;
            charlies++;
        }
        
        if(sideBetView != null)
            sideBetView.ending(hid);
        
        if(logan != null)
            logan.charlie(hid);
    }

    /**
     * Starts a game.
     * Note: we received the initial player bankroll during login
     * which is handled by GameFrame.
     * @param shoeSize Shoe size
     * @param hids Hand ids in this game
     */
    @Override
    public void starting(List<Hid> hids, final int shoeSize) {       
        numHands = hids.size();

        this.shoeSize = shoeSize;

        // It's nobdy's turn...yet
        turn = null;

        // Game definitely not over
        gameOver = false;

        // Create corresponding (animated) hands
        for (Hid hid: hids) {
            AHand hand =
                    hid.getSeat() == Seat.DEALER ? new ADealerHand(hid) : new AHand(hid);

            // Assign hand to it's manager
            AHandsManager animator = seats.get(hid.getSeat());

            animator.add(hand);

            // Put the hand in cache for quick look up later
            manos.put(hid, hand);
        }
        
        if(logan != null)
            logan.startGame(hids, shoeSize);
    }

    /**
     * Signals end of a game.
     * @param shoeSize Shoe size
     */
    @Override
    public void ending(final int shoeSize) {
        LOG.info("num hands = "+numHands+" wins = "+wins+" loses = "+loses+" pushes = "+pushes);
        
        // Game now over
        gameOver = true;

        // NOTE: numHands includes the dealer
        
        // Play sound if all players win in non-heads up game.
        if (wins == numHands-1 && numHands > 2)
            SoundFactory.play(Effect.NICE);

        // Play a sound if heads up and no sound already played
        else if(wins == 1 && blackjacks == 0 && charlies == 0 && numHands == 2)
            SoundFactory.play(Effect.NICE);

        // Play sound if all players lose in non-heads up
        else if(loses == numHands-1 && numHands > 2)
            SoundFactory.play(Effect.TOUGH);
        
        // Play a sound if player loses in heads up and sound not already played
        else if(loses == 1 && busts == 0 && numHands == 2)
            SoundFactory.play(Effect.TOUGH);
        
        // Update the shoe size
        this.shoeSize = shoeSize;   
        
        if (logan == null) {
            // Enable betting and dealing again
            frame.enableDeal(true);
            this.bettable = true;

            // Disable play -- we must wait for player to upBet and request deal
            frame.enablePlay(false);
        }
        else {
            // Run logan in worker thread in event there's a need for
            // endGame to wait between games.
            new Thread(new Runnable() { 
                @Override
                public void run() {
                    logan.endGame(shoeSize);

                    timeout(2.5);

                    // Clear the table
                    clear();
                    
                    // Do shuffle processing
                    shuffle();
                    
                    // Tell Logan it's time to place a new bet
                    logan.go();
                }
            }).start();

        }
    }

    /**
     * Let's us know dealer shuffling deck after this game ends but before
     * the next game starts.
     */
    @Override
    public void shuffling() {
        burnCard.launch();
        
        shufflePending = true;
        
        if (logan != null)
            logan.shuffling();
    }

    /**
     * Invoked as callback when mouse clicked.
     * @param e Mouse event
     */
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * Register up bets.
     * @param e Mouse event
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if(logan != null)
            return;
        
        if (!bettable)
            return;

        // Get the coordinates of the mouse and let upBet manager
        // determine whether this is a upBet and how much.
        int x = e.getX();
        int y = e.getY();

        // Place main upBet on left-click
        if(SwingUtilities.isLeftMouseButton(e))
            monies.get(Seat.YOU).click(x, y);
        
        // Ditto for the side upBet system on right-click
        if(sideBetView != null && SwingUtilities.isRightMouseButton(e))
            sideBetView.click(x, y);
    }

    /**
     * Toggles button from pressed image to up image.
     * @param e Mouse event
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if(logan != null)
            return;
        
        monies.get(Seat.YOU).unclick();
    }

    /**
     * Invoked when mouse enters the JPanel.
     * @param e Mouse event
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    
    /**
     * Invoked when mouse exists the JPanel.
     * @param e Mouse event
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }
    
    /**
     * Sets the courier.
     * @param courier Courier
     */
    @Override
    public void setCourier(Courier courier) {
        this.courier = courier;
    }
    
    /**
     * Tests whether the Logan has been installed.
     * @return True if auto-pilot enabled.
     */
    public boolean autopilotEngaged() {
        return logan != null;
    }
    
    /**
     * Starts the Logan.
     */
    public void startAutopilot() {
        this.logan.setMoneyManager(this.monies.get(Seat.YOU));
        this.logan.setCourier(courier);       
        if (logan != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    logan.go();
                }
            }).start();
        }
    }
    
    /**
     * Loads the plug-ins.
     */
    protected void loadConfig() { 
        try {
            // Open the configuration file
            props = new Properties();
            props.load(new FileInputStream("charlie.props"));

        } catch (IOException ex) {
            LOG.error("failed to open charlie.props: "+ex);
            return;
        }
        
        loadSideBetSystem();
        loadAutoPilot();
    } 
    
    /**
     * Loads the side upBet system based on the property file setting.
     */
    protected void loadSideBetSystem() {
        try {
            String className = props.getProperty(Constant.PLUGIN_SIDE_BET_VIEW);
            
            if (className == null)
                return;
            
            Class<?> clazz;
            
            clazz = Class.forName(className);
            
            this.sideBetView = (ISideBetView) clazz.newInstance();
            
            this.sideBetView.setMoneyManager(this.monies.get(Seat.YOU)); 
            
            LOG.info("successfully loaded side bet view");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            LOG.error("failed to load side bet view: "+ex);
        }
    }
    
    /**
     * Loads the Logan based on the property file setting.
     */
    protected void loadAutoPilot() {
        try {
            String className = props.getProperty(Constant.PLUGIN_LOGAN);
            
            if (className == null)
                return;
            
            Class<?> clazz;
            
            clazz = Class.forName(className);
            
            this.logan = (ILogan) clazz.newInstance();
                        
            LOG.info("successfully loaded autopilot");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            LOG.error("failed to load autopilog: "+ex);
        }
    } 
    
    /**
     * Do shuffle, if needed.
     * Note: shuffling method only says a shuffle is on the way. This shuffle
     * says to shuffle, if a shuffle is pending.
     */
    public void shuffle() {    
        if(shufflePending) {
            burnCard.clear();
            
            SoundFactory.play(Effect.SHUFFLING);

            shufflePending = false;
            
            timeout(3.0);
        }
    }
    
    /**
     * Pauses for a time.
     * @param seconds Pause time
     */
    protected void timeout(double seconds) {
        try {
            Thread.sleep((long) (seconds * 1000));
        }
        catch(InterruptedException e) {
            
        }
    }

    /**
     * Gets invoked by Courier that dealer has acknowledged split request
     * @param newHid New hand id
     * @param origHid Original (split) hand id
     */
    @Override
    public void split(Hid newHid, Hid origHid) {
        
        // Let us get our 'hand' aka the original hand
        AHand hand = manos.get(origHid);
        
        // We need to remove one of the cards put this in new hand later
        ACard card = (ACard) hand.cards.remove(1);
        
        // Revalue the hands values
        int[] newHandValues = splitRevalue(hand);
        
        // Set the new value for original hand
        hand.setValues(newHandValues);
        
        // Create a new AHand from the new HID
        AHand newHand = new AHand(newHid);
        
        // Let us 'hit' that hand with one of the split cards
        newHand.hit(card);
        
        // Now we will set the value of that hand
        newHand.setValues(newHandValues);
        
        // Add the new hand to the hand hash 'manos'
        manos.put(newHid, newHand);
        
        // Increase so we know there is another hand
        numHands++;
        
        // Add hand to me -- hmm, might want to change this in the future to be any AHandsManager
        you.add(newHand);
        
        // Add bet to table -- do we want to make something different?
        AMoneyManager money = this.monies.get(newHid.getSeat());
        money.split();

        // actions to be taken to update the GameFrame
        this.frame.split(newHid, origHid);
        
        if(this.logan != null){
            this.logan.split(newHid, origHid);
        }
    }
    
    /**
     * Helper function to update an AHands value
     * Typically should only be called during split action
     * @param hand hand to revalue
     * @return hard/soft values.
     */
    private int[] splitRevalue(AHand hand){

        int[] value = new int[2];
        
        // assuming hard/soft value locations do not change
        // an incoming pair of aces should look like this
        // which we will split to 1 & 11, all other pairs 
        // can be div by 2 to update the value
        if(hand.values[0] == 2 && hand.values[1] == 12){
            value[0] = 1;
            value[1] = 11;
            return value;
        }else{
            value[0] = value[1] = hand.values[0] / 2;
            return value;
        }
    }
}