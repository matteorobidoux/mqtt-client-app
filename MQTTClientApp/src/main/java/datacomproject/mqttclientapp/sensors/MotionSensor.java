package datacomproject.mqttclientapp.sensors;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import datacomproject.mqttclientapp.Camera.CameraApp;
import datacomproject.mqttclientapp.JavaFX.FXScreen;
import javafx.application.Platform;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

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
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    // fxScreen.row1.updateDoorbell(timeStamp);
                                    try {
                                        Path imagePath = Paths.get("./image.txt");
                                        byte[] imageBytes = Files.readAllBytes(imagePath);
                                        InputStream targetStream = new ByteArrayInputStream(imageBytes);
                                        fxScreen.row1.updateImage(targetStream);
                                    } catch (IOException e) {
                                        // ignore
                                    }
                                }
                            });
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
