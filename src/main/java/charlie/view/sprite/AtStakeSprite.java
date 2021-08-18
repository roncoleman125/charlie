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
package charlie.view.sprite;

import charlie.card.Hid;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

/**
 * This class implement the at-stake (amount bet) sprite.
 * @author Ron Coleman
 */
public class AtStakeSprite {
    public final static int DIAMETER = 50;
    protected Integer amt;
    protected Font font = new Font("Arial", Font.BOLD, 18);
    protected Color fontColor = Color.YELLOW;
    protected BasicStroke stroke = new BasicStroke(3);
    private final int xHome;
    private final int yHome;
    
    /**
     * Constructor
     * @param x Home x
     * @param y Home y
     * @param amt Amount at stake (ie, the bet amount)
     */
    public AtStakeSprite(int x, int y, Integer amt) {
        this.xHome = x;
        this.yHome = y;
        this.amt = amt;
    }
    
    /**
     * Zeros the bet amount
     */
    public void zero() {
        this.amt = 0;
    }
    
    /**
     * Doubles the bet amount.
     * @param hid Hand ID to double wager
     */
    public void dubble(Hid hid) {
        this.amt += (int) (hid.getAmt() / 2);
    }
    
    /**
     * Renders the bet amount.
     * @param g Graphics context
     */
    public void render(Graphics2D g) {
        g.setColor(Color.BLACK);
                
        g.setStroke(stroke);
        g.drawOval(xHome, yHome, DIAMETER, DIAMETER);
        
        g.setColor(fontColor);
        g.setFont(font);

        String text = amt + "";
        FontMetrics fm = g.getFontMetrics(font);

        int x = xHome + DIAMETER/2 - fm.charsWidth(text.toCharArray(), 0, text.length()) / 2;
        int y = yHome + DIAMETER/2 + fm.getHeight() / 4;
        
        g.drawString(amt+"", x, y);
    }

    /**
     * Gets the bet amount.
     * @return Bet amount at stake
     */
    public Integer getAmt() {
        return amt;
    }

    /**
     * Sets the bet amount at stake.
     * @param amt Bet amount
     */
    public void setAmt(Integer amt) {
        this.amt = amt;
    }
    
    /**
     * Increases the bet amount.
     * @param amt Bet amount
     */
    public void increase(Integer amt) {
        this.amt += amt;
    }
    
    /**
     * Tests if (x,y) is over the bet amount.
     * @param x X coordinate
     * @param y Y coordinate
     * @return True if (x,y) is within the bet amount region.
     */
    public boolean isPressed(int x, int y) {
        if( x > this.xHome && x < this.xHome+DIAMETER && y > this.yHome && y < this.yHome+DIAMETER) {
            return true;
        }
        
        return false;
    }    
}
