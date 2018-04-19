package services;

import com.google.gson.Gson;
import java.util.Timer;
import java.util.TimerTask;
import models.PhoneModel;

import serviceui.ServiceUI;

/**
 * Phone Service Mark McDonald, x14503387
 */
public class PhoneService extends Service { //extends from service class 

    private final Timer timer;
    private int percentConnected;
    private boolean isConnected;
    private boolean isDisconnected;
    private boolean isBluetoothOn;
    private boolean bluetoothOn;
    private boolean isConnecting;
    private boolean isMusicOn;
    private String currentSong;
    private String msg;
    private String json;
    List<String> SongList = new ArrayList<>();
    Random randomizer = new Random();
    String SERVICE_UI_MESSAGE

    public PhoneService(String name) {
        super(name, "_phone._udp.local.");
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
                    String msg = getStatus();
                    String json = new Gson().toJson(new PhoneModel(PhoneModel.Operation.STATUS, msg));
                    sendBack(json); //status is sent back
                    break;
                }
                case CONNECT: {
                    Connect();
                    String msg = (isConnected) ? "Phone is connected" : "The Phone is not connected-turn on bluetooth"; //one line if statment to see if it's connected
                    String json = new Gson().toJson(new PhoneModel(PhoneModel.Operation.CONNECT, msg));
                    System.out.println(json);
                    sendBack(json);
                    String serviceMessage = (isConnected) ? "Phone is connected" : "Turn on Bluetooth";
                    ui.updateArea(serviceMessage);
                    break;
                }
                case DISCONNECT: {
                    Disconnect();
                    String msg = (isConnected) ? "Phone is connected" : "The phone is disconnected";
                    String json = new Gson().toJson(new PhoneModel(PhoneModel.Operation.DISCONNECT, msg));
                    System.out.println(json);
                    sendBack(json);
                    String serviceMessage = (isConnected) ? "Phone is connected" : "The phone is disconnected";
                    ui.updateArea(serviceMessage);
                    break;
                }
                case BLUETOOTH: {
                    Bluetooth();
                    String msg = (isBluetoothOn) ? "Bluetooth On" : "Bluetooth Off";
                    String json = new Gson().toJson(new PhoneModel(PhoneModel.Operation.BLUETOOTH, msg));
                    System.out.println(json);
                    sendBack(json);
                    String serviceMessage = (isBluetoothOn) ? "Bluetooth On" : "Bluetooth Off";
                    ui.updateArea(serviceMessage);
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
        if ((bluetoothOn == true) && (isConnected == false)) { //If it's on but not connected

            isConnected = true;
            ui.updateArea("Connected");

        } else if (bluetoothOn == true && isConnected == true) { //If bluetooth is on and connected
            isConnected = true;
            ui.updateArea("Already Connected");
        } else {
            ui.updateArea("Turn on bluetooth");
        }
    }

    private void Disconnect() {
        if ((bluetoothOn == true) && (isConnected == true)) { //If the bluetooth is on and connceted you are enabled to disconnect

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
            isConnected = false; //not connected

        } else if (bluetoothOn == true) { //if the bluetooth is not operating
            isBluetoothOn = false;
            bluetoothOn = false; //put it on 
            isConnected = false; //not connected

        }
    }

    private void PlayMusic() {
        //isRadioConnected();
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

    class RemindTask extends TimerTask {

        @Override
        public void run() {//everytime method gets called it adds 20% until it reaches 100%
            if (percentConnected < 100) {
                percentConnected += 20;
            }
        }
    }

    @Override
    public String getStatus() {
        if (percentConnected == 100) {
            return "Connection complete ";
        }
        return "Connection status: " + percentConnected + "% ";
    }

    public static void main(String[] args) {
        new PhoneService(Constants.Phone_service_name);
    }
}