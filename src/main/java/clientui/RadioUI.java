package clientui;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import client.RadioClient;

public class RadioUI extends ClientUI {

    private JButton radioON;
    private JButton radioOFF;
    private JButton next;
    private JButton previous;
    private final RadioClient parent;

    public RadioUI(RadioClient radioClient) {
        super(radioClient);
        parent = radioClient;
        init();
    }

    @Override
    public void init() {
        super.init();
        radioON = new JButton("Radio ON");
        radioOFF = new JButton("Radio OFF");
        next = new JButton("Next Station");
        previous = new JButton("Previous Station");
        scroll.setBounds(5, 40, UIConstants.COMPONENTWIDTH, 300);
        add(new JButton[]{radioON});
        add(new JButton[]{radioOFF});
        add(new JButton[]{next});
        add(new JButton[]{previous});
        radioOFF.setEnabled(false);
        next.setEnabled(false);
        previous.setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == radioON) {
            parent.action("RADIOON");
            radioON.setEnabled(false);
            radioOFF.setEnabled(true);
            next.setEnabled(true);
            previous.setEnabled(true);
        }
        if (e.getSource() == radioOFF) {
            parent.action("RADIOOFF");
            radioON.setEnabled(true);
            radioOFF.setEnabled(false);
            next.setEnabled(false);
            previous.setEnabled(false);
        }
        if (e.getSource() == next) {
            parent.action("NEXT");
        }
        if (e.getSource() == previous) {
            parent.action("PREVIOUS");
        }
    }
    
}
