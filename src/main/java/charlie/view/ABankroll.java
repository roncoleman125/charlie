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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * This class implements the bankroll rendering.
 * @author Ron Coleman
 */
public class ABankroll {
    protected Double amount;
    protected Font font = new Font("Arial", Font.BOLD, 18);
    private final int x;
    private final int y;
    
    /**
     * Constructor
     * @param x X position
     * @param y Y position
     * @param amt Amount in the bankroll.
     */
    public ABankroll(int x, int y, Double amt) {
        this.x = x;
        this.y = y;
        this.amount = amt;
    }
    
    /**
     * Increases the bankroll.
     * @param amt Amount of increase
     */
    public void increase(Double amt) {
        this.amount += amt;
    }
    
    /**
     * Decreases the bankroll
     * @param amt Amount of decrease
     */
    public void decrease(Double amt) {
        this.amount -= amt;
    }    

    /**
     * Gets the bankroll.
     * @return Bankroll amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Sets the bankroll.
     * @param amount Amount
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    
    /**
     * Randers the bankroll.
     * @param g Graphics context
     */
    public void render(Graphics2D g) {
        g.setFont(font);
        String remark = "";
        if(amount >= 100)
            g.setColor(Color.WHITE);
        else if(amount > 50 && amount < 100) {
            g.setColor(Color.YELLOW);
            remark = " !";
        }
        else {
            g.setColor(Color.CYAN);
            remark = " !!!!!";
        }
        
        String t = String.format("%7.2f%s",amount,remark);
        g.drawString("Bankroll:  $ "+t, x, y);        
    }
}
