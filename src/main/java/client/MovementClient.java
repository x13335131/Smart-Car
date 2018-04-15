package client;

import clientui.MovementUI;
import models.MovementModel;
import com.google.gson.Gson;

/**
 * Movement Client.
 * ref: using gson to action string to json object https://stackoverflow.com/questions/4110664/gson-directly-action-string-to-jsonobject-no-pojo
 * @author Louise O'Connor, x13335131
 */
public class MovementClient extends Client {  //Movement client extends from client
    
    private final String ACCELERATE;
    private final String SLOWDOWN;
    private final String ENGINEON;
    private final String ENGINEOFF;
    private boolean isMoving;
    private boolean isStopping;
    private boolean engineOn;
    private boolean engineOff;
    private String msg;
    private String json;
    private MovementModel movement;

    /**
     * Movement Client Constructor.
     */

    //constructor
    public MovementClient() {
        super();
        this.engineOff = true;
        this.engineOn = false;
        this.isStopping = true;
        this.isMoving = false;
        this.ENGINEOFF = "Engine OFF";
        this.ENGINEON = "Engine ON";
        this.SLOWDOWN = "SlowDown";
        this.ACCELERATE = "Accelerate";
        
        //specifies it wants to talk to movement services
        serviceType = "_movement._udp.local.";
        ui = new MovementUI(this);//new ui
        name = "Movement"; //pops up at top of ui
    }

    /**
     * action method
     * @param action
     */
  
    public void action(String action){
        
        //depending on action passed through it performs different tasks
        switch(action) {
          //if action==ACCELERTATE do this:
         case "ACCELERATE" :
            System.out.println("Accelerate"); 
            json = new Gson().toJson(new MovementModel(MovementModel.Operation.ACCELERATE));
            break;
         //if action==SLOWDOWN do this:
         case "SLOWDOWN" :
             json = new Gson().toJson(new MovementModel(MovementModel.Operation.SLOWDOWN));
             System.out.println("Slowdown"); 
            break;
          //if action==ENGINEON do this:
         case "ENGINEON" :
             json = new Gson().toJson(new MovementModel(MovementModel.Operation.ENGINEON));
            System.out.println("Engine On");
            break;
          //if action==ENGINEOFF do this:
         case "ENGINEOFF" :
             json = new Gson().toJson(new MovementModel(MovementModel.Operation.ENGINEOFF));
            System.out.println("Engine Off");
         //else print the following:
         default :
            System.out.println("Invalid action");
      }
        
        //contd...
        msg = sendMessage(json);//sendMessage method is in client class
        movement = new Gson().fromJson(msg, MovementModel.class); //conversion
        System.out.println("Client Received " + json);
        
        if (movement.getOperation() == MovementModel.Operation.ACCELERATE) {
            isMoving = movement.getValue();  
        }
        else if (movement.getOperation() == MovementModel.Operation.SLOWDOWN) {
            isStopping = movement.getValue();  
        }
       else if (movement.getOperation() == MovementModel.Operation.ENGINEON) {
            engineOn = movement.getValue();  
        }
       else if (movement.getOperation() == MovementModel.Operation.ENGINEOFF) {
            engineOff = movement.getValue();  
        }else{
           System.out.println("No valid action found");
       }
        //print response to ui
        ui.updateArea(movement.getResponse());
    }
    @Override
    public void updatePoll(String msg) {
        json = new Gson().toJson(new MovementModel(MovementModel.Operation.MOVED));
        msg = sendMessage(json);//sendMessage method is in client class
        movement = new Gson().fromJson(msg, MovementModel.class); //conversion
        System.out.println("Client Received " + json);
        if (movement.getOperation() == MovementModel.Operation.MOVED) {
            isMoving = movement.getValue();
        }
    }

    @Override
    public void disable() {
        super.disable();
        ui = new MovementUI(this);
        isMoving = false;
        isStopping = false;
        engineOn = false;
        engineOff = false;
    }
}
