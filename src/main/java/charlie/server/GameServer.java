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
package charlie.server;

import charlie.actor.House;
import charlie.actor.ServerAuthenticator;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import org.apache.log4j.Logger;


/**
 * This class implements the game server.
 * @author Ron Coleman
 */
public class GameServer {
    static {
        // For properties see http://www.slf4j.org/api/org/slf4j/impl/SimpleLogger.html
        System.getProperties().setProperty("LOGFILE","log-server.out");
    }
    protected static  Logger LOG = Logger.getLogger(GameServer.class);
    protected final static String HOUSE_ACTOR = "HOUSE";
    protected final static Random ran = new Random(0);
    protected final static Integer TOPOLOGY_PORT = 1234;
    protected final static String HOST = "127.0.0.1";
    protected final List<Ticket> tickets = new ArrayList<>();
    
    /**
     * This method is the main entry point for the server.
     * @param args Command line arguments (currently not used)
     */
    public static void main(String[] args) {
        new GameServer().go();
    }
    
    protected void go() {
        try {
            LOG.info("game server started");
            
            // Start the actor server
            Properties props = System.getProperties();
            
            props.load(new FileInputStream("charlie.props"));

            // Spawn the house
            House house = new House(this);
            house.setListener(house);
            house.start();
            LOG.info("house started");    
  
            // Enter the authentication loop
            while(true) {
                // If the authentication succeeds, there'll be an extra ticket in repository
                int sz = tickets.size();
                
                new ServerAuthenticator(this).receive();
                
                if(tickets.size() == sz)
                    LOG.error("client authentication failed");
            }
        } catch (IOException | NumberFormatException ex) {
            LOG.error("exception thrown: "+ex);
        }        
    }

    /**
     * Gets the logins by ticket
     * @return Tickets
     */
    public List<Ticket> getTickets() {
        return tickets;
    }
}
