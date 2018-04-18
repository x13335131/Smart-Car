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

    public PhoneService(String name) {
        super(name, "_phone._udp.local.");
        timer = new Timer();
        percentConnected = 0;
        isConnected = false;
        isDisconnected = false;
        isBluetoothOn = false;
        bluetoothOn = false;
        ui = new ServiceUI(this, name);
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
