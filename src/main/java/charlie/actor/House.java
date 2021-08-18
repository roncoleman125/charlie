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
package charlie.actor;

import charlie.actor.last.Listener;
import charlie.actor.last.Actor;
import charlie.card.Hid;
import charlie.dealer.Dealer;
import charlie.message.Message;
import charlie.plugin.IPlayer;
import charlie.message.view.from.Arrival;
import charlie.server.GameServer;
import charlie.server.Ticket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * This class implements the house.
 * @author Ron Coleman
 */
public class House extends Actor implements Listener {
    private final Logger LOG = Logger.getLogger(House.class);
    private final String PLAYER_ACTOR = "PLAYER-";
    protected List<RealPlayer> players = new ArrayList<>();
    private Integer nextPlayerId = 0;
    private final GameServer server;
    protected HashMap<IPlayer,Ticket> accounts = new HashMap<>();

    /**
     * Constructor
     * @param server Game server
     */
    public House(GameServer server) { 
        super(System.getProperty("charlie.server.house"));
        this.server = server;
    }
    
    /**
     * House does not send messages but delegates it to RealPlayer.
     * @param msg Message
     */
    @Override
    public void send(Message msg) {
        error("bad message "+msg+" not sent");
    }
    
    /**
     * Receives messages.
     * @param msg Message
     */
    @Override
    public void received(Message msg) {
        if(!(msg instanceof Arrival)) {
            error("bad received message "+msg+" dropped.");
            return;
        }
        
        onReceive((Arrival)msg);
    }
    
    /**
     * Receives an arrival by a client.
     * At login the user gets a ticket from the server which the
     * house uses to validate. If the ticket is valid, the house
     * allocates a dealer and spawns a real player to service the interface
     * between the dealer and a "real" player. The dealer then waits for contact
     * via a bet message through real player.
     * @param arrival Arrival message
     */
    public void onReceive(Arrival arrival) {
        Ticket ticket = arrival.getTicket();

        if (!valid(ticket)) {
            LOG.error("invalid ticket = " + ticket);
            return;
        }

        LOG.info("validated ticket = " + ticket);
        
        // Build address to courier to which real player is connected.
        InetAddress addr = arrival.getSource();
        LOG.info("arrival from " + addr);

        String courier = addr.getHostAddress() + ":" + arrival.getPort();
        
        // Get a dealer for this player
        // Note: if we were allocating dealers from a pool, this is the place
        // to implement that logic. For now we'll just spawn dealers without
        // restriction.
        Dealer dealer = new Dealer(this);

        // Spawn a "real player" in server       
        RealPlayer player = new RealPlayer(dealer, courier);
        player.setListener(player);
        
        player.start();

        // Put this player in the repository of player accounts.
        accounts.put(player, ticket);

        synchronized (this) {
            nextPlayerId++;

            players.add(player);
        }

        // Inform courier that login is complete and we're ready to play
        player.ready();
    }
    
    /**
     * Validates a ticket.
     * @param ticket Ticket
     * @return True if the ticket is valid, false otherwise.
     */
    protected boolean valid(Ticket ticket) {
        List<Ticket> logins = server.getTickets();
        
        for(int i=0; i < logins.size(); i++) {
            if(logins.get(i).equals(ticket))
                return true;
        }
        
        return false;
    }
    /**
     * Updates the bankroll.
     * @param hid Hand
     * @param gain P&L
     * @param player the player
     */
    public void updateBankroll(IPlayer player,Hid hid,Double gain) {      
        if(player == null || !accounts.containsKey(player))
            return;
        
        Ticket ticket = accounts.get(player);
        
        Double bankroll = ticket.getBankroll() + gain * hid.getAmt() + hid.getSideAmt();
        
        ticket.setBankroll(bankroll);
    }
    
    /**
     * Gets the bankroll for a myAddress.
     * @param player the player
     * @return Dollar amount of bankroll
     */
    public Double getBankroll(IPlayer player) {
        if(player == null || !accounts.containsKey(player))
            return 0.0;
        
        return accounts.get(player).getBankroll();
    }
}
