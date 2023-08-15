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
package charlie;

import javax.swing.*;
import java.awt.*;

/**
 * This is an experimental game frame manually constructed rather than using the NB gui builder.
 * @author Ron.Coleman
 */
public class GameFrameX extends JFrame { //extends GameFrame {
    JPanel surface = new JPanel();

    final static int FRAME_WIDTH = 800;
    final static int FRAME_HEIGHT = 500;
    public GameFrameX() {
        surface.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        surface.setLayout(new BorderLayout());

        JPanel atable = new JPanel();
        atable.setSize(FRAME_WIDTH, FRAME_HEIGHT);

        surface.add(atable,BorderLayout.CENTER);
        surface.add(new ControlPanel(),BorderLayout.SOUTH);

//        add(new ATable(this,surface));
        add(surface);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null);
        setTitle("Count Basic");
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
        GameFrameX gf = new GameFrameX();
        gf.setVisible(true);
    }
}

class ControlPanel extends JPanel {
    public ControlPanel() {
        setLayout(new BorderLayout());
        add(new TogglePanel(),BorderLayout.WEST);
        add(new ButtonPanel(),BorderLayout.EAST);
    }
}

class ButtonPanel extends JPanel {
    public ButtonPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(new JButton("Login"));
        add(new JButton("Deal"));
        add(new JButton("Stay"));
        add(new JButton("Hit"));
        add(new JButton("DDown"));
        add(new JButton("Split"));
    }
}
class TogglePanel extends JPanel {
    public TogglePanel() {
        setLayout(new FlowLayout(FlowLayout.RIGHT));
        add(new JCheckBox("Sounds"));
        add(new JCheckBox("Advise"));
    }
}
