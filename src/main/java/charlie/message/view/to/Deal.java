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

import charlie.card.Card;
import charlie.card.Hid;
import charlie.message.Message;

/**
 * This message carries a card being dealt to hand.
 * @author Ron Coleman
 */
public class Deal extends Message {  
    private final Hid hid;
    private final Card card;
    private final int[] values;
    
    /**
     * Constructor
     * @param hid Hand id
     * @param values Hand values, literal and soft
     * @param card Card being dealt
     */
    public Deal(Hid hid, int[] values, Card card) {
        this.hid = hid;
        this.values = values;
        this.card = card;
    }

    /**
     * Gets hand id
     * @return Hand id
     */
    public Hid getHid() {
        return hid;
    }

    /**
     * Gets card.
     * @return Card
     */
    public Card getCard() {
        return card;
    }
    
    /**
     * Gets hand values.
     * @return Literal and soft values
     */
    public int[] getHandValues() {
        return values;
    }
}
