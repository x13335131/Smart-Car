package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.jmdns.ServiceInfo;
import javax.swing.JPanel;

import clientui.ClientUI;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;

/**
 * Abstract super class for all clients. Reference: Dominic Carr-
 * https://moodle.ncirl.ie/course/view.php?id=1473 "project sample"
 *
 * @author louise
 */
public abstract class Client {

    protected ClientUI ui;
    protected String serverHost = "";
    protected int serverPort = 0;
    protected String serviceType = "stuff";
    protected boolean initialized = false;
    protected String name = " ";
    protected String GET_STATUS = "{operation: STATUS}";
    protected Socket toServer;
    protected String pollQuery;
    protected String pollResult;
    protected Timer timer;
    protected HashMap<String, ServiceInfo> services;
    protected final String BAD_COMMAND = "bad Command";
    protected final String OK = "OK";
    protected ServiceInfo current;
    protected String serverStatus;

    /**
     * Constructor.
     */
    public Client() {
        serverStatus = "";
        services = new HashMap<String, ServiceInfo>();
    }

    public void remove(String key) {
        for (String k : services.keySet()) {
            if (k.equals(key)) {
                ServiceInfo s = services.remove(k);
                services.remove(s);
                break;
            }
        }
        Vector<String> d = new Vector<String>();
        d.addAll(services.keySet());
        ui.addChoices(d);
    }

    public boolean isCurrent(String name) {
        return current.getName().equals(name);
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Adds the choice.
     *
     * @param serviceInfo the service info
     */
    public void addChoice(ServiceInfo serviceInfo) {
        services.put(serviceInfo.getName(), serviceInfo);
        HashMap<String, ServiceInfo> as = (HashMap<String, ServiceInfo>) services
                .clone();
        as.remove(current.getName());
        Vector<String> d = new Vector<String>();
        d.add(current.getName());
        System.out.println("current " + current.getName());
        d.addAll(as.keySet());
        System.out.println("called add choices from addchoice");
        ui.addChoices(d);
    }

    /**
     * Switch service.
     *
     * @param a the a
     */
    public void switchService(String a) {
        switchService(services.get(a));
    }

    /**
     * Switch service.
     *
     * @param newService the new service
     */
    @SuppressWarnings("unchecked")
    public void switchService(ServiceInfo newService) {
        System.out.println("switched");
        serverStatus = "";
        current = newService;
        HashMap<String, ServiceInfo> as = (HashMap<String, ServiceInfo>) services
                .clone();
        as.remove(current.getName());
        System.out.println(current.getName());
        Vector<String> d = new Vector<String>();
        d.add(current.getName());
        d.addAll(as.keySet());
        ui.refresh(d);
        setUp(newService.getHostAddress(), newService.getPort());
    }

    /**
     * Checks for multiple.
     *
     * @return true, if successful
     */
    public boolean hasMultiple() {
        return services.size() > 1;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setUp(String server, int port) {
        System.out.println(serverHost + " " + server + " " + serverPort + " " + port);
        serverHost = server;
        serverPort = port;
        initialized = true;
        timer = new Timer();
        timer.scheduleAtFixedRate(new PollServer(), 6000, 20000); //waits 6 seconds and every  20 seconds it talks to server, to see if it is alive or not
    }

    public JPanel returnUI() {
        return ui;
    }

    public String sendMessage(String a_message) {
        String msg = "";
        try {
            toServer = new Socket(serverHost, serverPort);
            PrintWriter out = new PrintWriter(toServer.getOutputStream(), true);
            out.println(a_message);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    toServer.getInputStream()));
            msg = in.readLine();
            out.close();
            toServer.close();
        } catch (Exception ioe) {
            ui.updateArea("Server Down");
        }
        return msg;
    }

    class PollServer extends TimerTask {

        //poll server is running on a timer
        @Override
        public void run() {
            String message;
            String response;
            String prevStatus;
            try {
                Socket pollSocket = new Socket(serverHost, serverPort);
                PrintWriter out = new PrintWriter(pollSocket.getOutputStream(),
                        true);
                out.println(GET_STATUS);
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        pollSocket.getInputStream()));
                message = in.readLine(); //reads in as string
                System.out.println("input message " + message);
                JsonParser parser = new JsonParser();
                JsonObject jObject = parser.parse(message).getAsJsonObject(); //converting string to json object
                if (jObject.has("response")) {
                    response = jObject.get("response").getAsString(); //getting 'response' key/value from json object
                    System.out.println("response " + response);
                    prevStatus = serverStatus;
                    serverStatus = message;
                    if (!prevStatus.equals(serverStatus)) {
                        ui.updateArea(response);
                        updatePoll(response);
                    }
                } else {
                    System.out.println("Status");
                }

                out.close();
                pollSocket.close();

                //multicatch for any exceptions that may arise
            } catch (IOException | NullPointerException | JsonSyntaxException e) {
                System.out.println("error: " + e);
                ui.updateArea("Server Down - attempting to poll service");
            }
        }
    }

    public void disable() {
        initialized = false;
    }

    public void updatePoll(String msg) {
        // TODO Auto-generated method stub

    }

    public void setCurrent(ServiceInfo info) {
        current = info;
    }
}
