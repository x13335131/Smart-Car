/*
 * 
 */
package client;

import clientui.PhoneUI;
import com.google.gson.Gson;
import models.PhoneModel;

/**
 * Phone Client.
 *
 * Mark McDonald, x14503387
 * Ref: Dominic Carr 
 */
public class PhoneClient extends Client { //extends from the client

    private final String CONNECT;
    private final String DISCONNECT;
    private final String BLUETOOTHON;
    private final String BLUETOOTHOFF;
    private final String BLUETOOTH;
    private final String CALLINPROGRESS;
    private final String CANCEL;
    private boolean isConnected;
    private boolean bluetoothOn;
    private String msg;
    private String json;
    private PhoneModel phone;

    
    //Phone Constructor.
     
    public PhoneClient() {
        super();
        this.bluetoothOn = false;
        this.isConnected = false;
        this.CANCEL = "Cancel";
        this.CALLINPROGRESS = "Calling";
        this.BLUETOOTH = "Bluetooth";
        this.BLUETOOTHOFF = "BluetoothOff";
        this.BLUETOOTHON = "BluetoothOn";
        this.DISCONNECT = "Disconnect";
        this.CONNECT = "Connect";
        //talks to the phone service
        serviceType = "_phone._udp.local.";
        ui = new PhoneUI(this);//generate PhoneUI
        name = "Phone";
    }

    public void action(String action) {

        //depending on action passed through it performs different tasks
        switch (action) {
            case "BLUETOOTH":
                System.out.println("Bluetooth");
                json = new Gson().toJson(new PhoneModel(PhoneModel.Operation.BLUETOOTH));
                break;
            case "CONNECT":
                json = new Gson().toJson(new PhoneModel(PhoneModel.Operation.CONNECT));
                System.out.println("Connect");
                break;
            case "DISCONNECT":
                json = new Gson().toJson(new PhoneModel(PhoneModel.Operation.DISCONNECT));
                System.out.println("Disconnect");
                break;
            default:
                System.out.println("Invalid option");
        }

        msg = sendMessage(json);//this method will be called in the client class
        phone = new Gson().fromJson(msg, PhoneModel.class); //converted to json
        System.out.println("Client Received " + json);

        if (phone.getOperation() == PhoneModel.Operation.BLUETOOTH) {
            bluetoothOn = phone.getValue();
        } 
        else if (phone.getOperation() == PhoneModel.Operation.CONNECT) {
            isConnected = phone.getValue();
        } 
        else if (phone.getOperation() == PhoneModel.Operation.DISCONNECT) {
            isConnected = phone.getValue();
        } 
        else {
            System.out.println("Not a valid action");
        }
       
        ui.updateArea(phone.getResponse());
    }

    @Override
    public void updatePoll(String msg) {
        json = new Gson().toJson(new PhoneModel(PhoneModel.Operation.CONNECT)); 
        msg = sendMessage(json);//sendMessage is initialized in the client class
        phone = new Gson().fromJson(msg, PhoneModel.class); //converted from gson to json
        System.out.println("Client Received " + json);
        if (phone.getOperation() == PhoneModel.Operation.CONNECT) {
            isConnected = phone.getValue();
        }
    }

    @Override
    public void disable() {
        super.disable();
        ui = new PhoneUI(this);
        isConnected = false;
    }
}
