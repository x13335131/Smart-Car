package client;

import java.io.IOException;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

import clientui.ClientManagerUI;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Reference: Dominic Carr- https://moodle.ncirl.ie/course/view.php?id=1473 - "project sample"
 *
 * @author Louise O'Connor, x13335131
 */
public class ClientManager implements ServiceListener {

    /*
    controls the ui framewhcih controls all client connections 
     */
    private final ClientManagerUI ui;
    private JmDNS jmdns;
    private ArrayList<Client> clients;

    //constructor
    public ClientManager() {
        //array of clients
        clients = new ArrayList<>();

        //adding the different clients to array
        clients.add(new MovementClient());
        clients.add(new RadioClient());

        try {
            //looks for a jdms instance and creates a service listener for all the types of services the clients are interested in eg radio, phone, temp etc
            jmdns = JmDNS.create(InetAddress.getLocalHost());
            for (Client client : clients) {//for every client it creates a service listener
                //listener
                jmdns.addServiceListener(client.getServiceType(), this);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        ui = new ClientManagerUI(this);
    }

    public void end() {
        try {
            jmdns.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void serviceAdded(ServiceEvent arg0) {
        System.out.println(arg0);
        arg0.getDNS().requestServiceInfo(arg0.getType(), arg0.getName(), 0);
    }

    
    public void serviceRemoved(ServiceEvent arg0) {//this is checking if a service is being removed- ie if a service is going offline

        System.out.println(arg0);
        String type = arg0.getType();
        String name = arg0.getName();
        ServiceInfo newService = null;

        for (Client client : clients) {
            if (client.getServiceType().equals(type) && client.hasMultiple()) {
                if (client.isCurrent(name)) {
                    ServiceInfo[] a = jmdns.list(type);
                    for (ServiceInfo in : a) {
                        if (!in.getName().equals(name)) {
                            newService = in;
                        }
                    }
                    client.switchService(newService);
                }
                client.remove(name);
            } else if (client.getServiceType().equals(type)) {
                ui.removePanel(client.returnUI());
                client.disable();
                client.initialized = false;
            }
        }

    }

    public void serviceResolved(ServiceEvent arg0) {//only resolving services that we are interested in. therell be a service event- get info we need about that service, local host address( local area network-LANs), port and type of service  
        System.out.println(arg0);
        String address = arg0.getInfo().getHostAddress();
        int port = arg0.getInfo().getPort();
        String type = arg0.getInfo().getType();

        for (Client client : clients) { //***change here if one client has many services- ie kitchen client might have microwave service, dishwasher service and refridgerator service**** in this one client - one service
            //does this match the service type my client is looking for? if it does then add in there.
            if (client.getServiceType().equals(type) && !client.isInitialized()) {
                client.setUp(address, port);
                ui.addPanel(client.returnUI(), client.getName());
                client.setCurrent(arg0.getInfo());
                client.addChoice(arg0.getInfo());//adds choice-thats how it knows it has multiple different types of services
            } else if (client.getServiceType().equals(type)
                    && client.isInitialized()) {
                client.addChoice(arg0.getInfo());

            }
        }

    }

    public static void main(String[] args) {
        //main method to start the client manager
        new ClientManager();

    }
}
