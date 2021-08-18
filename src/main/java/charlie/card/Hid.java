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
package charlie.card;

import charlie.dealer.Seat;
import charlie.util.Constant;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.Random;
import org.apache.log4j.Logger;

/**
 * This class implements the hand id.
 * The hand id is ubiquitous and serves as the key to managing hands throughout
 * a game. A hand id does not have meaning apart from a hand. Thus, if there is
 * a hand id, there is a corresponding hand somewhere in the game.
 * @author Ron Coleman
 */
public class Hid implements Serializable {
    private final static Logger LOG = Logger.getLogger(Hid.class);
    private static Random ran = new Random();
    private static String host = "UNKNOWN";
    private Long key = Math.abs(ran.nextLong());
    private Seat seat = Seat.YOU;
    protected double amt = 0.0;
    protected double sideAmt = 0.0;
    
    /**
     * Set if this hand was created due to a split
     * For now, it's part of my "handIndex" solution 
     * on the server side .... 
     */
    private boolean split = false;
    
    static {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            
            Hid.host = addr.getHostName();           
        }
        catch(UnknownHostException e) {
            LOG.error(e.toString());
        }        
    }
    
    /**
     * Constructor uses all defaults.
     */
    public Hid() {

    }
    
    /**
     * Copy constructor
     * @param hid Hand id
     */
    public Hid(Hid hid) {
        this.key = hid.key;
        this.seat = hid.seat;
        this.amt = hid.amt;
        this.sideAmt = hid.sideAmt;
    }
    /**
     * Constructor
     * @param seat Hand id for this seat
     * @param amt Main bet amount
     * @param sideAmt Side bet amount
     */
    public Hid(Seat seat, double amt, double sideAmt) {     
        this.amt = amt;
        this.sideAmt = sideAmt;
        this.seat = seat;
    }
    
    /**
     * Constructor
     * @param seat Seat
     */
    public Hid(Seat seat) {
        this(seat,Constant.MIN_BET,0.0);
    }

    /**
     * Gets the amount for this hand.
     * @return Amount
     */
    public double getAmt() {
        return amt;
    }

    /**
     * Sets the amount for this hand.
     * @param amt Amount
     */
    public void setAmt(double amt) {
        this.amt = amt;
    }
    
    /**
     * Multiplies bet by some factor.
     * @param factor the multiplier
     */
    public void multiplyAmt(double factor) {
        this.amt *= factor;
    }
    
    /**
     * Doubles the bet amount.
     */
    public void dubble() {
        multiplyAmt(2.0);
    }

    /**
     * Gets the seat.
     * @return A seat id
     */
    public Seat getSeat() {
        return seat;
    }

    /**
     * Sets the seat.
     * @param seat Seat
     */
    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    /**
     * Gets the side bet amount.
     * @return Double
     */
    public Double getSideAmt() {
        return sideAmt;
    }

    /**
     * Sets the side bet amount.
     * @param sideAmt Side bet amount
     */
    public void setSideAmt(double sideAmt) {
        this.sideAmt = sideAmt;
    }
    
    /**
     * Gets the split notification, if hand was created due to a split
     * @return boolean true if hand was created due to a split
     */
    public boolean getSplit(){
        return this.split;
    }
    
    /**
     * Sets the split notification, if hand was created due to a split
     * @param split true or false whether it was created due to split
     */
    public void setSplit(boolean split){
        this.split = split;
    }
    
    /**
     * Gets the string representation of the hand id.
     * @return String representation
     */
    @Override
    public String toString() {
        return host + ":" + seat + ":" +Long.toHexString(this.key).toUpperCase();
    }
    
    /**
     * Hashes this object to insure hash code based on hand id.
     * @return Hash code
     */
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    /**
     * Tests if object is an hand id.
     * @param obj Other object
     * @return True if hands are equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Hid other = (Hid) obj;
        return Objects.equals(this.key, other.key);
    }
}
