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
package charlie.view;

import charlie.card.Hand;
import charlie.card.Hid;
import static charlie.view.AHand.HOME_OFFSET_X;
import charlie.util.Point;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

/**
 * Dealer hand only uses the soft value.
 * @author Ron Coleman
 */
public class ADealerHand extends AHand {
    protected Color bjFgColor = Color.BLACK;
    protected Color bjBgColor = new Color(116,255,4);
    protected Color bustFgColor = Color.WHITE;
    protected Color bustBgColor = new Color(250,58,5);    
    /**
     * Constructor
     * @param hid Hand hid
     */
    public ADealerHand(Hid hid) {
        super(hid);
    }
    
    /**
     * Hits hand with a card.
     * @param card Card
     */
    @Override
    public void hit(ACard card) {
        // Move other cards to left to make room for new card
        // The idea is to move the cards such that the original 
        // center remains the same regards of how many cards
        // are in the hand with a little space between the cards.
        int cardWidth = ACard.getCardWidth();

        int sz = cards.size();

        for (int i = 0; i < sz; i++) {
            ACard acard = cards.get(i);

            Point ahome = acard.getHome();

            int x = ahome.getX() - (cardWidth + HOME_OFFSET_X ) / 2;
            int y = ahome.getY();

            Point newHome = new Point(x, y);

            acard.setHome(newHome);
        }

        // Put in the new card in the space we just created for it.
        int x;

        if (sz == 0)
            x = home.getX();
        else {
            int xLastCard = cards.get(sz - 1).getHome().getX();

            x = xLastCard + cardWidth + HOME_OFFSET_X / 2;
        }

        Point sweetHome = new Point(x, home.getY());

        cards.add(new ACard(card, sweetHome));
    }
    
    /**
     * Renders the hand state (i.e., the value and its outcomeText in case of Blackjack).
     * @param g Graphics context
     * @param text Message
     */
    @Override
    protected void renderState(Graphics2D g,String text) {
        if(cards.isEmpty())
            return;

        FontMetrics fm = g.getFontMetrics(outcomeFont);
        
        int textWidth = fm.charsWidth(text.toCharArray(), 0, text.length());
        
        int x = cards.get(0).getX() + getPileWidth() / 2 - textWidth / 2;
        int y = ACard.getCardHeight() + fm.getHeight();
        
        g.setColor(stateColor);
        g.setFont(stateFont);

        g.drawString(text, x, y); 
        
        int value = Hand.getValue(values);
        if(cards.isEmpty() || value < 21)
            return;
       
        String outcomeText = ""; 
        if(isBlackjack())
            outcomeText += " BLACKJACK ! ";
        else if(isBroke())
            outcomeText += " BUST ! ";
        
        int sz = cards.size();
        
        ACard lastCard = cards.get(sz-1);
        x = cards.get(0).getX() + getPileWidth() - 15;
        y = lastCard.getY() + ACard.getCardHeight() / 2;
        
        int w = fm.charsWidth(outcomeText.toCharArray(), 0, outcomeText.length());
        int h = fm.getHeight(); 
                
        if(isBlackjack())
            g.setColor(bjBgColor);
        else if(isBroke())
            g.setColor(bustBgColor);
        
        g.fillRoundRect(x, y-h+5, w, h, 5, 5);
        
        if(isBlackjack())
            g.setColor(bjFgColor);
        else if(isBroke())
            g.setColor(bustFgColor);
        
        g.setFont(outcomeFont);
        g.drawString(outcomeText, x, y);
    }
    
    @Override
    public Integer getPileWidth() {
        if (cards.isEmpty()) {
            return 0;
        }

        int sz = cards.size();
        
        int cardWidth = ACard.getCardWidth();
        
        int pw = cardWidth * sz + (sz - 1) * HOME_OFFSET_X / 2;
        
        return pw;
    }
    
    @Override
    public Integer getPileHeight() {
        if (cards.isEmpty()) {
            return 0;
        }

        return ACard.getCardHeight();
    }
    
    @Override
    protected String getText() {
//        int value = values[Constant.HAND_SOFT_VALUE] <= 21 ?
//                values[Constant.HAND_SOFT_VALUE] :
//                values[Constant.HAND_LITERAL_VALUE];
        int value = Hand.getValue(values);
        
        String text = name + ": " + value;

//        if(value != 0) {
//            if(cards.size() == 2 && value == 21)
//                text += ": Blackjack !";
//            else
//                text += ": "+value;
//        }
        
        return text;
    }
    
    protected boolean isBlackjack() {
        return Hand.getValue(values) == 21 && cards.size() == 2;
    }
}
