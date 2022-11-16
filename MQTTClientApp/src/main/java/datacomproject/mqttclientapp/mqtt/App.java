/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datacomproject.mqttclientapp.mqtt;

import datacomproject.mqttclientapp.sensors.SensorDHT;
//import datacomproject.mqttclientapp.mqtt.KeyStore.KeyStoreHelper;
import datacomproject.mqttclientapp.sensors.ButtonDoorbell;

/**
 *
 * @author Matteo
 */
public class App {
    
    public static void main(String[]args) throws Exception{
//        KeyStoreHelper ksh = new KeyStoreHelper();
//        ksh.getUserInput();

        // getting temperature and humidity data
//        System.out.println("Capturing temperature and humidity data...");
//        SensorDHT dht_sensor = new SensorDHT();
//        dht_sensor.startThread();
        
        // getting doorbell data if pressed
        System.out.println("getting doorbell data if pressed...");
        ButtonDoorbell doorbell_button = new ButtonDoorbell();
        doorbell_button.startThread();
    }
}
