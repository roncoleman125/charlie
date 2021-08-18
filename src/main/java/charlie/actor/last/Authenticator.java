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
package charlie.actor.last;

import charlie.message.view.from.Login;
import charlie.server.Ticket;
import charlie.util.Constant;
import java.util.Random;

/**
 *
 * @author Ron.Coleman
 */
public abstract class Authenticator extends Actor {
    private final static Random ran = new Random(0);
    protected Ticket ticket = null;

    /**
     * Constructor
     * Initializes actor with property "charlie.server.login" in charlie.props.
     */
    public Authenticator() {
        super(System.getProperty("charlie.server.login"),
              System.getProperty("charlie.server.login"));
    }
       
    /**
     * Validates a login
     * @param login Login credentials to authenticate
     * @return Ticket or null if login fails
     */
    protected Ticket validate(Login login) {
        if (login.getLogname() != null && login.getPassword() != null)
            return new Ticket(ran.nextLong(), Constant.PLAYER_BANKROLL);

        return null;
    }
}
