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
import charlie.message.view.from.Login;
import charlie.server.Ticket;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.apache.log4j.Logger;

/**
 * This class implements the client-side login.
 * @author Ron.Coleman
 */
public class ClientAuthenticator extends Authenticator {    
    protected final Logger LOG = Logger.getLogger(ClientAuthenticator.class);
    
    public Ticket send(String logname, String password) {
        return send(new Login(logname, password));
    }
    
    /**
     * Sends a login message to the server.
     * @param login Login
     * @return Ticket if login successful, null otherwise
     */
    public Ticket send(Login login) {
        try {
            // Login to the server
            String[] params = remoteHost.split(":");
            String loginAddr = params[0];
            int loginPort = Integer.parseInt(params[1]);

            Socket client = new Socket(loginAddr, loginPort);

            OutputStream os = client.getOutputStream();

            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(login);
            oos.flush();
            LOG.info("sent login request");

            InputStream is = client.getInputStream();

            ObjectInputStream ois = new ObjectInputStream(is);

            this.ticket = (Ticket) ois.readObject();
            LOG.info("received ticket "+ticket);

            return ticket;

        } catch (IOException | ClassNotFoundException e) {
            LOG.info("failed to connect to server: " + e);

            return null;
        }
    }
}
