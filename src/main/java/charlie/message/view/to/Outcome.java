/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package charlie.message.view.to;

import charlie.card.Hid;
import charlie.message.Message;

/**
 * This is the base class for all outcomes.
 * @see charlie.message.view.from.Request
 * @author Ron Coleman
 */
abstract public class Outcome extends Message {
    private final Hid hid;
    
    public Outcome(Hid hid) {
        this.hid = hid;
    }

    public Hid getHid() {
        return hid;
    }
}
