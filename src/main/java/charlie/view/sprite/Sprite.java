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

import charlie.util.Point;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 * This is a base class representing a sprite.
 * @author Ron Coleman
 */
abstract public class Sprite {
    protected Image img;
    protected int x;
    protected int y;
    
    /**
     * Constructor
     */
    public Sprite() {
        
    }
    
    /**
     * Constructor
     * @param x X coordinate
     * @param y Y Coordinate
     */
    public Sprite(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Renders the sprite.
     * @param g Graphics context
     */
    public void render(Graphics2D g) {
        // Majic: possible synchronizaton problem here
        if(img == null)
            return;
        
        g.drawImage(this.img,x,y,null);
    }
    
    /**
     * Updates the sprite.
     */
    public void update() {
        
    }

     /**
     * Get the value of x
     *
     * @return the value of x
     */
    public int getX() {
        return x;
    }

    /**
     * Set the value of x
     *
     * @param x new value of x
     */
    public void setX(int x) {
        this.x = x;
    }


    /**
     * Get the value of y
     *
     * @return the value of y
     */
    public int getY() {
        return y;
    }

    /**
     * Set the value of y
     *
     * @param y new value of y
     */
    public void setY(int y) {
        this.y = y;
    }
    
    /**
     * Sets x, y jointly.
     * @param p Point
     */
    public void setXY(Point p) {
        this.x = p.getX();
        this.y = p.getY();
    }

    /**
     * Gets sprite width.
     * @return Width
     */
    public int getWidth() {
        return img.getWidth(null);
    }
    
    /**
     * Gets sprite height.
     * @return Height
     */
    public int getHeight() {
        return img.getHeight(null);
    }
    
    /**
     * Gets sprite image.
     * @return Image
     */
    public Image getImage() {
        return img;
    }
}
