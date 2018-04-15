package services;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;
import models.RadioModel;

import serviceui.ServiceUI;

/**
 * The Class RadioService.
 *
 * @author Louise O'Connor, x13335131
 */
public class RadioService extends Service { //extends service class 

    private boolean isRadioON;
    private String currentStation;
    String nextStation;
    String previousStation;
    int i = 0;
    List<String> StationsList = new ArrayList<>();
    ListIterator<String> listIterator;

    public RadioService(String name) {
        super(name, "_radio._udp.local.");
        isRadioON = false;
        ui = new ServiceUI(this, name);
        StationsList.add("Spin 103.8");
        StationsList.add("98 FM");
        StationsList.add("FM104");
        StationsList.add("Q102");
        StationsList.add("SUNSHINE 106.8");
        listIterator = StationsList.listIterator();
    }

    /*
    METHODS
     */
    @Override
    public void performAction(String a) {
        //if gets a request for status
        System.out.println("recieved: " + a);
        RadioModel radio = new Gson().fromJson(a, RadioModel.class);
        System.out.println("recieved getAction: " + radio.getOperation());
        if (null == radio.getOperation()) {
            sendBack(BAD_COMMAND + " - " + a);
        } else {
            switch (radio.getOperation()) { //depending on action do one of following:
                case STATUS: {
                    String msg = getStatus();
                    String json = new Gson().toJson(new RadioModel(RadioModel.Operation.STATUS, msg));
                    sendBack(json); //sends back status 
                    break;
                }
                case RADIOON: {
                    RadioOn();
                    String msg = (isRadioON) ? "Radio ON" : "Radio Already On";
                    String json = new Gson().toJson(new RadioModel(RadioModel.Operation.RADIOON, msg));
                    sendBack(json); //sends back status 
                    String serviceMessage = (isRadioON) ? "Radio ON" : "Radio Already On";
                    ui.updateArea(serviceMessage);
                    break;
                }
                case RADIOOFF: {
                    RadioOFF();
                    String msg = (isRadioON) ? "Radio ON" : "Radio OFF";
                    String json = new Gson().toJson(new RadioModel(RadioModel.Operation.RADIOOFF, msg));
                    sendBack(json); //sends back status 
                    String serviceMessage = (isRadioON) ? "Radio ON" : "Radio OFF";
                    ui.updateArea(serviceMessage);
                    break;
                }
                case NEXT: {
                    NextStation();
                    String msg = currentStation;
                    System.out.println("msg" + msg);
                    String json = new Gson().toJson(new RadioModel(RadioModel.Operation.NEXT, msg));
                    sendBack(json); //sends back status 
                    String serviceMessage = currentStation;
                    ui.updateArea(serviceMessage);
                    break;
                }
                case PREVIOUS: {
                    PreviousStation();
                    String msg = currentStation;
                    System.out.println("msg" + msg);
                    String json = new Gson().toJson(new RadioModel(RadioModel.Operation.PREVIOUS, msg));
                    sendBack(json); //sends back status 
                    String serviceMessage = currentStation;
                    ui.updateArea(serviceMessage);
                    break;
                }
                default:
                    sendBack(BAD_COMMAND + " - " + a);
                    break;
            }
        }
    }

    /*
    SUB-METHODS-CALLED IN ABOVE METHOD
     */
    public void RadioOn() {
        if (isRadioON == false) { //if radio is off
            isRadioON = true; //turn it on
            ui.updateArea("Radio ON");
        } else { //already on
            ui.updateArea("Radio is already ON");
        }
    }

    private void RadioOFF() {
        if (isRadioON == true) {//if radio is on
            isRadioON = false; //turn off
            ui.updateArea("Radio OFF");
        } else {//radio is already off
            ui.updateArea("Radio is already OFF");
        }
    }

    private void NextStation() {
        System.out.println("Iterating in Forward direction : ");
        try {
            if (listIterator.hasNext()) { //if there is more stations next
                nextStation = listIterator.next();
                if (nextStation != null) {//if station doesnt equal null
                    currentStation = nextStation; //set this as the current station
                }
            } else { //no more stations next
                listIterator = StationsList.listIterator(); //reset iteration
                System.out.println("end of iteration");
            }
        } //ERROR CATCHING
        catch (Exception e) {
            System.out.println("error " + e);
        }
    }

    private void PreviousStation() {
        System.out.println("Iterating in backward direction : ");
        try {
            if (listIterator.hasPrevious()) { //if the list has previous
                previousStation = listIterator.previous(); //get the previous station
                if (previousStation != null) { //if not equal to null
                    currentStation = previousStation; //this is the current station
                }
            } else {
                listIterator = StationsList.listIterator(5); //reset back to end of list and start iterating through again
                System.out.println("end of iteration");
            }
        } //ERROR CATCHING
        catch (Exception e) {
            System.out.println("error " + e);
        }
        System.out.println();
    }

    @Override
    public String getStatus() {
        if (isRadioON == true) {
            return "Radio ON.";
        } else {
            return "Radio OFF.";
        }
    }

    public static void main(String[] args) {
        new RadioService("Shauna's");
    }
}
