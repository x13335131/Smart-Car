/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

/**
 *
 * @author Louise O'Connor, x13335131
 */
public class RunAllServices {
      public static void main(String[] args){
        new MovementService(Constants.Movement_service_name);
        new RadioService(Constants.Radio_service_name);
        new PhoneService(Constants.Phone_service_name);
        new TemperatureService(Constants.Temperature_service_name);
    }
}
