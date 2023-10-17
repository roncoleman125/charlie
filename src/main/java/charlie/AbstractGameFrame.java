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
package charlie;

import charlie.actor.Arriver;
import charlie.actor.ClientAuthenticator;
import charlie.actor.Courier;
import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.plugin.IAdvisor;
import charlie.server.Ticket;
import charlie.util.Constant;
import charlie.util.Play;
import charlie.view.ATable;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class is used by the two frames, one build by NB and the other, IntelliJ.
 * @author Ron.Coleman
 */
public abstract class AbstractGameFrame extends javax.swing.JFrame {
    protected static Logger LOG = null;//Logger.getLogger(AbstractGameFrame.class);;
    protected final Integer MY_PORT = 2345;
    protected Courier courier;
    protected ATable table;
    protected boolean connected = false;
    protected final String COURIER_ACTOR = "COURIER";
    protected final String SOUND_EFFECTS_PROPERTY = "charlie.sounds.enabled";
    protected final List<Hid> hids = new ArrayList<>();
    protected final HashMap<Hid, Hand> hands = new HashMap<>();
    protected int handIndex = 0;
    protected boolean trucking = true;
    protected boolean dubblable;
    protected IAdvisor advisor;
    protected Hand dealerHand;
    //    private Properties props;
    protected boolean manuallyControlled = true;

    /**
     * Is a hand "split-able
     * @author Dan Blossom
     */
    protected boolean splittable = false;
    public abstract void enableDeal(boolean state);

    public abstract void enablePlay(boolean state);

    public abstract void split(Hid newHid, Hid origHid);

    public abstract void updateHandIndex();

    public abstract void setDubblable(boolean state);

    public abstract void deal(Hid hid, Card card, int[] handValues);

    public abstract void enableSplitButton(Hid hid);

    protected abstract boolean isAdvisingConfirmed(Hid hid, Play play);

    protected abstract void loadConfig();

    /**
     * Connects to server
     * @param panel Panel courier perceives.
     * @return True if connected, false if connect attempt fails.
     */
    protected boolean connect(ATable panel) {
        Ticket ticket = new ClientAuthenticator().send("abc", "def");

        if(ticket == null)
            return false;

        LOG.info("login successful");

        // Start courier to receive messages from dealer --
        // NOTE: we must start courier before sending arrival message otherwise
        // ready message will come before any actor can receive it.
        courier = new Courier(panel);
        courier.start();

        // Let house know we've arrived then wait for READY to begin playing
        new Arriver(ticket).send();

        synchronized (panel) {
            try {
                panel.wait(5000);

                Double bankroll = ticket.getBankroll();

                panel.setBankroll(bankroll);

                LOG.info("connected to courier with bankroll = " + bankroll);

            } catch (InterruptedException ex) {
                LOG.info("failed to connect to server: " + ex);

                failOver();

                return false;
            }
        }
        return true;
    }

    protected void loadAdvisor() {
        try {
            String className = System.getProperty(Constant.PLUGIN_ADVISOR);

            if (className == null)
                return;

            LOG.info("advisor plugin detected: "+className);
            Class<?> clazz = Class.forName(className);

            this.advisor = (IAdvisor) clazz.getDeclaredConstructor().newInstance();

            LOG.info("loaded advisor successfully");
        } catch (ClassNotFoundException |
                 InstantiationException |
                 IllegalAccessException |
                 NoSuchMethodException |
                 SecurityException |
                 IllegalArgumentException |
                 InvocationTargetException ex) {
            LOG.error(ex.toString());
        }
    }

    /**
     * Recovers in event we fail catastrophically.
     */
    protected void failOver() {

    }
}
