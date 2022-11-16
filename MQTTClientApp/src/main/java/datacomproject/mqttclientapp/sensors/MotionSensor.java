package datacomproject.mqttclientapp.sensors;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import datacomproject.mqttclientapp.mqtt.Camera.CameraApp;
import java.io.IOException;

/**
 *
 * @author Rim Dallali
 */
public class MotionSensor {

    private final String programPath = "src/main/Python/SenseLED.py";

    public void startThread() {
        Thread exampleThread = new Thread(() -> {
            boolean buttonState = false;
            while (true) {
                try {
                    String output = callProcess();
                    if (output.equals("on") && !buttonState) {
                        buttonState = true;
                        
                        Thread motionThread = new Thread(() -> {
                            //CODE FOR CAMERA START => NOT SURE IF FUNCTIONAL
                            Context pi4j = Pi4J.newAutoContext();

                            CameraApp runApp = new CameraApp();
                            runApp.execute(pi4j);

                            // Shutdown Pi4J
                            pi4j.shutdown();
                            //CODE FOR CAMERA END
                        });

                        //Start the thread
                        motionThread.start();
                    } else if (output.equals("off") && buttonState) {
                        System.out.println("!!Motion Detected!!");
                        buttonState = false;
                    }
                    
                } catch (Exception ex) {
                    System.err.println("exampleThread thread got interrupted");
                }
            }
        });

        //Start the thread
        exampleThread.start();
    }

    private String callProcess() {
        String output = "";
        try {
            MyProcessBuilder myProcessBuilder = new MyProcessBuilder(programPath);
            output = myProcessBuilder.startProcess();

        } catch (IOException e) {
            System.err.println("startProcess failed");
        }
        return output;
    }
}
