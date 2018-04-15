package services;

import com.google.gson.Gson;
import java.util.Timer;
import java.util.TimerTask;
import models.MovementModel;
import serviceui.ServiceUI;

/**
 * The Class MovementService.
 * @author Louise O'Connor, x13335131
 */

public class MovementService extends Service {//Movement Service extends service class 

    private final Timer timer1;
    private int speed;
    private boolean isMoving;
    private boolean isStopping;
    private boolean stopped;
    private boolean engineOn;
    private boolean engineOff;
    private boolean faster;
    private boolean isStopped;
    private String action;
    private String msg;
    private String json;
    String SERVICE_UI_MESSAGE;
    MovementModel movement;

    /*
        *MovementService Constructor*
    */
    public MovementService(String name) {
        super(name, "_movement._udp.local.");
        timer1 = new Timer();
        engineOn = false;
        engineOff = true;
        speed = 0;
        stopped = false;
        isMoving = false;
        isStopping = false;
        faster = false;
        ui = new ServiceUI(this, name);

    }
    /*
        *Method performing different actions*
    */
    @Override
    public void performAction(String a) {
        System.out.println("recieved: " + a);
        movement = new Gson().fromJson(a, MovementModel.class);
        action = movement.getOperation().toString();
        System.out.println("recieved getAction: " + action);

        switch (action) {
            //if action==ACCELERTATE do this:
            case "STATUS":
                System.out.println("Status");
                msg = getStatus();
                json = new Gson().toJson(new MovementModel(MovementModel.Operation.STATUS, msg));
                sendBack(json); //sends back status 
                break;
            //if action==ACCELERTATE do this:
            case "ACCELERATE":
                System.out.println("Accelerate");
                accelerate();
                msg = (faster) ? "Car is already Moving" : "Car is moving";
                timer1.schedule(new RemindTask(), 0, 4000); //starts timer1 task //this remind task should be run every 2 seconds
                json = new Gson().toJson(new MovementModel(MovementModel.Operation.ACCELERATE, msg, faster));
                System.out.println("json" + json);
                sendBack(json);
                SERVICE_UI_MESSAGE = (faster) ? "Car is already Moving" : "Car is moving";
                ui.updateArea(SERVICE_UI_MESSAGE);
                break;
            //if action==SLOWDOWN do this:
            case "SLOWDOWN":
                System.out.println("Slowdown");
                slowDown();
                msg = "Car is stopping";
                timer1.schedule(new RemindTask(), 0, 4000); //starts timer1 task //this remind task should be run every 2 seconds
                json = new Gson().toJson(new MovementModel(MovementModel.Operation.SLOWDOWN, msg, isStopping));
                System.out.println("json" + json);
                sendBack(json);
                SERVICE_UI_MESSAGE = "Car is Stopping";
                ui.updateArea(SERVICE_UI_MESSAGE);
                break;
            //if action==ENGINEON do this:
            case "ENGINEON":
                System.out.println("Engine On");
                turnOnEngine();
                msg = (engineOn) ? "The Engine is turned on" : "The Engine is on";
                json = new Gson().toJson(new MovementModel(MovementModel.Operation.ENGINEON, msg, engineOn));
                sendBack(json);
                SERVICE_UI_MESSAGE = (engineOn) ? "The Engine is turned on" : "The engine is on";
                ui.updateArea(SERVICE_UI_MESSAGE);
                break;
            //if action==ENGINEOFF do this:
            case "ENGINEOFF":
                System.out.println("Engine Off");
                turnOffEngine();
                msg = (engineOff) ? "The Engine is turned off" : "The Engine is off";
                json = new Gson().toJson(new MovementModel(MovementModel.Operation.ENGINEOFF, msg, engineOff));
                System.out.println(json);
                sendBack(json);
                SERVICE_UI_MESSAGE = (engineOff) ? "The Engine is turned off" : "The engine is off";
                ui.updateArea(SERVICE_UI_MESSAGE);

            //else print the following:
            default:
                sendBack(BAD_COMMAND + " - " + a);
        }

    }

    class RemindTask extends TimerTask {

        @Override
        public void run() {//every time run method gets called it adds 10%, once its less that 100
            if (speed <= 200 && isMoving == true) {
                if (speed == 200) {
                    ui.updateArea(getStatus());
                } else {
                    speed += 5;
                }

            } else if (speed >= 0 && isStopping == true) {
                if (speed == 0) {
                    stopped = true;
                    ui.updateArea(getStatus());
                } else {
                    speed -= 5;
                }
                
            }else{
                ui.updateArea(getStatus());
            }

        }
    }

    @Override
    public String getStatus() {
        if (speed == 0) {
            return "Car is Stopped";
        }
        if (speed == 200) {
            return "Car is at Max speed";
        }
        return "Car is at a speed of " + speed + " kph.";
    }

    public static void main(String[] args) {
        new MovementService("Louise's");
    }

    public void accelerate() {
        if (isMoving == false) {
            faster = false;
            isMoving = true;
            isStopping = false;
        } else {
            isStopping = false;
            faster = true;
        }
    }

    public void slowDown() {
        if (isMoving == true) {
            isMoving = false;
            isStopping = true;
            isStopped = false;
        } else {
            isStopping = true;
            isStopped = true;
        }
    }

    public void turnOnEngine() {
        engineOn = true;
        engineOff = false;

    }

    public void turnOffEngine() {
        engineOff = true;
        engineOn = false;
        isMoving = false;
        isStopping = true;
        //setting speed to 0
        speed = 0;
    }
}
