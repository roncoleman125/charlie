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

import java.awt.Graphics2D;
import java.awt.Image;

/**
 * This class implements a chip button.
 * @author Ron Coleman
 */
public class ChipButton {
    protected final Image up;
    protected final Image down;
    protected boolean pressed = false;
    private final int x;
    private final int y;
    private int width;
    private int height;
    protected boolean ready = true;
    protected int amt = 0;
    
    /**
     * Constructor
     * @param amt Dollar value of chip
     * @param up Up image
     * @param down Down image
     * @param x X coordinate
     * @param y Y coordinate
     */
    public ChipButton(int amt, Image up, Image down,int x, int y) { 
        this.amt = amt;
        this.up = up;
        this.down = down;
        this.x = x;
        this.y = y;
        this.width = this.height = up.getWidth(null);
    }
    
    /**
     * Renders the button.
     * @param g Graphics context
     */
    public void render(Graphics2D g) {
        if(!pressed)
            g.drawImage(up, x, y, null);
        else
            g.drawImage(down, x, y, null);
    }
    
    /**
     * Tests if (x,y) is over the button.
     * @param x X coordinate
     * @param y Y coordinate
     * @return True if (x,y) is over the button, false otherwise
     */
    public boolean isPressed(int x, int y) {
        if( x > this.x && x < this.x+width && y > this.y && y < this.y+height) {
            pressed = true;
            ready = false;
        }
        
        return pressed;
    }
    
    /**
     * Releases the button.
     */
    public void release() {
        pressed = false;
        ready = true;
    }
    
    /**
     * Tests if the button is ready, ie, it can be clicked again.
     * @return True if so, false otherwise
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * Gets the dollar value of the button.
     * @return Amount
     */
    public int getAmt() {
        return amt;
    }
    
    /**
     * Gets the button up image.
     * @return 
     */
    public Image getImage() {
        return up;
    }
    
    /**
     * Presses this button.
     */
    public void pressed() {
        pressed = true;
        ready = false;        
    }
}
