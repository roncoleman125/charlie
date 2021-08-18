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
package charlie.actor.last;

import charlie.message.Message;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.log4j.Logger;

/**
 * Models a simple actor with non-buffered, full duplex capability.
 * @author Ron.Coleman
 */
abstract public class Actor implements Runnable {
    private final Logger LOG = Logger.getLogger(Actor.class);
    
    /** Callback on message arrival */
    protected Listener listener;
    
    /** Server socket for receiving inbound messages */
    protected ServerSocket serverSocket;
    
    /** This host when actor runs */
    protected final String myHost;
    
    /** Remote host where message are sent to */
    protected String remoteHost;
    
    /**
     * Constructor for full-duplex actors
     * @param myHost My host in "address:port" form.
     * @param remoteHost Remote host in "address:port" form.
     */
    public Actor(String myHost, String remoteHost) {
        this.myHost = myHost;
        this.remoteHost = remoteHost;
    }
    
    /**
     * Constructor for half (i.e., receiving message) actors
     * @param myHost My host in "address:port" form.
     */
    public Actor(String myHost) {
        this(myHost,"");
    }
    
    /**
     * Starts actor running. Prior to calling this method, message send to the actor
     * result in "connection refused" exceptions.
     */
    public void start() {
        new Thread(this).start();
    }
    
    /**
     * Comes here when the actor begins.
     */
    @Override
    public void run() {
        receive();
    }
    
    /**
     * Sets the listener to enable message to be received.
     * @param listener Listener instance
     */
    public final void setListener(Listener listener) {
        this.listener = listener;
    }
    
    /**
     * Sets the remove host.
     * @param remoteHost Remove host
     */
    public final void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }
    
    public InetAddress getMyAddress() {
        try {
            String[] addr = myHost.split(":");
            
            return InetAddress.getByName(addr[0]);
        } catch (UnknownHostException ex) {
            error(ex+"");
        }
        return null;
    }
    
    /**
     * Receives a message which invokes the callback listener.
     */
    protected void receive() {
        int portno = Integer.parseInt(myHost.split(":")[1]);
        try {

            serverSocket = new ServerSocket(portno);
            
            while(true) {
                info("waiting for connection on port "+portno);
                try (Socket clientSocket = serverSocket.accept()) {
                    info("accepted connection on port "+portno);
                    
                    InputStream is = clientSocket.getInputStream();
                    
                    ObjectInputStream ois = new ObjectInputStream(is);
                    
                    Message msg = (Message) ois.readObject();
                    info("received message "+msg.getClass().getSimpleName());
                    
                    if(listener != null) {
                        info("invoking listener for "+msg.getClass().getSimpleName());
                        listener.received(msg);
                    }
                    else
                        error("dropped "+msg.getClass().getSimpleName());
                    
                    clientSocket.close();
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            error(ex+"");
        } 
    }
    
    /**
     * Sends a message synchronously.
     * @param msg Message to transmit
     */
    public void send(Message msg) {
        try {
            info("sending "+msg.getClass().getSimpleName()+" to "+remoteHost);
            String[] params = remoteHost.split(":");

            String addr = params[0];
            int outPort = Integer.parseInt(params[1]);
            
            try (Socket socket = new Socket(addr, outPort)) {
                OutputStream os = socket.getOutputStream();
                
                ObjectOutputStream oos = new ObjectOutputStream(os);
                oos.writeObject(msg);
                
                oos.flush();
            }
            
            info("sent successfully "+msg.getClass().getSimpleName()+" to "+remoteHost);
        } catch (IOException ex) {
            error(ex+"");
        }
    }
    
    /**
     * Logs diagnostics conveniently.
     * @param text Text of message.
     */
    protected void info(String text) {
        LOG.info(this.getClass().getSimpleName()+" "+text);
    }
    
    protected void error(String text) {
        LOG.error(this.getClass().getSimpleName()+" "+text);
    }
}
