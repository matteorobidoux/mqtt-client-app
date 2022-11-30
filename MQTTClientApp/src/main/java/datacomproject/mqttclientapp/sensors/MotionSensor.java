package datacomproject.mqttclientapp.sensors;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import datacomproject.mqttclientapp.Camera.CameraApp;
import datacomproject.mqttclientapp.JavaFX.FXScreen;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import javax.imageio.*;

/**
 *
 * @author Rim Dallali
 */
public class MotionSensor extends AbstractSensor {

    private final String programPath = "src/main/Python/SenseLED.py";

    public void startThread(FXScreen fxScreen) {
        Thread motionThread = new Thread(() -> {
            boolean motionState = false;
            while (true) {
                try {
                    String output = callProcess(programPath);
                    if (output.equals("on") && !motionState) {
                        motionState = true;
                        Date timeStamp = new Date();
                        System.out.println("!!Motion Detected!! --> " + timeStamp);

                        Thread cameraThread = new Thread(() -> {
                            Context pi4j = Pi4J.newAutoContext();

                            CameraApp runApp = new CameraApp();
                            runApp.execute(pi4j);
                            try {
                                Path imagePath = Paths.get("./image.txt");
                                byte[] imageBytes = Files.readAllBytes(imagePath);
                                InputStream targetStream = new ByteArrayInputStream(imageBytes);
                                
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            // Shutdown Pi4J
                            pi4j.shutdown();
                        });

                        // Start the thread
                        cameraThread.start();
                    } else if (output.equals("off") && motionState) {
                        motionState = false;
                    }
                } catch (Exception ex) {
                    System.err.println("exampleThread thread got interrupted");
                }
            }
        });
        // Start the thread
        motionThread.start();
    }
}
