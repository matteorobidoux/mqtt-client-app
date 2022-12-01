package datacomproject.mqttclientapp.sensors;

import datacomproject.mqttclientapp.JavaFX.FXScreen;
import datacomproject.mqttclientapp.mqtt.MQTT;

import java.security.PrivateKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import javafx.application.Platform;

/**
 *
 * @author Rim Dallali
 */
public class DHTSensor extends AbstractSensor {
    
    private final String programPath = "src/main/Python/DHT11.py";

    public void startThread(FXScreen fxScreen, MQTT mqtt, PrivateKey privateKey) {
        Thread dhtThread = new Thread(() -> {
            while (true) {
                try {
                    String output = callProcess(programPath);
                    double humidity = Double.parseDouble(output.split(" ")[0]);
                    double temperature = Double.parseDouble(output.split(" ")[1]);
                    Date timeStamp = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
                    String strDate = dateFormat.format(timeStamp);  
                    
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject dhtObject = new JSONObject();
                            dhtObject.put("humidity", humidity);
                            dhtObject.put("temperature", temperature); 
                            dhtObject.put("timestamp", strDate);

                            try {
                                mqtt.publishDataMessage(privateKey, "dht", dhtObject);
                            } catch (Exception e) {
                                System.out.println("Error");
                            }
                        }
                    });
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    System.err.println("exampleThread thread got interrupted");
                }
            }
        });

        //Start the thread
        dhtThread.start();
    }
}
