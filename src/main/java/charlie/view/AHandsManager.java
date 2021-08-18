/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package charlie.view;

import charlie.util.Point;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;


/**
 * Manages one or more hands (in case of splits).
 * @author Ron Coleman
 */
public class AHandsManager { 
    private final List<AHand> hands = new ArrayList<>();
    private final int handInset = ACard.cardWidth;
    
    private final Point center;
    
    private final String name;

    /**
     * Constructor
     * @param name Name of the hand.
     * @param center Center point
     */
    public AHandsManager(String name, Point center) {
        this.name = name;
        this.center = center;
    }
    
    /**
     * Returns true if the hand is ready
     * @return True if ready, false othrwise
     */
    public boolean isReady() {
        for(int i=0; i < hands.size(); i++) {
            AHand ahand = hands.get(i);
            
            if(!ahand.isReady())
                return false;
        }
        
        return true; 
    }
    
    /**
     * Updates all my hands.
     */
    public void update() {
        for(int i=0; i < hands.size(); i++) {
            AHand ahand = hands.get(i);
            ahand.update();
        }
    }
    
    /**
     * Renders all my hands.
     * @param g Graphics context
     */
    public void render(Graphics2D g) {
        for(int i=0; i < hands.size(); i++) {
            AHand ahand = hands.get(i);
            ahand.render(g);
        }
//        for(AHand hand: this)
//            hand.render(g);

    }
    
    /**
     * Adds a hand
     * @param hand Hand
     * @return True if the hand was successfully added.
     */
    public boolean add(AHand hand) {
        // Add this new hand
        boolean tf = hands.add(hand);
        
        hand.setName(name);
        
        // If this is the only hand, then its home is the player center
        // otherwise we have to spread the hands.
        if(hands.size() == 1)
            hand.setHome(center);
        else
            spread();
        
        return tf;
    }
    
//    public ACard split(int k) {
//        if(k < 0 || k >= hands.size())
//            return null;
//        
//        // Split the hand
//        AHand hand1 = hands.get(k);
//        
//        ACard card = hand1.split();
//        
//        // Create new hand with the card and hit the hand to the animator.
//        AHand hand2 = new AHand(card);       
//        this.hit(hand2);
//        
//        // Spread out the hands
//        spread();
//        
//        return card;
//    }
    
    /**
     * Spreads the hands.
     */
    protected void spread() { 
        // Select the offset relative to the current unsplit hand.
        // Note: to keep the cards evenly laid out along the player's center
        // the offset size depends on the rank of hands.
        int offset = hands.size() % 2 == 0 ? ACard.cardWidth : ACard.cardWidth / 2;
        
        // Move all the hands further to the left
        int x = hands.get(0).getHome().getX() - offset;
        int y = hands.get(0).getHome().getY();
        
        for(AHand hand: hands) {
            Point newhome = new Point(x,y);
            
            hand.setHome(newhome);
            
            x += (ACard.cardWidth + handInset + 70);
        }
    }
    
    /**
     * Wipes the hands we're managing.
     */
    public void clear() {
        hands.clear();
    }
    
    /**
     * Settles all the cards.
     */
    public void settle() {
        for(AHand hand: hands)
            hand.settle();
    }
}
