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

import java.awt.Image;

/**
 * This class implements a chip sprite.
 * @author Ron Coleman
 */
public class Chip extends Sprite {
    protected final int amt;
    
    /**
     * Copy constructor
     * @param chip Chip
     */
    public Chip(Chip chip) {
        this(chip.img,chip.x,chip.y,chip.amt);
    }
    
    /**
     * Constructor
     * @param img Image
     * @param x X coordinate
     * @param y Y coordinate
     * @param amt Amount
     */
    public Chip(Image img, int x, int y, int amt) {
        super(x,y);
        super.img = img;
        this.amt = amt;
    }

    /**
     * Gets the chip amount.
     * @return Dollar amount
     */
    public int getAmt() {
        return amt;
    }   
}
