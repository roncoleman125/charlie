/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package charlie.card;

/**
 *
 * @author Ron.Coleman
 */
public class BlackjackSplitShoe01 extends Shoe {
    @Override
    public void init() {
        cards.clear();
        //        cards.clear();
//        cards.add(new Card(Card.QUEEN, Card.Suit.HEARTS));
//        cards.add(new Card(6, Card.Suit.CLUBS));
//        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
//        cards.add(new Card(3, Card.Suit.SPADES));
//        cards.add(new Card(2, Card.Suit.SPADES));
//        cards.add(new Card(4, Card.Suit.DIAMONDS));
//        cards.add(new Card(6, Card.Suit.HEARTS));
//        cards.add(new Card(5, Card.Suit.CLUBS)); 

        // YOU
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        
        // DEALER
        cards.add(new Card(Card.QUEEN, Card.Suit.HEARTS));
        
        // YOU
        cards.add(new Card(Card.ACE, Card.Suit.HEARTS));
        
        
        // DEALER
        cards.add(new Card(6, Card.Suit.CLUBS));
        
        // YOU split here
        
        // Basic strategy says A+8; STAY
        cards.add(new Card(8, Card.Suit.DIAMONDS));
        
        // Should NOT be a natural blackjack but moves to next hand
        cards.add(new Card(Card.QUEEN, Card.Suit.HEARTS));

        
        // Rest of game
        cards.add(new Card(Card.QUEEN, Card.Suit.HEARTS));
        cards.add(new Card(6, Card.Suit.CLUBS));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(3, Card.Suit.SPADES));
        cards.add(new Card(2, Card.Suit.SPADES));
        cards.add(new Card(4, Card.Suit.DIAMONDS));
        cards.add(new Card(6, Card.Suit.HEARTS));
        cards.add(new Card(5, Card.Suit.CLUBS)); 
        
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
    }
}
