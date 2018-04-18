package services;

import com.google.gson.Gson;
import java.util.Timer;
import java.util.TimerTask;
import models.TemperatureModel;

import serviceui.ServiceUI;

/**
 * Temperature Service.
 *
 * Mark McDonald, x14503387
 */
public class TemperatureService extends Service { //extends service class 

    private final Timer timer;
    private int degrees;
    private boolean heating;
    private boolean isHeating;
    private boolean beginHeating;
    private boolean beginCooling;
    private String msg;
    private String json;
    private int tempTemp = 30;
    private int maxTemp = 35;
    String SERVICE_UI_MESSAGE;

    public TemperatureService(String name) {
        super(name, "_temperature._udp.local.");
        isHeating = false;
        beginHeating = false;
        beginCooling = false;
        timer = new Timer();
        degrees = 0;
        ui = new ServiceUI(this, name);
    }

    @Override
    public void performAction(String a) {
        System.out.println("recieved: " + a);
        TemperatureModel temp = new Gson().fromJson(a, TemperatureModel.class);

        if (null == temp.getOperation()) {
            sendBack(BAD_COMMAND + " - " + a);
        } else {// if it gets a request
            switch (temp.getOperation()) {
                case STATUS: {
                    msg = getStatus();
                    json = new Gson().toJson(new TemperatureModel(TemperatureModel.Operation.STATUS, msg));
                    sendBack(json);
                    break;
                }
                case HEAT: {
                    heating = true;
                    Heat();
                    msg = (beginHeating) ? "Car is heating" : "Car is already heating";
                    timer.schedule(new RemindTask(), 0, 2000);//this remind task should be run every 2 seconds
                    json = new Gson().toJson(new TemperatureModel(TemperatureModel.Operation.HEAT, msg));
                    System.out.println(json);
                    sendBack(json);
                    SERVICE_UI_MESSAGE = msg = (beginHeating) ? "Car is heating" : "Car is already heating";
                    ui.updateArea(SERVICE_UI_MESSAGE);
                    break;
                }
                case COOL: {
                    heating = false;
                    Cool();
                    msg = (beginCooling) ? "Car is cooling" : "Car is already cooling";
                    timer.schedule(new RemindTask(), 0, 2000);//this remind task should be run every 2 seconds
                    json = new Gson().toJson(new TemperatureModel(TemperatureModel.Operation.COOL, msg));
                    System.out.println(json);
                    sendBack(json);
                    SERVICE_UI_MESSAGE = "Increasing temperature";
                    ui.updateArea(SERVICE_UI_MESSAGE);
                    break;
                }
                case INCREASE: {
                    Increase();
                    msg = "" + tempTemp; //concatinating the string with the temperature
                    timer.schedule(new RemindTask(), 0, 2000);
                    json = new Gson().toJson(new TemperatureModel(TemperatureModel.Operation.INCREASE, msg));
                    System.out.println(json);
                    sendBack(json);
                    SERVICE_UI_MESSAGE = "Increasing temperature " + tempTemp;
                    ui.updateArea(SERVICE_UI_MESSAGE);
                    break;
                }
                case DECREASE: {
                    Decrease();
                    msg = "" + tempTemp;
                    timer.schedule(new RemindTask(), 0, 2000);
                    json = new Gson().toJson(new TemperatureModel(TemperatureModel.Operation.DECREASE, msg));
                    System.out.println(json);
                    sendBack(json);
                    SERVICE_UI_MESSAGE = "Decreasing temperature " + tempTemp;
                    ui.updateArea(SERVICE_UI_MESSAGE);
                    break;
                }
                case CONFIRM: {
                    Confirm();
                    heating = false;
                    msg = "" + maxTemp;
                    timer.schedule(new RemindTask(), 0, 2000);
                    json = new Gson().toJson(new TemperatureModel(TemperatureModel.Operation.CONFIRM, msg));
                    System.out.println(json);
                    sendBack(json);
                    SERVICE_UI_MESSAGE = "Confirmed. New max temp " + maxTemp;
                    ui.updateArea(SERVICE_UI_MESSAGE);
                    break;
                }
                default:
                    sendBack(BAD_COMMAND + " - " + a);
                    break;
            }
        }
    }

    public void Heat() {
        if (isHeating == false) { //if temp isn't already heating
            beginHeating = true;
            isHeating = true;//make it heat
            ui.updateArea("Car is heating");

        } else {//it is heating
            beginHeating = false;
            ui.updateArea("Car is already heating");
        }
    }

    public void Cool() {
        if (isHeating == true) { //if temp is heating
            beginCooling = true;//start cooling
            isHeating = false;//stop it heating
            ui.updateArea("Temperature is cooling");

        } else {
            beginCooling = false; //already cooling;
            ui.updateArea("Car is already cooling");
        }
    }

    private void Confirm() {
        maxTemp = tempTemp;
        ui.updateArea("max temp is now : " + maxTemp); //confirming what the max temperature can be
    }

    private void Decrease() {
        if (tempTemp <= 35 && tempTemp > 25) {
            tempTemp = tempTemp - 1;
        }
        tempTemp = tempTemp;
        ui.updateArea("tempTemp: " + tempTemp);
    }

    private void Increase() {
        if (tempTemp < 35 && tempTemp >= 25) {
            tempTemp = tempTemp + 1;
        }
        tempTemp = tempTemp;
        ui.updateArea("tempTemp: " + tempTemp);
    }

    class RemindTask extends TimerTask {

        @Override
        public void run() {//every time run method gets called it adds 5, once its less than 25 degrees
            if (degrees <= maxTemp && heating == true) {
                if (degrees == maxTemp) {
                    ui.updateArea(getStatus());

                } else {
                    degrees += 1;
                    //cooling=true;
                }
            } else if (degrees >= 0 && heating == false) {
                if (degrees == 0) {
                    ui.updateArea(getStatus());
                } else {
                    degrees -= 1;
                    // heating=false;
                }
            } else {
                ui.updateArea(getStatus());
            }

        }
    }

    @Override
    public String getStatus() {
        if (degrees == 0) {
            return "car is at minimum temp of " + degrees;
        }
        if (degrees == maxTemp) {
            return "car is at max temp of " + degrees;
        } else {
            return "Car is currently " + degrees + " degrees.";
        }
    }

    public static void main(String[] args) {
        new TemperatureService(Constants.Temperature_service_name);
    }
}
