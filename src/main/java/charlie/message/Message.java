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
package charlie.message;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * This class implements an abstract message for actors.
 * @author Ron Coleman
 */
abstract public class Message implements Serializable {
    protected Long serialno = 0L;
    protected static Long counter = 0L;
    protected final static String monitor = "YUMMY";
    protected InetAddress source;
    protected final Long stamp = System.currentTimeMillis();;

    /**
     * Constructor
     */
    public Message() {
        this.source = null;
        
        synchronized(monitor) {
            serialno = counter;
            counter += 1;
        }        
    }
    
    /**
     * Constructor
     * @param source Actor source address.
     */
    public Message(InetAddress source) {
        this.source = source;
        
        synchronized(monitor) {
            serialno = counter;
            counter += 1;
        }
    }

    /**
     * Gets the message serial number.
     * @return Serial number
     */
    public Long getSerialno() {
        return serialno;
    }

    /**
     * Sets the message serial number.
     * @param serialno Serial number
     */
    public void setSerialno(Long serialno) {
        this.serialno = serialno;
    }

    /**
     * Gets the message source address.
     * @return Address
     */
    public InetAddress getSource() {
        return source;
    }
    
    /**
     * Sets the message source address.
     * @param source Address
     */
    public void setSource(InetAddress source) {
        this.source = source;
    }

    /**
     * Gets the message time stamp.
     * @return Time stamp
     */
    public Long getStamp() {
        return stamp;
    }
    
    /**
     * Gets string version of this message.
     * @return String
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
    
}
