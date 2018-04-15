/*
 * 
 */
package client;

import clientui.RadioUI;
import com.google.gson.Gson;
import models.RadioModel;

/**
 * Radio Client.
 *
 * @author Louise O'Connor, x13335131
 */
public class RadioClient extends Client { //Radio client extends from client

    private final String RADIOON;
    private final String RADIOOFF;
    private final String NEXT;
    private final String PREVIOUS;
    private boolean radioON;
    private String msg;
    private String json;
    private RadioModel radio;
    /**
     * Radio Client Constructor.
     */
    public RadioClient() {
        super();
        this.radioON = true;
        this.PREVIOUS = "Previous";
        this.NEXT = "Next";
        this.RADIOOFF = "RadioOff";
        this.RADIOON = "RadioOn";
        //specifies it wants to talk to radio services
        serviceType = "_radio._udp.local.";
        ui = new RadioUI(this);//new ui
        name = "Radio"; //pops up at top of ui
    }
 public void action(String action) {

        //depending on action passed through it performs different tasks
        switch (action) {
            case "RADIOON":
                System.out.println("Radio On");
                json = new Gson().toJson(new RadioModel(RadioModel.Operation.RADIOON));
                break;
            case "RADIOOFF":
                json = new Gson().toJson(new RadioModel(RadioModel.Operation.RADIOOFF));
                System.out.println("Radio Off");
                break;
            case "NEXT":
                json = new Gson().toJson(new RadioModel(RadioModel.Operation.NEXT));
                System.out.println("Next");
                break;
            case "PREVIOUS":
                json = new Gson().toJson(new RadioModel(RadioModel.Operation.PREVIOUS));
                System.out.println("Previous");
                break;
            default:
                System.out.println("Invalid action");
        }

        //contd...
        msg = sendMessage(json);//sendMessage method is in client class
        radio = new Gson().fromJson(msg, RadioModel.class); //conversion
        System.out.println("Client Received " + json);

        if (radio.getOperation() == RadioModel.Operation.RADIOON) {
            radioON = radio.getValue();
        } else if (radio.getOperation() == RadioModel.Operation.RADIOOFF) {
            radioON = radio.getValue();
        } else if (radio.getOperation() == RadioModel.Operation.NEXT) {
            radioON = radio.getValue();
        } else if (radio.getOperation() == RadioModel.Operation.PREVIOUS) {
            radioON = radio.getValue();
        }else {
            System.out.println("No valid action found");
        }
        //print response to ui
        ui.updateArea(radio.getResponse());
    }
 
    @Override
    public void disable() {
        super.disable();
        ui = new RadioUI(this);
        radioON = false;
    }
}
