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

import charlie.actor.Courier;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.plugin.IAdvisor;
import charlie.util.Constant;
import charlie.view.ATable;
import org.apache.log4j.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainForm extends JFrame {
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

    private JPanel topmostPanel;
    private JCheckBox soundsCheckBox;
    private JCheckBox adviseCheckBox;
    private JButton stayButton;
    private JButton dealButton;
    private JButton loginButton;
    private JButton hitButton;
    private JButton splitButton;
    private JPanel surface;

    public MainForm() {
        // Topmost is where the GUI builder begins...but loading of the form is unclear.
        setContentPane(topmostPanel);

        // For now, we're using a stub for the ATable
        topmostPanel.add(new ATablePlaceholder());

        setTitle("Charlie 3.1");

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // These values just...work based on the NB gui builder
        setSize(695,500);

        // Makes the frame appear (typically) in the middle of the desktop
        setLocationRelativeTo(null);

        setVisible(true);

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

    public static void main(String[] main) {
        // Set the L&F which must be done before instantiating any components.
        try {
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Launches the GUI but...I don't understand how it finds the form since it's not referenced
        // in the code anywhere.
        new MainForm();
    }
}

class ATablePlaceholder extends JPanel {
    protected Image instrImg;
    protected Image shoeImg;
    protected Image trayImg;
    protected static Color COLOR_FELT = new Color(0, 153, 100);

    public ATablePlaceholder() {
        setBackground(COLOR_FELT);
        this.instrImg = new ImageIcon(Constant.DIR_IMGS + "dealer-stands-0.png").getImage();
        this.shoeImg = new ImageIcon(Constant.DIR_IMGS + "shoe-0.png").getImage();
        this.trayImg = new ImageIcon(Constant.DIR_IMGS + "tray-0.png").getImage();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(this.instrImg, 140, 208, this);
        g2d.drawImage(this.shoeImg, 540, 5, this);
        g2d.drawImage(this.trayImg, 430, 5, this);
    }
}
