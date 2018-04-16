package models;

/**
 *
 * Mark McDonald, x14503387
 * ref: http://www.vogella.com/tutorials/JavaLibrary-Gson/article.html
 */

//both service and client import this class.
public class PhoneModel {
    
    public enum Operation {
        STATUS, CONNECT, DISCONNECT, BLUETOOTHON, BLUETOOTH, BLUETOOTHOFF, CALLINPROGRESS, CANCEL
    }

    private Operation operation;
    private String response;
    private boolean value;
    
    public PhoneModel(){
        
    }
    
    public PhoneModel(Operation operation){
        this.operation = operation;
    }
    
    public PhoneModel(Operation operation, String response){
        this.operation = operation;
        this.response = response;
    }
      
    public PhoneModel(Operation operation, String response, boolean value ){
        this.operation = operation;
        this.response = response;
        this.value = value;
    }
    
    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
    
}
