package datacomproject.mqttclientapp.sensors;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import datacomproject.mqttclientapp.Camera.CameraApp;
import datacomproject.mqttclientapp.JavaFX.FXScreen;
import datacomproject.mqttclientapp.mqtt.MQTT;
import javafx.application.Platform;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.Date;

import org.json.JSONObject;

/**
 *
 * @author Rim Dallali
 */
public class MotionSensor extends AbstractSensor {

    private final String programPath = "src/main/Python/SenseLED.py";

    public void startThread(FXScreen fxScreen, MQTT mqtt, PrivateKey privateKey) {
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
                                    try {
                                        Path userHome = Paths.get(System.getProperty("user.home"));
                                        String imageDirPath = userHome + "/Pictures";
                                        String imageAbsPath = getLatestFilefromDir(imageDirPath);
                                        if (imageAbsPath != null) {
                                            Path imagePath = Paths.get(imageAbsPath);
                                            byte[] imageBytes = Files.readAllBytes(imagePath);
                                            JSONObject imageObject = new JSONObject();
                                            imageObject.put("image", new String(Base64.getEncoder().encode(imageBytes), "UTF-8"));
                                            mqtt.publishDataMessage(privateKey, "image", imageObject);
                                        }
                                    } catch (Exception e) {}
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

    private String getLatestFilefromDir(String dirPath) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }

        File lastModifiedFile = files[0];
        for (int i = 1; i < files.length; i++) {
            if (lastModifiedFile.lastModified() < files[i].lastModified()) {
                lastModifiedFile = files[i];
            }
        }
        System.out.println(lastModifiedFile.getName());
        return lastModifiedFile.getAbsolutePath();
    }
}
