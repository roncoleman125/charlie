/*
 Copyright (c) Ron Coleman

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

import charlie.actor.last.Actor;
import charlie.message.view.from.Arrival;
import charlie.server.Ticket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Implements actor to send arrival message to house.
 * @author Ron.Coleman
 */
public class Arriver extends Actor {

    private final Ticket ticket;
    
    /**
     * Constructor
     * Initializes actor with remote host with "charlie.server.house" property
     * in charlie.props.
     * @param ticket Ticket to conduct business
     */
    public Arriver(Ticket ticket) {
        super("",System.getProperty("charlie.server.house"));
        
        this.ticket = ticket;
    }
    
    public void send() {
        try {
            int courierPort = Integer.parseInt(System.getProperty("charlie.client.courier").split(":")[1]);
            
            super.send(new Arrival(ticket,InetAddress.getLocalHost(),courierPort));
            info("sent arrival message");
            
        } catch (UnknownHostException ex) {
            error("got exception "+ex);
        }
    }
    
    /**
     * Blocks receives.
     */
    @Override
    public void receive() {
        
    }
    
    /**
     * Blocks start to receive.
     */
    @Override
    public void start() {
        
    }
}
