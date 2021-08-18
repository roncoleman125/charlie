package charlie.card;

/**
 *
 * @author blossom
 */
public class SplitShoe00 extends Shoe{
    
    @Override
    public void init() {
        cards.clear();
        
        // My first card
        cards.add(new Card(9, Card.Suit.HEARTS));
        
        // Dealers first card
        cards.add(new Card(2, Card.Suit.CLUBS));
        
        // My second card
        cards.add(new Card(9, Card.Suit.SPADES));
        
        // Dealers second card
        cards.add(new Card(10, Card.Suit.SPADES));
        
        // My first card for one of the split hands
        cards.add(new Card(10, Card.Suit.SPADES));
        
        // My first card for the second hand
        cards.add(new Card(9, Card.Suit.DIAMONDS));
        
//        // My first card second round
//        cards.add(new Card(Card.ACE, Card.Suit.HEARTS));

        // want the dealer to break ... still like the A above to test later
        cards.add(new Card(Card.KING, Card.Suit.HEARTS));
        
        // dealers first card second round
        cards.add(new Card(10, Card.Suit.CLUBS)); 
        
        // my second card second round
        cards.add(new Card(Card.ACE, Card.Suit.HEARTS));
        
        // Dealers second card
        cards.add(new Card(Card.JACK, Card.Suit.CLUBS));
        
        // My second card for split
        cards.add(new Card(Card.KING, Card.Suit.SPADES));
        
        // my other hands card
        cards.add(new Card(Card.ACE, Card.Suit.SPADES));
        
        /**
         * Just a bunch of cards for the shoe, a lot of 6's copy/paste.
         */
        cards.add(new Card(10, Card.Suit.SPADES));
        cards.add(new Card(4, Card.Suit.DIAMONDS));
        cards.add(new Card(6, Card.Suit.HEARTS));
        cards.add(new Card(4, Card.Suit.CLUBS)); 
        cards.add(new Card(8, Card.Suit.HEARTS));
        cards.add(new Card(10, Card.Suit.CLUBS));
        cards.add(new Card(6, Card.Suit.SPADES));
        cards.add(new Card(6, Card.Suit.SPADES));
        cards.add(new Card(10, Card.Suit.SPADES));
        cards.add(new Card(4, Card.Suit.DIAMONDS));
        cards.add(new Card(6, Card.Suit.HEARTS));
        cards.add(new Card(5, Card.Suit.CLUBS)); 
        cards.add(new Card(8, Card.Suit.HEARTS));
        cards.add(new Card(6, Card.Suit.CLUBS));
        cards.add(new Card(6, Card.Suit.SPADES));
        cards.add(new Card(6, Card.Suit.SPADES));
        cards.add(new Card(10, Card.Suit.SPADES));
        cards.add(new Card(4, Card.Suit.DIAMONDS));
        cards.add(new Card(6, Card.Suit.HEARTS));
        cards.add(new Card(5, Card.Suit.CLUBS)); 
    }
    
    @Override
    public boolean shuffleNeeded() {
        return false;
    }
    
}
