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

import charlie.util.Constant;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements a hand which has a unique id, a collection of cards
 * and values.
 * @author Ron Coleman
 */
public class Hand implements Serializable {  
    protected Hid hid;
    protected List<Card> cards = new ArrayList<>();
    protected int[] values;
    
    /**
     * Constructor
     */
    public Hand() {
        this(new Hid());
    }
    
    /**
     * Constructor
     * @param hid Use this hand id.
     */
    public Hand(Hid hid) {
        this.hid = hid;
        values = new int[2];
    }
    
    /**
     * Gets a card in the hand.
     * @param k Index
     * @return Card
     */
    public Card getCard(int k) {
        if(k >= cards.size() || k < 0)
            return null;
        
        return cards.get(k);
    }
    
    /**
     * Gets the number of cards in the hand.
     * @return Number of cards in hand
     */
    public int size() {
        return cards.size();
    }
    
    /**
     * Tests if the hand is broke.
     * @return True if hand > 21
     */
    public boolean isBroke() {
        if(values[Constant.HAND_LITERAL_VALUE] > 21 && values[Constant.HAND_SOFT_VALUE] > 21)
            return true;
        
        return false;
    }
    
    /**
     * Tests if hand has a blackjack.
     * @return True if hand has A+10
     */
    public boolean isBlackjack() {
        if(cards.size() == 2 && values[Constant.HAND_SOFT_VALUE] == 21)
            if(cards.get(0).isAce() || cards.get(1).isAce())
                return true;
        
        return false;
    }
    
    /**
     * Tests if hand has a blackjack.
     * @return True if hand has A+10
     */    
    public boolean isCharlie() {
        if(cards.size() == 5 && getValue() <= 21)
            return true;
        
        return false;
    }
    
    /**
     * Tests if the had is a pair.
     * @return True if hand has 2+2, 3+3, etc.
     */
    public boolean isPair() {
        if(cards.size() != 2)
            return false;
        
        return cards.get(0).rank == cards.get(1).rank;
    }
    
    /**
     * Doubles the bet in the hand.
     */
    public void dubble() {
        this.hid.dubble();
    }
    
    /**
     * Gets the hand id.
     * @return Hand id
     */
    public Hid getHid() {
        return hid;
    }
    
    /**
     * Hits the hand with a card.
     * @param card Card
     */
    public void hit(Card card) {
        this.cards.add(card);

        // If the card is a hole card, don't count it
        if(card instanceof HoleCard)
            return;
        
        Integer value = card.value();
        
        values[Constant.HAND_LITERAL_VALUE] += value;
        values[Constant.HAND_SOFT_VALUE] += value;
        
        if(card.isAce() && values[Constant.HAND_SOFT_VALUE]+10 <= 21)
            values[Constant.HAND_SOFT_VALUE] += 10;
    }
    
    /**
     * Splits the cards into two hands and returns the newly created hand.
     * @param newHid the new Hand ID for the hand.
     * @return Hand the newly created hand.
     */
    public Hand split(Hid newHid){
        
        // Create a new hand
        Hand newHand = new Hand(newHid);
                
        // WARNING::REMOVING THE UN-NEEDED CARD WHILE RETURNING IT.
        // Should use setter and getter here?
        // altering original hand from constructor - deep copy instead?
        Card card = this.cards.remove(1);
        
        // Update "this" hand's values
        this.revalue();
        
        // Give the new hand a card, the card we removed from this hand
        newHand.hit(card);
        
        // Now return the new hand
        return newHand;
    }
        
    /**
     * Revalues the hand.
     * This method uses all cards, including the hole hard. The method is
     * typically invoked when the dealer plays, that is, upon showing the
     * hole card.
     */
    public void revalue() {
        values[Constant.HAND_LITERAL_VALUE] = values[Constant.HAND_SOFT_VALUE] = 0;
        
        for(Card card: cards) {
            int value = card.value();
            
            values[Constant.HAND_LITERAL_VALUE] += value;
            values[Constant.HAND_SOFT_VALUE] += value;
            
            if(card.isAce() && values[Constant.HAND_SOFT_VALUE]+10 <= 21)
                values[Constant.HAND_SOFT_VALUE] += 10;
        }
    }
    
    /**
     * Gets the hands literal and soft values.
     * @return Hand values
     */
    public int[] getValues() {
        return values;
    }
    
    /**
     * Gets the hand unified value. 
     * @return Hand value
     */
    public int getValue() {
        return Hand.getValue(values);        
    }
    
    /**
     * Get hand unified value.
     * Typically this is the soft value with the literal value as the backup.
     * @param values
     * @return 
     */
    public static int getValue(int[] values) {
        return values[Constant.HAND_SOFT_VALUE] <= 21 ?
                values[Constant.HAND_SOFT_VALUE] :
                values[Constant.HAND_LITERAL_VALUE];        
    }
    
    /**
     * Converts a hand to a string.
     * This method shows all the cards in the hand.
     * @return String
     */
    @Override
    public String toString() {
        String s = "";
        for(int i=0; i < cards.size(); i++) {
            s += "[" + cards.get(i) + "] ";
        }
        
        return s;
    }

}
