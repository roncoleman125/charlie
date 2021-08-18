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
package charlie.plugin;

import charlie.card.Card;

/**
 * This interface defines behaviors of the shoe.
 * @author Ron Coleman, Ph.D.
 */
public interface IShoe {
    /**
     * Initializes the shoe. Allocating the cards and shuffling them, if needed.
     * here.
     */
    public void init();
    
    /**
     * Tests if the shoe needs to be shuffled.
     * @return True if the shoe needs to be shuffled, false otherwise.
     */
    public boolean shuffleNeeded();
    
    /**
     * Shuffles the cards in the shoe.
     */
    public void shuffle();
    
    /**
     * Gets the next card, if any in the shoe.
     * @return Card or null if the shoe is empty
     */
    public Card next();
    
    /**
     * Gets the shoe size.
     * @return Number of cards in the shoe
     */
    public int size();
}
