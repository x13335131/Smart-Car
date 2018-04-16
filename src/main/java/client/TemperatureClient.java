package client;

import clientui.TemperatureUI;
import com.google.gson.Gson;
import models.TemperatureModel;

/**
 * Temperature Client.
 *
 * Mark McDonald, x14503387
 */
public class TemperatureClient extends Client { 

    private final String HEAT;
    private final String COOL;
    private final String MAX;
    private final String MIN;
    private String msg;
    private String json;
    private TemperatureModel temp;
    private boolean isHeating;
    private boolean Hot;
    String output= " ";

    /**
     * Constructor.
     */
    public TemperatureClient() {
        super();
        this.isHeating = false;
        this.MIN = "Min";
        this.MAX = "Max";
        this.COOL = "Cooled";
        this.HEAT = "Heated";
        this.Hot = false;
        ////talks to the temperature services
        serviceType = "_temperature._udp.local.";
        ui = new TemperatureUI(this);//
        name = "Temperature"; 
    }

    public void action(String action) {

        //preform different task depending on the operation chosen. eg heat = HEAT
        switch (action) {
            case "HEAT":
                System.out.println("Heat");
                json = new Gson().toJson(new TemperatureModel(TemperatureModel.Operation.HEAT));
                break;
            case "COOL":
                json = new Gson().toJson(new TemperatureModel(TemperatureModel.Operation.COOL));
                System.out.println("Cool");
                break;
            case "INCREASE":
                json = new Gson().toJson(new TemperatureModel(TemperatureModel.Operation.INCREASE));
                System.out.println("Increase");
                break;
            case "DECREASE":
                json = new Gson().toJson(new TemperatureModel(TemperatureModel.Operation.DECREASE));
                System.out.println("Decrease");
                break;
            case "CONFIRM":
                json = new Gson().toJson(new TemperatureModel(TemperatureModel.Operation.CONFIRM));
                System.out.println("Confirm");
                break;
            default:
                System.out.println("Invalid operation");
        }

        msg = sendMessage(json);
        temp = new Gson().fromJson(msg, TemperatureModel.class);
        System.out.println("Client Received " + json);
        
        
        if (temp.getOperation() == TemperatureModel.Operation.HEAT) {
            isHeating = temp.getValue();
            //will return isheating true or false
        } 
        else if (temp.getOperation() == TemperatureModel.Operation.COOL) {
            isHeating = temp.getValue();
            //will return isheating true or false
        } 
        else if (temp.getOperation() == TemperatureModel.Operation.INCREASE) {
             output = temp.getResponse();
            
        } 
        else if (temp.getOperation() == TemperatureModel.Operation.DECREASE) {
            output = temp.getResponse();
            
        } 
        else if (temp.getOperation() == TemperatureModel.Operation.CONFIRM) {
            output = temp.getResponse();
        }
        else {
            System.out.println("operation not found");
        }
        clientui.TemperatureUI.setText(output);
        ui.updateArea(temp.getResponse());
    }

    @Override
    public void updatePoll(String msg) {
        json = new Gson().toJson(new TemperatureModel(TemperatureModel.Operation.STATUS)); 
        msg = sendMessage(json);//sendMessage is initialized in the client class
        temp = new Gson().fromJson(msg, TemperatureModel.class); //converted from gson to json
        System.out.println("Client Received " + json);
        if (temp.getOperation() == TemperatureModel.Operation.STATUS) {
            isHeating = temp.getValue();
        }
    }

    @Override
    public void disable() {
        super.disable();
        ui = new TemperatureUI(this);
        isHeating = false;
    }
}
