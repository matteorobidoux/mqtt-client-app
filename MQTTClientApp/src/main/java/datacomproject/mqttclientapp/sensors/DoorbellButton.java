package datacomproject.mqttclientapp.sensors;

import datacomproject.mqttclientapp.JavaFX.FXScreen;
import datacomproject.mqttclientapp.mqtt.MQTT;
import javafx.application.Platform;

import java.security.PrivateKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

public class DoorbellButton extends AbstractSensor {

	private final String PROGRAMPATH = "src/main/Python/Doorbell.py";

	/**
	 * Starts the thread to retrieve sensor data from the doorbell
	 * 
	 * @param fxScreen
	 * @param mqtt
	 * @param privateKey
	 */
	public void startThread(FXScreen fxScreen, MQTT mqtt, PrivateKey privateKey) {
		Thread doorbellThread = new Thread(() -> {
			boolean buttonState = false;
			while (true) {
				try {
					String output = callProcess(PROGRAMPATH);
					if (output.equals("on") && !buttonState) {
						buttonState = true;
						Date timeStamp = new Date();
						DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String strDate = dateFormat.format(timeStamp);
						publishData(strDate, mqtt, privateKey);
					} else if (output.equals("off") && buttonState) {
						buttonState = false;
					}

				} catch (Exception ex) {
					System.err.println("exampleThread thread got interrupted");
				}
			}
		});

		// Start the thread
		doorbellThread.start();
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
				JSONObject doorbellObject = new JSONObject();
				doorbellObject.put("doorbell", strDate);
				try {
					mqtt.publishDataMessage(privateKey, "doorbell", doorbellObject);
				} catch (Exception e) {
					System.out.println("Error");
				}
			}
		});
	}
}
