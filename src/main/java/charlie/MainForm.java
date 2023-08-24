package charlie;

import charlie.actor.Courier;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.plugin.IAdvisor;
import charlie.view.ATable;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainForm {
    protected static Logger LOG = null;
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




    private JPanel panel1;
    private JCheckBox soundsCheckBox;
    private JCheckBox adviseCheckBox;
    private JButton stayButton;
    private JButton dealButton;
    private JButton loginButton;
    private JButton hitButton;
    private JButton splitButton;
public MainForm() {
    loginButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    });

    dealButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    });

    stayButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    });

    hitButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    });

    splitButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    });
}
}
