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

import charlie.util.Constant;
import java.awt.Graphics2D;
import javax.swing.ImageIcon;

/**
 * This class implements the player turn indicator.
 * @author Ron Coleman
 */
public class TurnIndicator extends Sprite {  
    private boolean visible = false;
    
    /**
     * Constructor
     */
    public TurnIndicator() {
        String path = Constant.DIR_IMGS + "arrow-180-1.png";
        
        ImageIcon icon = new ImageIcon(path);

        img = icon.getImage();
    }
    
    /**
     * Renders the indicator.
     * @param g Graphics context
     */
    @Override
    public void render(Graphics2D g) {
        if(visible)
            super.render(g);
    }
    
    /**
     * Toggles the indicator visibility.
     * @param b Boolean true/false
     */
    public void show(boolean b) {
        this.visible = b;
    }
}
