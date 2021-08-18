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

import charlie.actor.Courier;
import charlie.view.AMoneyManager;
import java.awt.Graphics2D;

/**
 * This interface defines the behavior of an AI player running on the client.
 * @author Ron Coleman
 */
public interface ILogan extends IPlayer {
    /**
     * Tells bot it's time to make a bet to start a game.
     */
    public void go( );
    
    /**
     * Sets the courier actor through which we communicate with the controller.
     * @param courier Courier
     */
    public void setCourier(Courier courier);
    
    /**
     * Sets the money manager for managing bets.
     * @param moneyManager Money manager
     */
    public void setMoneyManager(AMoneyManager moneyManager);
    
    /**
     * Updates the bot.
     */
    public void update();
    
    /**
     * Renders the bot.
     * @param g Graphics context.
     */
    public void render(Graphics2D g);
}
