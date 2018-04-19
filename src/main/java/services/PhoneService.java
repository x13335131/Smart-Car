package services;

import client.Client;
import client.RadioClient;
import com.google.gson.Gson;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import models.PhoneModel;
import models.RadioModel;

import serviceui.ServiceUI;

/**
 * Phone Service Mark McDonald, x14503387
 */
public class PhoneService extends Service implements ServiceListener { //extends from service class 

    private final Timer timer;
    private int percentConnected;
    private boolean isConnected;
    private boolean isDisconnected;
    private boolean isBluetoothOn;
    private boolean bluetoothOn;
    private boolean isConnecting;
    private boolean isMusicOn;
    private String currentSong;
    private String audioOutput;
    private String msg;
    private String json;
    List<String> SongList = new ArrayList<>();
    Random randomizer = new Random();
    String SERVICE_UI_MESSAGE;
    private Client client;
    private JmDNS jmds;
    protected HashMap<String, ServiceInfo> services;
    protected String serverStatus;
    protected ServiceInfo current;
    protected Socket toServer;
    protected String serverHost = "";
    protected String serviceType = "stuff";
    protected int serverPort = 0;
    private boolean isRadioOn;
    private RadioModel radio;

    public PhoneService(String name) {
        super(name, "_phone._udp.local.");

        System.out.println("name :" + name);
        client = new RadioClient();

        try {
            jmdns = JmDNS.create(InetAddress.getLocalHost());
            jmdns.addServiceListener(client.getServiceType(), this);
            System.out.println("constructor ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        serviceType = "_radio._udp.local.";
        timer = new Timer();
        percentConnected = 0;
        isConnected = false;
        isDisconnected = false;
        isBluetoothOn = false;
        bluetoothOn = false;
        ui = new ServiceUI(this, name);
        isConnecting = false;
        isMusicOn = false;
        SongList.add("Britney Spears~Piece of me");
        SongList.add("Robbie Williams~Angels");
        SongList.add("Beegees~Night Fever");
        SongList.add("Elton John~Tiny Dancer");
        SongList.add("Imagine Dragons~Its Time");
        SongList.add("Justin Bieber~Love Yourself");
        SongList.add("Queen~We Will Rock You");

    }
public String sendMessage(String a_message) {
        msg = client.sendMessage(a_message);
        /* String msg = "";
        System.out.println("msg from radio: "+ a_message);
        try {
            serverHost = client.get
            System.out.println("serverHost: "+serverHost+" server port: "+serverPort);
            toServer = new Socket(serverHost, serverPort);
            System.out.println("after to server line ");
            PrintWriter out = new PrintWriter(toServer.getOutputStream(), true);
            System.out.println("after print writer ");
            out.println(a_message);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    toServer.getInputStream()));
            msg = in.readLine();
            System.out.println("msg after readline: "+msg);
            out.close();
            toServer.close();
            System.out.println("finished try loop");
        } catch (Exception ioe) {
            System.out.println("CATCH");
            ui.updateArea("Server Down");
        }
         */

        System.out.println("got through send message to end");
        return msg;
    }
    @Override
    public void performAction(String a) {
        System.out.println("recieved: " + a);
        PhoneModel phone = new Gson().fromJson(a, PhoneModel.class);
        System.out.println("recieved getAction: " + phone.getOperation());
        if (null == phone.getOperation()) {
            sendBack(BAD_COMMAND + " - " + a);
        } else {
            switch (phone.getOperation()) {
                case STATUS: {
                    System.out.println("in STATUS ");
                    msg = getStatus();
                    json = new Gson().toJson(new PhoneModel(PhoneModel.Operation.STATUS, msg));
                    sendBack(json); //status is sent back
                    break;
                }
                case CONNECT: {
                   Connect();
                    msg = (isConnecting) ? "phone is connecting" : "The Phone is not connected-turn on bluetooth";
                    timer.schedule(new RemindTask(), 0, 10000); //starts timer1 task //this remind task should be run every 2 seconds
                    json = new Gson().toJson(new PhoneModel(PhoneModel.Operation.CONNECT, msg));
                    System.out.println(json);
                    sendBack(json);
                    SERVICE_UI_MESSAGE = (isConnected) ? "phone is connected" : "Turn on bluetooth";
                    ui.updateArea(SERVICE_UI_MESSAGE);
                    break;
                }
                case DISCONNECT: {
                    Disconnect();
                    msg = (isConnected) ? "Phone is connected" : "The phone is disconnected";
                    timer.schedule(new RemindTask(), 0, 4000); //starts timer1 task //this remind task should be run every 2 seconds
                    json = new Gson().toJson(new PhoneModel(PhoneModel.Operation.DISCONNECT, msg));
                    System.out.println(json);
                    sendBack(json);
                    SERVICE_UI_MESSAGE = (isConnected) ? "Phone is connected" : "The phone is disconnected";
                    ui.updateArea(SERVICE_UI_MESSAGE);
                    break;
                }
                case BLUETOOTH: {
                    Bluetooth();
                    msg = (isBluetoothOn) ? "Bluetooth On" : "Bluetooth Off";
                    json = new Gson().toJson(new PhoneModel(PhoneModel.Operation.BLUETOOTH, msg));
                    System.out.println(json);
                    sendBack(json);
                    SERVICE_UI_MESSAGE = (isBluetoothOn) ? "Bluetooth On" : "Bluetooth Off";
                    ui.updateArea(SERVICE_UI_MESSAGE);
                    break;
                }

                case MSG: {
                    System.out.println("in phone service");
                    Msg();
                    msg = (isConnected) ? "Phone is 100% connected" : "Phone is not yet fully connected";
                    json = new Gson().toJson(new PhoneModel(PhoneModel.Operation.MSG, msg));
                    System.out.println(json);
                    sendBack(json);
                    SERVICE_UI_MESSAGE = (isConnected) ? "Phone is 100% connected" : "Phone is not yet connected";
                    ui.updateArea(SERVICE_UI_MESSAGE);
                    break;
                }
                case PLAYMUSIC: {
                    //audioOutput();
                    PlayMusic();
                    msg = (isMusicOn) ? "Music On. Playing: " + currentSong + " audio output: " + audioOutput : "Music Off";
                    json = new Gson().toJson(new PhoneModel(PhoneModel.Operation.PLAYMUSIC, msg));
                    System.out.println(json);
                    sendBack(json);
                    SERVICE_UI_MESSAGE = (isMusicOn) ? "Music On. Playing: " + currentSong + "audio output: " + audioOutput : "Music Off";
                    ui.updateArea(SERVICE_UI_MESSAGE);
                    break;
                }
                default:
                    sendBack(BAD_COMMAND + " - " + a);
                    break;
            }
        }
    }

    private void Connect() {
        if ((bluetoothOn == true) && (isConnecting == false)) { //if bluetooth is on but not connected
            isConnecting = true;
            ui.updateArea("Connecting");
            isRadioConnected();
            if (isRadioOn == false) {
                System.out.println("radio off - playing through internal speakers");
            } else {
                System.out.println("radio ON - playing through radio speakers");
            }

        } else if (bluetoothOn == true && isConnected == true) { //if bluetooth is on and connected
            isConnected = true;
            isConnecting = false;
            ui.updateArea("Already Connected");
        } else {
            isConnected = false;
            isConnecting = false;
            ui.updateArea("Turn on bluetooth");
        }
    }

    private void isRadioConnected() {
        // client = new RadioClient();
        try {
            json = new Gson().toJson(new RadioModel(RadioModel.Operation.ISRADIOON));
            System.out.println("is radio on");
            msg = sendMessage(json);//sendMessage method is in client class
            System.out.println("msg: " + msg);
            radio = new Gson().fromJson(msg, RadioModel.class); //conversion
            System.out.println("Client Received " + json);
            System.out.println("Radio" + radio);
            System.out.println("Radio.getOperation " + radio.getOperation());
            System.out.println("RadioModel.Operation.ISRADIOON " + RadioModel.Operation.ISRADIOON);
            if (radio.getOperation() == RadioModel.Operation.ISRADIOON) {
                isRadioOn = radio.getValue();
                System.out.println("isRadioOn= " + isRadioOn);
            } else {
                System.out.println("No valid action found");
            }
            //print response to ui
            ui.updateArea(radio.getResponse());
        } catch (Exception e) {
            System.out.println("Radio unable to connect " + e);
            isRadioOn = false;
        }
    }

    private void Disconnect() {
        if ((bluetoothOn == true) && (isConnected == true)) { //If the bluetooth is on and connceted you are enabled to disconnect
             percentConnected = 0;
             isConnected = false;
            isConnected = false;
            ui.updateArea("Disconnected");

        } else {
            ui.updateArea("Nothing to disconnect");
        }
    }

    private void Bluetooth() {
        if (bluetoothOn == false) { //if the bluetooth is not operating
            isBluetoothOn = true;
            bluetoothOn = true; //turn on method
             percentConnected = 0;
            //isConnected = false; //not connected

        } else if (bluetoothOn == true) { //if the bluetooth is not operating
            isBluetoothOn = false;
            bluetoothOn = false; //put it on 
         //   isConnected = false; //not connected

        }
    }

    private void audioOutput() {
        if (isRadioOn == true && isConnected == true) {
            audioOutput = "radio speakers";
        } else {
            audioOutput = "internal speakers";
        }
    }

    private void PlayMusic() {
        //isRadioConnected();
        audioOutput();
        if (isMusicOn == false) {
            isMusicOn = true;
            String randomSong = SongList.get(randomizer.nextInt(SongList.size()));
            System.out.println("random song: " + randomSong);
            currentSong = randomSong;
        } else {
            isMusicOn = false;
        }

    }

    private void Msg() {
        if (percentConnected == 100) {
            isConnected = true;
        } else {
            isConnected = false;
        }
    }

    @Override
    public void serviceAdded(ServiceEvent se) {
        System.out.println("Service added method");
        System.out.println(se);
        se.getDNS().requestServiceInfo(se.getType(), se.getName(), 0);
    }

    @Override
    public void serviceRemoved(ServiceEvent se) {
        System.out.println("Service removed method");
        //checking if service is being removed
        System.out.println(se);
        String type = se.getType();
        String name = se.getName();
        ServiceInfo newService = null;
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
            // ui.removePanal(client.returnUI());
            client.disable();
            client.initialized = false;
        }
    }

    @Override
    public void serviceResolved(ServiceEvent se) {
        System.out.println("Service resolved method");
        //only resolving services we are interested in
        System.out.println(se);
        String address = se.getInfo().getHostAddress();
        int port = se.getInfo().getPort();
        String type = se.getInfo().getType();

        if (client.getServiceType().equals(type) && !client.isInitialized()) {
            client.setUp(address, port);
            System.out.println("address " + address + " port " + port);
            // ui.addP
            client.setCurrent(se.getInfo());
            client.addChoice(se.getInfo());

        } else if (client.getServiceType().equals(type) && client.isInitialized()) {
            client.addChoice(se.getInfo());
        }
    }

    class RemindTask extends TimerTask {

        @Override
        public void run() {//everytime method gets called it adds 20% until it reaches 100%
             if ((percentConnected <= 100) && (isConnected == true || isConnecting == true) && bluetoothOn == true) {
                if (percentConnected == 100) {
                    ui.updateArea(getStatus());
                } else {
                    percentConnected += 10;
                }
            } else {
                ui.updateArea(getStatus());
            }
        }
    }

    @Override
    public String getStatus() {
         isRadioConnected();
        if (percentConnected == 100) {
            return "Connection complete " + percentConnected + " %";
        } else if (percentConnected < 100 && bluetoothOn == true) {
            return "Connection status: " + percentConnected + "% ";
        } else {
            percentConnected = 0;
            return "bluetooth OFF. Connection Status: " + percentConnected;
        }
    }

    public static void main(String[] args) {
        new PhoneService(Constants.Phone_service_name);
    }
}
