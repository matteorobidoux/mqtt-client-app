package datacomproject.mqttclientapp;

import datacomproject.mqttclientapp.sensors.DHTSensor;
import datacomproject.mqttclientapp.sensors.DoorbellButton;
import datacomproject.mqttclientapp.sensors.MotionSensor;

/**
 *
 * @author Rim Dallali
 */
public class App {
    
    public static void main(String[]args) throws Exception{
        // getting temperature and humidity data
        System.out.println("Capturing temperature and humidity data...");
        DHTSensor dht_sensor = new DHTSensor();
        dht_sensor.startThread();
        
        // getting doorbell data if pressed
        System.out.println("getting doorbell data if pressed...");
        DoorbellButton doorbell_button = new DoorbellButton();
        doorbell_button.startThread();
        
        // getting doorbell data if pressed
        System.out.println("getting motion sensor data if detected...");
        MotionSensor motion_sensor = new MotionSensor();
        motion_sensor.startThread();
    }
}
