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
public class RadioModel {

    //enumeration list(constants-one for each operation in which the car can perform)if not on list -it'll throw error
    public enum Operation {
        STATUS,
        RADIOON,
        RADIOOFF,
        NEXT,
        PREVIOUS,
        ISRADIOON
    }

    private Operation operation;
    private String response;
    private boolean value;

    //constructor-no params
    public RadioModel() {

    }
    /*
        3 Different constructors that take different params: operation, a response and a value
     */
    public RadioModel(Operation operation) {
        this.operation = operation;
    }

    public RadioModel(Operation operation, String response) {
        this.operation = operation;
        this.response = response;
    }

    public RadioModel(Operation operation, String response, boolean value) {
        this.operation = operation;
        this.response = response;
        this.value = value;
    }

    /*
        GETTERS && SETTERS
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
