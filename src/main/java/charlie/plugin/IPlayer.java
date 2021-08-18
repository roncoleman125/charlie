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
import charlie.card.Hid;
import java.util.List;

/**
 * Messages sent to the player from dealer.
 * @author Ron Coleman
 */
public interface IPlayer {    
    /**
     * Starts game with list of hand ids and shoe size.
     * The number of decks is shoeSize / 52.
     * @param hids Hand ids
     * @param shoeSize Starting shoe size
     */
    abstract public void startGame(List<Hid> hids,int shoeSize);
    
    /**
     * Ends a game with shoe size.
     * @param shoeSize Shoe size
     */
    abstract public void endGame(int shoeSize);
    
    /**
     * Deals a card to player.
     * All players receive all cards which is useful for card counting.
     * @param hid Hand id which might not necessarily belong to player.
     * @param card Card being dealt
     * @param values Hand values, literal and soft
     */
    abstract public void deal(Hid hid, Card card, int[] values);
    
    /**
     * Offers player chance to buy insurance.
     */
    abstract public void insure();
    
    /**
     * Tells player the hand is broke.
     * @param hid Hand id
     */
    abstract public void bust(Hid hid);
    
    /**
     * Tells player the hand won.
     * @param hid Hand id
     */    
    abstract public void win(Hid hid);
    
    /**
     * Tells player the hand won with blackjack.
     * @param hid Hand id
     */     
    abstract public void blackjack(Hid hid);
    
    /**
     * Tells player the hand won with Charlie.
     * @param hid Hand id
     */     
    abstract public void charlie(Hid hid);
    
    /**
     * Tells player the hand lost to dealer.
     * @param hid Hand id
     */     
    abstract public void lose(Hid hid);
    
    /**
     * Tells player the hand pushed.
     * @param hid Hand id
     */     
    abstract public void push(Hid hid);
    
    /**
     * Tells player shoe to be shuffled before the start of the next game.
     */     
    abstract public void shuffling();
    
    /**
     * Tells player to start playing hand.
     * @param hid Hand id
     */     
    abstract public void play(Hid hid);
    
    /**
     * Tells player to split the hand.
     * @param newHid the new hand id
     * @param origHid the original hand id
     */
    abstract public void split(Hid newHid, Hid origHid);
    
}
