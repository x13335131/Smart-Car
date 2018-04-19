/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author Louise O'Connor, x13335131
 * ref: http://www.vogella.com/tutorials/JavaLibrary-Gson/article.html
 */

//both service and client import this class.
public class MovementModel {
    
    //enumeration list(constants-one for each operation in which the car can perform)if not on list -it'll throw error
    public enum Operation {//the different actions
        STATUS, 
        ACCELERATE, 
        SLOWDOWN,
        ENGINEON, 
        ENGINEOFF,
        MOVED, 
        TOWN, REGIONAL, NATIONAL, MOTORWAY
    }

    private Operation operation;
    private String response;
    private boolean value;
    
    public MovementModel(){
        
    }
    /*
        3 Different constructors that take different params: operation, a response and a value
    */
    
    //this constructor is used when operations are passed in from clients
    public MovementModel(Operation operation){
        this.operation = operation;
    }
    //this constructor is used when operations are passed in from clients
    public MovementModel(Operation operation, String response){
        this.operation = operation;
        this.response = response;
    }
    //this constructor is mainly used when operations are being passed in from service
    public MovementModel(Operation operation, String response, boolean value ){
        this.operation = operation;
        this.response = response;
        this.value = value;
    }
    /*
        Getters && Setters
    */
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
