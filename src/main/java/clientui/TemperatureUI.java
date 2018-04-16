package clientui;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import client.TemperatureClient;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class TemperatureUI extends ClientUI {

    private JButton heat;
    private JButton cool;
    private JButton increase;
    private JButton decrease;
    private JButton confirm;
    private TemperatureClient parent;
    private static JTextField maxTempTF;

    public TemperatureUI(TemperatureClient temperatureClient) {
        super(temperatureClient);
        parent = temperatureClient;
        init();
    }

    @Override
    public void init() {
        super.init();
        heat = new JButton("Heat");
        cool = new JButton("Cool");
        increase = new JButton("Increase");
        decrease = new JButton("Decrease");
        confirm = new JButton("Confirm");
        confirm.setEnabled(false);
        scroll.setBounds(5, 40, UIConstants.COMPONENTWIDTH, 300);
        add(new JButton[]{heat});
        add(new JButton[]{cool});
        add(new JButton[]{increase});
        add(new JButton[]{decrease});
        JLabel label = new JLabel("Set Max: ");
        controls.add(label);
        maxTempTF = new javax.swing.JTextField();
        controls.add(maxTempTF);
        maxTempTF.setText("NULL");
        maxTempTF.setEnabled(false);
        add(new JButton[]{confirm});
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == heat) {
            parent.action("HEAT");
        }
        if (e.getSource() == cool) {
            parent.action("COOL");
        }
        if (e.getSource() == increase) {
            parent.action("INCREASE");
            confirm.setEnabled(true);
        }
        if (e.getSource() == decrease) {
            parent.action("DECREASE");
            confirm.setEnabled(true);
        }
        if (e.getSource() == confirm) {
            parent.action("CONFIRM");
            confirm.setEnabled(false);
        }
    }
    public static void setText(String i){
        maxTempTF.setText(i);//settitng the max temp
    }
}
