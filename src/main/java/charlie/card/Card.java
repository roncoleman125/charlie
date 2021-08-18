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

import java.io.Serializable;

/**
 * This class implements a card which is stored en mass in a shoe.
 * @author Ron Coleman
 */
public class Card implements Serializable {
    public enum Suit { SPADES, CLUBS, HEARTS, DIAMONDS };
    public final static int ACE = 1;
    public final static int JACK = 11;
    public final static int QUEEN = 12;
    public final static int KING = 13;
    protected final int rank;
    private final Suit suit;
    
    /**
     * Copy constructor
     * @param card Card to copy
     */
    public Card(Card card) {
        this.suit = card.suit;
        this.rank = card.rank;
    }
    
    /**
     * Constructor
     * @param rank Rank
     * @param suit Suit
     */
    public Card(Integer rank,Suit suit) {
        this.suit = suit;
        this.rank = rank;
    }
    
    /**
     * Gets card value.
     * @return Integer value
     */
    public Integer value() {
        // All face cards, J, Q, K, have value 10
        if(isFace())
            return 10;
        
        // Otherwise, the value is the card rank
        return rank;
    }
    
    /**
     * Tests if the card is an ace.
     * @return True if card is an ace, false otherwise.
     */
    public boolean isAce() {
        return rank == 1;
    }
    
    /**
     * Tests if the card has a face, J, Q, K.
     * @return True if card has a face, false otherwise.
     */
    public boolean isFace() {
        return rank >=11 && rank <= 13;
    }
    
    /**
     * Gets card name
     * @return Number (e.g., 2, 3, etc.) or face (A, J, Q, K)
     */
    public String getName() {
        if(!isFace() && !isAce())
            return rank + "";
        
        if(rank == ACE)
            return "A";
        
        else if(rank == JACK)
            return "J";
        
        else if(rank == QUEEN)
            return "Q";
        
        else if(rank == KING)
            return "K";
        
        else
            return "?";
    }

    /**
     * Gets the card rank.
     * @return Rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * Gets the suit.
     * @return Suit
     */
    public Suit getSuit() {
        return suit;
    }
    
    /**
     * Converts card including suit and name to string.
     * @return Card as a string
     */
    @Override
    public String toString() {
        return suit.toString().charAt(0) + "" + getName();
    }
}
