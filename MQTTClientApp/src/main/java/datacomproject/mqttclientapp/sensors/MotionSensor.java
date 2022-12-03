package datacomproject.mqttclientapp.sensors;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import datacomproject.mqttclientapp.Camera.CameraApp;
import datacomproject.mqttclientapp.JavaFX.FXScreen;
import datacomproject.mqttclientapp.mqtt.MQTT;
import javafx.application.Platform;

import java.io.File;
import java.nio.file.*;
import java.security.PrivateKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import org.json.JSONObject;

public class MotionSensor extends AbstractSensor {

	private final String PROGRAMPATH = "src/main/Python/SenseLED.py";

	/**
	 * Starts the thread to retrieve sensor data from motion sensor
	 * 
	 * @param fxScreen
	 * @param mqtt
	 * @param privateKey
	 */
	public void startThread(FXScreen fxScreen, MQTT mqtt, PrivateKey privateKey) {
		Thread motionThread = new Thread(() -> {
			boolean motionState = false;
			while (true) {
				try {
					String output = callProcess(PROGRAMPATH);
					if (output.equals("on") && !motionState) {
						motionState = true;
						Date timeStamp = new Date();
						DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String strDate = dateFormat.format(timeStamp);

						Thread cameraThread = new Thread(() -> {
							Context pi4j = Pi4J.newAutoContext();

							CameraApp runApp = new CameraApp();
							runApp.execute(pi4j);
							publishData(strDate, mqtt, privateKey);
							// Shutdown Pi4J
							pi4j.shutdown();
						});

						// Start the thread
						cameraThread.start();
					} else if (output.equals("off") && motionState) {
						motionState = false;
					}
				} catch (Exception ex) {
					System.err.println("motion sensor thread got interrupted");
				}
			}
		});
		// Start the thread
		motionThread.start();
	}

	/**
	 * Publish the retrieved sensor data to mqtt
	 * 
	 * @param strDate
	 * @param mqtt
	 * @param privateKey
	 */
	private void publishData(String strDate, MQTT mqtt, PrivateKey privateKey) {
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
						imageObject.put("motionTimestamp", strDate);
						mqtt.publishDataMessage(privateKey, "image", imageObject);
					}
				} catch (Exception e) {
				}
			}
		});
	}

	/**
	 * Get the latest file from the given directory
	 * 
	 * @param dirPath
	 * @return String
	 */
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
		return lastModifiedFile.getAbsolutePath();
	}
}
