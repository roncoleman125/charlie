package charlie.card;

import charlie.card.Card.Suit;
import java.util.Random;
import org.apache.log4j.Logger;

/**
 * This class implements a test of splits by "forcing" player to receive
 * a "random-ish" pair every 5th hand/game.
 * @author blossom
 */
public class SplitShoe01 extends Shoe{
    
    private final Logger LOG = Logger.getLogger(SplitShoe01.class);
    
    public static int gameNumber = 0;
    private int cardNumber = 1;
    
    public SplitShoe01(){
        // Not liking hard value, but just for testing...
        super(6);
    }
    
    @Override
    public void init(){
        super.load();
        super.shuffle();
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
        
        LOG.info("A card was requested for game number: " + gameNumber);
        
        if(gameNumber % 5 == 0){
            
            LOG.info("We are dealing pair to player! "
                     + "This is card: "
                     + cardNumber);
            
            Card card = dealSplitHand(cardNumber++);
            
            LOG.info("We just received card: " 
                    + card 
                    + " as part of the forced pair!");
            
            return card;
        }
        
        return cards.get(index++);
    }

    private Card dealSplitHand(int cardNumber) {
        
        int randomSplitCard = 0;
        
        switch (cardNumber){

            case 1:
                Random randomCard = new Random();
                randomSplitCard = randomCard.nextInt(13) + 1;
                
                // I don't like passing 'hard' values for ace/face cards
                // again a test deck
                Card myFirstCard = new Card(randomSplitCard, Suit.DIAMONDS);
                return myFirstCard;
            
            case 2:
                return cards.get(index++);
                
            case 3:
                Card mySecondCard = new Card(randomSplitCard, Suit.CLUBS);
                return mySecondCard;
                
            case 4:
                gameNumber++;
                return cards.get(index++);
            
            default:
                return cards.get(index++);
        }
    }   
}