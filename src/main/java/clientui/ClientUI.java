/*
 * 
 */
package clientui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import client.Client;
import java.awt.Font;

/**
 * The Class ClientUI.
 *
 * Mark McDonald
 * ref Dominic Carr
 */
public abstract class ClientUI extends JPanel implements ActionListener {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
    protected JComboBox services;//this is where it shows if its dominics or pauls bath etc
    protected JPanel controls;
    protected JTextArea textArea;
    protected JScrollPane scroll;
    protected Client p;
    Font trb = new Font("TimesRoman", Font.BOLD, 16);
    Font trb1 = new Font("TimesRoman", Font.BOLD, 14);

    public ClientUI(Client a) {
        p = a;
    }

    public void init() {
        setLayout(null);
        services = new JComboBox();
        services.addActionListener(actionListener);
        add(services);
        services.setBounds(170, 5, 200, 30);
        // textArea.setBorder(BorderFactory.createEtchedBorder(Color.red, Color.yellow));

        controls = new JPanel();
        controls.setBounds(UIConstants.CONTROLX, UIConstants.CONTROLY, UIConstants.COMPONENTWIDTH,
                UIConstants.COMPONENTHEIGHT);
        controls.setLayout(new FlowLayout());
        controls.setBorder(BorderFactory.createLineBorder(Color.gray, UIConstants.BORDERWIDTH, true));
        controls.setBackground(Color.lightGray);
        add(controls);
        textArea = new JTextArea();
        textArea.setBorder(BorderFactory.createEtchedBorder(Color.gray, Color.lightGray));
        textArea.setBackground(Color.lightGray);
        textArea.setFont(trb);
       // textArea.setForeground(Color.red);
        textArea.setEditable(false);
        scroll = new JScrollPane();
        scroll.setViewportView(textArea);
        add(scroll);
    }

    public void add(JButton[] a) {
        for (JButton in : a) {
            in.addActionListener(this);
            in.setFont(trb1);
            in.setForeground(Color.red);
            controls.add(in);
        }
    }

    public void addChoices(Vector<String> a) {
        System.out.println("passed to add choices" + a);
        remove(services);
        services = new JComboBox(a);
        services.addActionListener(actionListener);
        services.setBounds(170, 5, 200, 30);
        add(services);
        updateUI();
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JComboBox cb = (JComboBox) actionEvent.getSource();
            String serviceName = (String) cb.getSelectedItem();
            p.switchService(serviceName);
        }
    };

    public void refresh(Vector<String> a) {
        this.removeAll();
        init();
        addChoices(a);
    }

    public void clearArea() {
        textArea.setText("");
    }

    public void updateArea(String string) {
        if (textArea.getText().equals("")) {
            textArea.append(string);
        } else {
            textArea.append("\n" + string);
        }
    }
}
