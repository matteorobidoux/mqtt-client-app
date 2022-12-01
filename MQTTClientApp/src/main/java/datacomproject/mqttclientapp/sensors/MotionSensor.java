package datacomproject.mqttclientapp.sensors;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import datacomproject.mqttclientapp.Camera.CameraApp;
import datacomproject.mqttclientapp.JavaFX.FXScreen;
import javafx.application.Platform;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class MotionSensor extends AbstractSensor {

	private final String programPath = "src/main/Python/SenseLED.py";

	/**
	 * Starts the thread to retrieve sensor data from motion sensor
	 * 
	 * @param fxScreen
	 */
	public void startThread(FXScreen fxScreen) {
		Thread motionThread = new Thread(() -> {
			boolean motionState = false;
			while (true) {
				try {
					String output = callProcess(programPath);
					if (output.equals("on") && !motionState) {
						motionState = true;
						Date timeStamp = new Date();

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
											InputStream targetStream = new ByteArrayInputStream(imageBytes);
											fxScreen.row1.updateImage(targetStream);
										}
									} catch (IOException e) {
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
