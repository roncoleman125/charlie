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

import charlie.actor.last.Authenticator;
import charlie.message.Message;
import charlie.message.view.from.Login;
import charlie.server.GameServer;
import static charlie.util.Helper.sleep;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.Logger;

/**
 *
 * @author Ron.Coleman
 */
public class ServerAuthenticator extends Authenticator {
    protected final Logger LOG = Logger.getLogger(ServerAuthenticator.class);
    
    private final GameServer server;
    
    public ServerAuthenticator(GameServer server) {
        this.server = server;
    }
    
    /**
     * Blocks message from being sent as the server side only receives messages.
     * @param msg 
     */
    @Override
    public void send(Message msg) {
        error("bad operation "+msg+" not sent");
    }
    
    /**
     * 
     */
    @Override
    public void receive() {
        try {           
            int loginPort = Integer.parseInt(myHost.split(":")[1]);
            
            serverSocket = new ServerSocket(loginPort);
            
            Socket clientSocket = serverSocket.accept();
            
            InputStream is = clientSocket.getInputStream();
            
            ObjectInputStream ois = new ObjectInputStream(is);
            
            Login login = (Login) ois.readObject();
            LOG.info("got login");
            
            this.ticket = validate(login);
            
            if (ticket != null) {               
                LOG.info("validated ticket " + ticket + " login successful!");
                
                server.getTickets().add(ticket);
                
                OutputStream os = clientSocket.getOutputStream();
                
                ObjectOutputStream oos = new ObjectOutputStream(os);
                
                oos.writeObject(ticket);
                
                oos.flush();
                info("sent ticket to client");
                
                clientSocket.close();
                serverSocket.close();
                
                sleep(250);                
            }
        } catch (IOException | ClassNotFoundException ex) {
            error("exception caught "+ex);
        }
    }
}
