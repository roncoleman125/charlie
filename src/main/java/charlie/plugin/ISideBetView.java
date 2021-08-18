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
package charlie.plugin;

import charlie.card.Hid;
import charlie.view.AMoneyManager;
import java.awt.Graphics2D;

/**
 * This interface defines behavior of the side bet view.
 * @author Ron Coleman
 */
public interface ISideBetView {
    /**
     * Sets the money manager.
     * @param moneyManager 
     */
    public void setMoneyManager(AMoneyManager moneyManager);
    
    /**
     * Handles clicks in this region of the UI.
     * @param x X coordinate
     * @param y Y coordinate
     */
    public void click(int x, int y);
    
    /**
     * Reports side bet outcome at the end of the game for hand with hand id.
     * If the side bet amount is &lt;0, the player lost.
     * If the side bet amount is >&gt;0 the player won.
     * If the side bet amount is 0, there was no side bet.
     * @param hid Hand id
     */
    public void ending(Hid hid);
    
    /**
     * Indicates new game starting.
     */
    public void starting();
    
    /**
     * Gets the side bet amount in dollars
     * @return Side bet amount.
     */
    public Integer getAmt();
    
    /**
     * Updates the side bet
     */
    public void update();
    
    /**
     * Renders the side bet.
     * @param g Graphics context
     */
    public void render(Graphics2D g);
}
