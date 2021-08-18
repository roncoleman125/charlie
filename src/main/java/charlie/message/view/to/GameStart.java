/*
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
package charlie.message.view.to;

import charlie.card.Hid;
import charlie.message.Message;
import java.util.List;

/**
 * This message tells all players the game is starting with all hands in the game.
 * @author Ron Coleman
 */
public class GameStart extends Message {
    private final List<Hid> hids;
    private final int shoeSize;
    
    /**
     * Constructor
     * @param hids Hand ids playing in the game.
     * @param shoeSize Starting shoe size
     */
    public GameStart(List<Hid> hids,int shoeSize) {
        this.hids = hids;
        this.shoeSize = shoeSize;
    }

    /**
     * Gets the hand ids.
     * @return Hand ids
     */
    public List<Hid> getHids() {
        return hids;
    }

    /**
     * Gets the shoe size.
     * @return Shoe size
     */
    public int shoeSize() {
        return shoeSize;
    }
}
