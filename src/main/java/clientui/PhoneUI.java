package clientui;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import client.PhoneClient;

public class PhoneUI extends ClientUI {

    private JButton Connect;
    private JButton Disconnect;
    private JButton BluetoothON;
    private JButton BluetoothOFF;
    private final PhoneClient parent;

    public PhoneUI(PhoneClient phoneClient) {
        super(phoneClient);
        parent = phoneClient;
        init();
    }

    @Override
    public void init() {
        super.init();
        Connect = new JButton("Connect");
        Disconnect = new JButton("Disconnect");
        BluetoothON = new JButton("BluetoothON");
        BluetoothOFF = new JButton("BluetoothOFF");
        scroll.setBounds(5, 40, UIConstants.COMPONENTWIDTH, 300);
        add(new JButton[]{Connect});
        add(new JButton[]{Disconnect});
        add(new JButton[]{BluetoothON});
        add(new JButton[]{BluetoothOFF});
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Connect) {
            parent.action("CONNECT");
        }
        if (e.getSource() == Disconnect) {
            parent.action("DISCONNECT");
        }
        if (e.getSource() == BluetoothON) {
            parent.action("BLUETOOTH");
        }
        if (e.getSource() == BluetoothOFF) {
            parent.action("BLUETOOTH");
        }
    }
}
