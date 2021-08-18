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

import charlie.card.Hand;
import charlie.card.Hid;
import charlie.view.sprite.TurnIndicator;
import charlie.util.Constant;
import charlie.util.Point;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Animated hand
 * @author Ron Coleman, Ph.D.
 */
public class AHand {
    // Hand outcomes
    public enum Outcome { None, Blackjack, Win, Push, Lose, Charlie, Bust };
    
    // Card offsets to create "staggered" look
    protected final static int HOME_OFFSET_X = 15;
    protected final static int HOME_OFFSET_Y = 7;
    
    protected List<ACard> cards = new ArrayList<>();
    protected int[] values;
    protected Font stateFont = new Font("Arial", Font.PLAIN, 18);
    protected Font outcomeFont = new Font("Arial", Font.BOLD, 18);
    protected Color stateColor = Color.WHITE;
    protected Color looseColorBg = new Color(250,58,5);
    protected Color looseColorFg = Color.WHITE;
    protected Color winColorFg = Color.BLACK;
    protected Color winColorBg = new Color(116,255,4);
    protected Color pushColorFg = Color.BLACK;
    protected Color pushColorBg = Color.CYAN;
    protected Point home;
    protected String name = "NOBODY";
    protected Outcome outcome = Outcome.None; 
    protected TurnIndicator turnSprite = new TurnIndicator();
    protected final Hid hid;

    /**
     * Constructor
     * @param hid Hand id
     */
    public AHand(Hid hid) {
        this.hid = hid;
        this.home = new Point(0, 0);
    }

    /**
     * Updates hand.
     */
    public void update() {
        int sz = cards.size();
        for (int i = 0; i < sz; i++) {
            ACard card = cards.get(i);
            card.update();
        }
    }

    /**
     * Renders hand.
     * @param g Graphics context
     */
    public void render(Graphics2D g) {
        if (cards.isEmpty())
            return;

        for (int i = 0; i < cards.size(); i++) {
            ACard card = cards.get(i);
            card.render(g);
        }
        
        String text = this.getText();
        
        renderState(g,text);
    }

    /**
     * Renders hand state, e.g., value plus win, loose, etc.
     * @param g Graphics context
     * @param text Message
     */
    protected void renderState(Graphics2D g, String text) {
        try {
            // Paint the turn indicator
            int indicatorWidth = turnSprite.getImage().getWidth(null);
            int cardWidth = ACard.getCardWidth();
            
            int xoff = cardWidth / 2 - indicatorWidth / 2;
            int yoff = -turnSprite.getImage().getHeight(null) - 5;

            int x = home.getX() + xoff;
            int y = home.getY() + yoff;

            Point pos = new Point(x, y);

            turnSprite.setXY(pos);

            turnSprite.render(g);

            // Paint the state
            x += turnSprite.getWidth() + 5;
            y += turnSprite.getHeight() / 2 + 15;

            g.setColor(stateColor);
            g.setFont(stateFont);

            g.drawString(text, x, y);

            int sz = cards.size();
            if(sz == 0 || outcome == Outcome.None)
                return;
            
            // Figure the outcome text
            String outcomeText = "";             
            if(outcome != Outcome.None)
                outcomeText += " " + outcome.toString().toUpperCase() + " ! "; 
            
            // Calculate the outcome position
            int cardHeight = ACard.getCardHeight();
            x = cards.get(sz-1).getHome().getX() + cardWidth - 100; // was 15 trying to center more
            y = cards.get(sz-1).getHome().getY() + cardHeight / 2; 
            
            FontMetrics fm = g.getFontMetrics(outcomeFont);
            int w = fm.charsWidth(outcomeText.toCharArray(), 0, outcomeText.length());
            int h = fm.getHeight();
            
            // Paint the outcome background            
            if (outcome == Outcome.Lose || outcome == Outcome.Bust)
                g.setColor(looseColorBg);
            
            else if(outcome == Outcome.Push)
                g.setColor(pushColorBg);
            else
                g.setColor(winColorBg);    
            
            g.fillRoundRect(x, y-h+5, w, h, 5, 5);
            
            // Paint the outcome foreground            
            if (outcome == Outcome.Lose || outcome == Outcome.Bust)
                g.setColor(looseColorFg);
            
            else if(outcome == Outcome.Push)
                g.setColor(pushColorFg);
            else
                g.setColor(winColorFg);    
            
            g.setFont(outcomeFont);
            g.drawString(outcomeText,x,y);
        }
        catch (Exception e) {
        }
    }
    
    /**
     * Gets the message text.
     * @return Message
     */
    protected String getText() {
        int value = values[Constant.HAND_SOFT_VALUE] <= 21 ?
                values[Constant.HAND_SOFT_VALUE] :
                values[Constant.HAND_LITERAL_VALUE];

        String text = name + ": "+value;
        
        return text;
    }
    
    /**
     * Hits this hand with a card.
     * @param card Card
     */
    public void hit(ACard card) {
        int xoff = cards.size() * HOME_OFFSET_X;
        int yoff = cards.size() * HOME_OFFSET_Y;

        Point myhome = new Point(home.getX() + xoff, home.getY() + yoff);

        cards.add(new ACard(card, myhome));
    }

    /**
     * Tests whether the hand is ready, i.e., whether all cards have "landed".
     * @return 
     */
    public boolean isReady() {
        for(int i=0; i < cards.size(); i++) {
            if(!cards.get(i).isLanded())
                return false;
        }
        
        return true;
    }

    /**
     * Gets the hand id.
     * @return Hand id
     */
    public Hid getHid() {
        return hid;
    }
    
    /**
     * Sets a new home for the hand. This causes all the cards in the hand to
     * move.
     *
     * @param home New home
     */
    public void setHome(Point home) {
        this.home = home;

        int x = home.getX();
        int y = home.getY();

        // Move all the cards in the hand to their new home
        for (int k = 0; k < cards.size(); k++) {
            int xoff = k * HOME_OFFSET_X;
            int yoff = k * HOME_OFFSET_Y;

            ACard card = cards.get(k);

            card.setHome(new Point(x + xoff, y + yoff));
        }
    }

    /**
     * Gets the hands home location
     * @return Point
     */
    public Point getHome() {
        return home;
    }

    /**
     * Sends all cards to their home point.
     */
    public void settle() {
        for (ACard card : cards) {
            card.settle();
        }
    }

    /**
     * Splits the hand which reduces the hand by one card
     * and returns the last card.
     *
     * @return Animated card
     */
    public ACard split() {
        int lastIndex = cards.size() - 1;

        if (lastIndex == -1)
            return null;

        ACard card = cards.get(lastIndex);

        cards.remove(lastIndex);

        return card;
    }

    /**
     * Sets the hand values, hard and soft.
     * @param values Values
     */
    public void setValues(int[] values) {
        this.values = values;
    }

    /**
     * Gets the width of the card pile.
     * @return Pile width
     */
    public Integer getPileWidth() {
        if (cards.isEmpty()) {
            return 0;
        }

        return cards.get(0).getWidth() + (cards.size() - 1) * HOME_OFFSET_X;
    }

    /**
     * Gets the height of the card pile.
     * @return Pile height
     */
    public Integer getPileHeight() {
        if (cards.isEmpty()) {
            return 0;
        }

        return cards.get(0).getHeight() + (cards.size() - 1) * HOME_OFFSET_Y;
    }

    /**
     * Gets hand name
     * @return Name of hand
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the hand name.
     * @param name Name of hand
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Gets the number of cards in the hand.
     * @return Hand size
     */
    public Integer size() {
        return cards.size();
    }
    
    /**
     * Gets the kth card in the hand.
     * @param k
     * @return 
     */
    public ACard get(Integer k) {
        if(k >= cards.size())
            return null;
        
        return cards.get(k);
    }
    
    /**
     * Toggles the turn indicator for the hand.
     * If playing is true, the turn indicator is enabled.
     * @param playing 
     */
    public void enablePlaying(boolean playing) {
        this.turnSprite.show(playing);
    }

    /**
     * Sets the hand outcome
     * @param outcome Outcome
     */
    public void setOutcome(Outcome outcome) {
        this.outcome = outcome;
    }

    /**
     * Tests whether the hand is broke.
     * @return True if the hand is broke.
     */
    public boolean isBroke() {
        return Hand.getValue(values) > 21;
    }
    
}
