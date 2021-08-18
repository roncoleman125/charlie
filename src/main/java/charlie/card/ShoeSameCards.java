/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package charlie.card;

/**
 *
 * @author blossom
 */
public class ShoeSameCards extends Shoe{
    
    @Override
    public void init() {
        cards.clear();
        
        /*******************
        /* test layouts - we all get charlie dealer loses
        /*******************/
        // inital deal - ACES
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));

        
        // bot right 4 cards
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));

        
        // I split - 4 cards for hand L 4 cards for hand R
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        
        // bot left 4 cards
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));

        
        // 2's
        
        cards.add(new Card(2, Card.Suit.SPADES));
        cards.add(new Card(2, Card.Suit.SPADES));
        cards.add(new Card(2, Card.Suit.SPADES));
        cards.add(new Card(2, Card.Suit.SPADES));
        cards.add(new Card(2, Card.Suit.SPADES));
        cards.add(new Card(2, Card.Suit.SPADES));
        cards.add(new Card(2, Card.Suit.SPADES));
        cards.add(new Card(2, Card.Suit.SPADES));
        
        // bot right 4 cards
        cards.add(new Card(2, Card.Suit.SPADES));
        cards.add(new Card(2, Card.Suit.SPADES));
        cards.add(new Card(2, Card.Suit.SPADES));

        
        // I split - 4 cards for hand L 4 cards for hand R
        cards.add(new Card(2, Card.Suit.SPADES));
        cards.add(new Card(2, Card.Suit.SPADES));
        cards.add(new Card(2, Card.Suit.SPADES));
        cards.add(new Card(2, Card.Suit.SPADES));
        cards.add(new Card(2, Card.Suit.SPADES));
        cards.add(new Card(2, Card.Suit.SPADES));
        cards.add(new Card(2, Card.Suit.SPADES));
        cards.add(new Card(2, Card.Suit.SPADES));
        
        // bot left 4 cards
        cards.add(new Card(2, Card.Suit.SPADES));
        cards.add(new Card(2, Card.Suit.SPADES));
        cards.add(new Card(2, Card.Suit.SPADES));


        // 3's
        
        cards.add(new Card(3, Card.Suit.SPADES));
        cards.add(new Card(3, Card.Suit.SPADES));
        cards.add(new Card(3, Card.Suit.SPADES));
        cards.add(new Card(3, Card.Suit.SPADES));
        cards.add(new Card(3, Card.Suit.SPADES));
        cards.add(new Card(3, Card.Suit.SPADES));
        cards.add(new Card(3, Card.Suit.SPADES));
        cards.add(new Card(3, Card.Suit.SPADES));
        
        // bot right 4 cards
        cards.add(new Card(3, Card.Suit.SPADES));
        cards.add(new Card(3, Card.Suit.SPADES));
        cards.add(new Card(3, Card.Suit.SPADES));

        
        // I split - 4 cards for hand L 4 cards for hand R
        cards.add(new Card(3, Card.Suit.SPADES));
        cards.add(new Card(3, Card.Suit.SPADES));
        cards.add(new Card(3, Card.Suit.SPADES));
        cards.add(new Card(3, Card.Suit.SPADES));
        cards.add(new Card(3, Card.Suit.SPADES));
        cards.add(new Card(3, Card.Suit.SPADES));
        cards.add(new Card(3, Card.Suit.SPADES));
        cards.add(new Card(3, Card.Suit.SPADES));
        
        // bot left 4 cards
        cards.add(new Card(3, Card.Suit.SPADES));
        cards.add(new Card(3, Card.Suit.SPADES));
        cards.add(new Card(3, Card.Suit.SPADES));

        
        // 2's
        
        cards.add(new Card(4, Card.Suit.SPADES));
        cards.add(new Card(4, Card.Suit.SPADES));
        cards.add(new Card(4, Card.Suit.SPADES));
        cards.add(new Card(4, Card.Suit.SPADES));
        cards.add(new Card(4, Card.Suit.SPADES));
        cards.add(new Card(4, Card.Suit.SPADES));
        cards.add(new Card(4, Card.Suit.SPADES));
        cards.add(new Card(4, Card.Suit.SPADES));
        
        // bot right 4 cards
        cards.add(new Card(4, Card.Suit.SPADES));
        cards.add(new Card(4, Card.Suit.SPADES));
        cards.add(new Card(4, Card.Suit.SPADES));

        
        // I split - 4 cards for hand L 4 cards for hand R
        cards.add(new Card(4, Card.Suit.SPADES));
        cards.add(new Card(4, Card.Suit.SPADES));
        cards.add(new Card(4, Card.Suit.SPADES));
        cards.add(new Card(4, Card.Suit.SPADES));
        cards.add(new Card(4, Card.Suit.SPADES));
        cards.add(new Card(4, Card.Suit.SPADES));
        cards.add(new Card(4, Card.Suit.SPADES));
        cards.add(new Card(4, Card.Suit.SPADES));
        
        // bot left 4 cards
        cards.add(new Card(4, Card.Suit.SPADES));
        cards.add(new Card(4, Card.Suit.SPADES));
        cards.add(new Card(4, Card.Suit.SPADES));

        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        /***********************************************/
        /********* LOTS OF ACES ************************/
        /***********************************************/
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));        
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));        
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));          
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));

        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        
        cards.add(new Card(9, Card.Suit.HEARTS));
        //cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        
    }
    
    @Override
    public boolean shuffleNeeded() {
        return false;
    }
    
}
