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

import charlie.actor.Courier;
import charlie.card.Hid;
import charlie.card.Card;
import java.util.List;

/**
 * This is the interface to the UI from Courier's point of view.
 * @author Ron Coleman
 */
public interface IUi {
    abstract public void deal(Hid hid, Card card, int[] handValues);
    abstract public void turn(Hid hid);
    abstract public void bust(Hid hid);
    abstract public void win(Hid hid);
    abstract public void lose(Hid hid);
    abstract public void push(Hid hid);
    abstract public void blackjack(Hid hid);
    abstract public void charlie(Hid hid);
    abstract public void starting(List<Hid>hids,int shoeSize);
    abstract public void ending(int shoeSize);
    abstract public void shuffling();
    abstract public void setCourier(Courier courier);
    
    // added for Split Implementation 
    abstract public void split(Hid newHid, Hid origHid);
}
