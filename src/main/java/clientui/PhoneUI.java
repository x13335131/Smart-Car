package clientui;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import client.PhoneClient;
import javax.swing.JPanel;

public class PhoneUI extends ClientUI {

    private JButton connect;
    private JButton disconnect;
    private JButton bluetoothON;
    private JButton playMusic;
    private final PhoneClient parent;

    public PhoneUI(PhoneClient phoneClient) {
        super(phoneClient);
        parent = phoneClient;
        init();
    }

    @Override
    public void init() {
        super.init();
        connect = new JButton("Connect");
        disconnect = new JButton("Disconnect");
        bluetoothON = new JButton("BluetoothON/OFF");
        playMusic = new JButton("Play Mp3");
        //bluetoothOFF = new JButton("BluetoothOFF");
        scroll.setBounds(5, 40, UIConstants.COMPONENTWIDTH, 300);
        add(new JButton[]{connect});
        add(new JButton[]{disconnect});
        add(new JButton[]{bluetoothON});
        add(new JButton[]{playMusic});
       // add(new JButton[]{bluetoothOFF});
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == connect) {
            parent.action("CONNECT");
        }
        if (e.getSource() == disconnect) {
            parent.action("DISCONNECT");
        }
        if (e.getSource() == bluetoothON) {
            parent.action("BLUETOOTH");
        }
          if (e.getSource() == playMusic) {
            parent.action("PLAYMUSIC");
        }
        /*if (e.getSource() == bluetoothOFF) {
            parent.action("BLUETOOTH");
        }*/
    }
   
}