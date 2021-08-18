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

import charlie.card.Card.Suit;
import charlie.plugin.IShoe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.apache.log4j.Logger;

/**
 * This class implements a six deck shoe, the standard in many houses.
 * This class is also the base class for all other shoes.
 * @author Ron Coleman
 */
public class Shoe  implements IShoe {
    private final Logger LOG = Logger.getLogger(Shoe.class);
    
    protected Integer numDecks = 6;
    protected List<Card> cards = new ArrayList<>();
    protected Integer index = 0;
    protected int burnIndex = Integer.MAX_VALUE;
    protected Random ran = new Random(System.nanoTime());
    
    /**
     * Constructor
     */
    public Shoe() {

    }
    
    /**
     * Constructor
     * @param numDecks Number of decks
     */
    public Shoe(int numDecks) {
        this.numDecks = numDecks;
    }
    
    /**
     * Initializes the shoe.
     */
    @Override
    public void init() {        
        load();
        
        shuffle();        
    }
       
    /**
     * Loads the shoe with cards.
     */
    protected final void load() {
        cards.clear();
        for(int deckno=0; deckno < this.numDecks; deckno++) {
            for(int rank=1; rank <= 13; rank++) {
                for(Suit suit: Suit.values()) {
                    cards.add(new Card(rank,suit));
                }
            }
        }        
    }
    
    /**
     * Shuffles cards in the shoe.
     */
    @Override
    public void shuffle() {
        Collections.shuffle(cards,ran);
        
        index = 0;
                
        int next13 = ran.nextInt(13);
        
        int cutSize = Math.min(next13 + 13, cards.size() / 3);
        
//        burnIndex = cards.size() - ran.nextInt(13) - 13;
        burnIndex = cards.size() - cutSize;
        
        LOG.info("shuffling burn index = "+burnIndex);
    }
    
    /**
     * Gets the next card.
     * @return A card, if there is one
     */
    @Override
    public Card next() {
        LOG.info("cards size = "+cards.size()+" burn index = "+burnIndex+" index = "+index);
        
        if(index >= cards.size()) {
            LOG.error("shoe empty!");
            return null;
        }
        
        return cards.get(index++);
    }
    
    /**
     * Tests if shoe has another card.
     * @return True if shoe has another card, false otherwise.
     */
    public boolean hasNext() {
        return index < cards.size();
    }
    
    /**
     * Tests if shoe needs shuffling.
     * @return True if we've reach the burn card, false otherwise.
     */
    @Override
    public boolean shuffleNeeded() {
        LOG.info("index = "+index+" burnIndex = "+burnIndex+" shuffle needed = "+(index>=burnIndex));
        return index >= burnIndex;
    }
    
    /**
     * Gets number of cards still in the shoe.
     * @return Number cards in shoe
     */
    @Override
    public int size() {
        return cards.size() - index;
    }
    
    /**
     * Converts shoe to string.
     * @return String
     */
    @Override
    public String toString() {
        String s = "";
        for(int i=index, count=0; i < cards.size() && count < 15; i++, count++)
            s += cards.get(i) + " ";
        return this.getClass().getName()+": "+s;
    }
}
