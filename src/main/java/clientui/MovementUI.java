package clientui;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import client.MovementClient;

public class MovementUI extends ClientUI {

    private JButton accelerate;
    public JButton slowDown;
    private JButton engineON;
    private JButton engineOFF;
    private final MovementClient parent;

    public MovementUI(MovementClient movementClient) {
        super(movementClient);
        parent = movementClient;
        init();
    }

    @Override
    public void init() {
        super.init();
        accelerate = new JButton("Accelerate");
        slowDown = new JButton("Slow Down");
        engineON = new JButton("EngineON");
        engineOFF = new JButton("EngineOFF");
        scroll.setBounds(5, 40, UIConstants.COMPONENTWIDTH, 300);
        add(new JButton[]{accelerate});
        add(new JButton[]{slowDown});
        add(new JButton[]{engineON});
        add(new JButton[]{engineOFF});
        engineOFF.setEnabled(false);
        accelerate.setEnabled(false);
        slowDown.setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == accelerate) {
            parent.action("ACCELERATE");
            slowDown.setEnabled(true);
        }
        if (e.getSource() == slowDown) {
            parent.action("SLOWDOWN");
            slowDown.setEnabled(false);
        }
        if (e.getSource() == engineON) {
            parent.action("ENGINEON");
            engineON.setEnabled(false);
            engineOFF.setEnabled(true);
            accelerate.setEnabled(true);
            slowDown.setEnabled(false);
        }

        if (e.getSource() == engineOFF) {
            parent.action("ENGINEOFF");
            engineOFF.setEnabled(false);
            engineON.setEnabled(true);
            accelerate.setEnabled(false);
            slowDown.setEnabled(false);
        }

    }

    public void setBtnDisabled() {
        slowDown.setEnabled(false);
    }
}
