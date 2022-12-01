package datacomproject.mqttclientapp.sensors;

import datacomproject.mqttclientapp.JavaFX.FXScreen;
import datacomproject.mqttclientapp.mqtt.MQTT;
import javafx.application.Platform;

import java.security.PrivateKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

/**
 *
 * @author Rim Dallali
 */
public class DoorbellButton extends AbstractSensor {

    private final String programPath = "src/main/Python/Doorbell.py";

    public void startThread(FXScreen fxScreen, MQTT mqtt, PrivateKey privateKey) {
        Thread doorbellThread = new Thread(() -> {
            boolean buttonState = false;
            while (true) {
                try {
                    String output = callProcess(programPath);
                    if (output.equals("on") && !buttonState) {
                        buttonState = true;
                        Date timeStamp = new Date();
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
                        String strDate = dateFormat.format(timeStamp);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                JSONObject doorbellObject = new JSONObject();
                                doorbellObject.put("doorbell", strDate);
                                try {
                                    mqtt.publishDataMessage(privateKey, "doorbell", doorbellObject);
                                } catch (Exception e) {
                                   System.out.println("Error");
                                }
                            }
                        });
                    } else if (output.equals("off") && buttonState) {
                        buttonState = false;
                    }

                } catch (Exception ex) {
                    System.err.println("exampleThread thread got interrupted");
                }
            }
        });

        //Start the thread
        doorbellThread.start();
    }
}
