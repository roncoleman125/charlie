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

import charlie.util.Constant;
import charlie.util.Point;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * This class implements the "burn" card.
 * @author Ron Coleman
 */
public final class ABurnCard extends ACard { 
    /**
     * Constructor
     */
    public ABurnCard() {
        super(ABurnCard.getBurnImage(),new Point(0,0),new Point(0,0));
        
        clear();
    }

    /**
     * Tests whether card is on the table.
     * @return True if the card is on the table, false otherwise
     */
    public boolean isVisible() {
        return x+ACard.getCardWidth() > 0;
    }
    
    /**
     * Clears card from the table.
     */
    public void clear() {
        // Setting the home causes the card to (re)seek its home
        this.home.setX(-2*ACard.getCardWidth());
    }
    
    /**
     * Sends card to table.
     */
    public void launch() {
        this.home = new Point(375,150);
        
        this.x = Constant.SHOE_X;
        this.y = Constant.SHOE_Y;
    }
    
    /**
     * Renders the card
     * @param g Graphic context
     */
    @Override
    public void render(Graphics2D g) {
//        super.render(g);
//        
//        int w = img.getWidth(null);
//        int h = img.getHeight(null);
//
//        g.setColor(Color.RED);
//
//        g.drawRect(x, y, w, h);
        if(img == null)
            return;
        
        g.drawImage(img, x, y,null);
    }
    
    /**
     * Gets the burn card image.
     * @return Image for the burn card.
     */
    public static Image getBurnImage() {
        String path = Constant.DIR_IMGS + "burn-card-2.png";
        
        Image img = imgCache.get(path);
        
        if (img == null) {
            ImageIcon icon = new ImageIcon(path);
            
            img = icon.getImage();
            
            imgCache.put(path, img);
        }
        
        return img;
    }   
}
