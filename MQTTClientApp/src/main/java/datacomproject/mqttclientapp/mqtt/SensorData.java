/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datacomproject.mqttclientapp.mqtt;

import java.io.IOException;

/**
 *
 * @author Ray Hernaez
 */
public class SensorData {
    
    String BuzzerPath = "Python/Doorbell.py";
    String HumidtyTempPath = "Python/DHT11.py";
    String MotionDetectorPath = "Python/SenseLED.py";
    
    public String getOutputBuzzer() {
        String theCmd = BuzzerPath;
        MyProcessBuilder pBuilder = new MyProcessBuilder(theCmd);
        String output;
        
        try {
            output = pBuilder.startProcess();
            return output;
        } catch(IOException e) {
            return "Error reading buzzer data";
        }
    }
    
    public String getOutputHumidityTemperature() {
        String theCmd = HumidtyTempPath;
        MyProcessBuilder pBuilder = new MyProcessBuilder(theCmd);
        String output;
        
        try {
            output = pBuilder.startProcess();
            return output;
        } catch(IOException e) {
            return "Error reading humidity and temperature data";
        }
    }
    
    public String getOutputMotionDetector() {
        String theCmd = MotionDetectorPath;
        MyProcessBuilder pBuilder = new MyProcessBuilder(theCmd);
        String output;
        
        try {
            output = pBuilder.startProcess();
            return output;
        } catch(IOException e) {
            return "Error reading motion detector data";
        }
    }
    
}
