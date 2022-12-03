package datacomproject.mqttclientapp.sensors;

import datacomproject.mqttclientapp.JavaFX.FXScreen;
import datacomproject.mqttclientapp.mqtt.MQTT;

import java.security.PrivateKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import javafx.application.Platform;

public class DHTSensor extends AbstractSensor {

	private final String PROGRAMPATH = "src/main/Python/DHT11.py";

	/**
	 * Starts the thread to retrieve sensor data from DHT
	 * 
	 * @param fxScreen
	 * @param mqtt
	 * @param privateKey
	 */
	public void startThread(FXScreen fxScreen, MQTT mqtt, PrivateKey privateKey) {
		Thread dhtThread = new Thread(() -> {
			while (true) {
				try {
					String output = callProcess(PROGRAMPATH);
					double humidity = Double.parseDouble(output.split(" ")[0]);
					double temperature = Double.parseDouble(output.split(" ")[1]);
					Date timeStamp = new Date();
					DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
					String strDate = dateFormat.format(timeStamp);

					publishData(humidity, temperature, strDate, mqtt, privateKey);
					Thread.sleep(2000);
				} catch (InterruptedException ex) {
					System.err.println("dht thread got interrupted");
				}
			}
		});

		// Start the thread
		dhtThread.start();
	}

	/**
	 * Publish the retrieved sensor data to mqtt
	 * 
	 * @param humidity
	 * @param temperature
	 * @param strDate
	 * @param mqtt
	 * @param privateKey
	 */
	private void publishData(double humidity, double temperature, String strDate, MQTT mqtt, PrivateKey privateKey) {
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
	}
}
