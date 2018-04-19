package clientui;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import client.MovementClient;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JLabel;

public class MovementUI extends ClientUI {

    private JButton accelerate;
    public JButton slowDown;
    private JButton engineON;
    private JButton engineOFF;
   // private JButton setSpeed;
    private final MovementClient parent;
    private static JComboBox jComboBox1;

 Font trb = new Font("TimesRoman", Font.ITALIC, 16);
    public MovementUI(MovementClient movementClient) {
        super(movementClient);
        parent = movementClient;
        init();
    }

    @Override
    public void init() {
        super.init();
        jComboBox1 = new JComboBox();
        accelerate = new JButton("Accelerate");
        slowDown = new JButton("Slow Down");
        engineON = new JButton("EngineON");
        engineOFF = new JButton("EngineOFF");
       // setSpeed = new JButton("Set Speed");
        scroll.setBounds(10, 40, UIConstants.COMPONENTWIDTH, 300);
        jComboBox1.addItem("Town/City");
        jComboBox1.addItem("Regional/local");
        jComboBox1.addItem("National");
        jComboBox1.addItem("Motorway");
        //  jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Town/city", "Regional/local", "National", "Motorways" }));
        jComboBox1.setSelectedItem(null);
        add(new JButton[]{accelerate});
        add(new JButton[]{slowDown});
        add(new JButton[]{engineON});
        add(new JButton[]{engineOFF});
      //  add(new JButton[]{setSpeed});
        JLabel label = new JLabel("Choose Road: ");
        label.setFont(trb);
        label.setPreferredSize(new Dimension(120,30));

        controls.add(label);
        controls.add(jComboBox1);
        engineOFF.setEnabled(false);
        accelerate.setEnabled(false);
        slowDown.setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == accelerate) {
            slowDown.setEnabled(true);
            String value = (String)jComboBox1.getSelectedItem();
            System.out.println("combobox value " + value);

            if (value.equals("Town/City")) {
                parent.action("TOWN");
                System.out.println("in town option ");

            }
            if (value.equals("Regional/local")) {
                parent.action("REGIONAL");
                System.out.println("in region option ");
            }
            if (value.equals("National")) {
                parent.action("NATIONAL");
                System.out.println("in national option ");

            }
            if (value.equals("Motorway")) {
                parent.action("MOTORWAY");
                System.out.println("in motorway option ");

            }
            
            parent.action("ACCELERATE");

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
      /*  if (e.getSource() == setSpeed) {
            String value = (String)jComboBox1.getSelectedItem();
            System.out.println("combobox value " + value);

            if (value.equals("Town/City")) {
                parent.action("TOWN");
                System.out.println("in town option ");

            }
            if (value.equals("Regional/local")) {
                parent.action("REGIONAL");
                System.out.println("in region option ");
            }
            if (value.equals("National")) {
                parent.action("NATIONAL");
                System.out.println("in national option ");

            }
            if (value.equals("Motorway")) {
                parent.action("MOTORWAY");
                System.out.println("in motorway option ");

            }

        }*/
    }

    public void setBtnDisabled() {
        slowDown.setEnabled(false);
    }
}
