package datacomproject.mqttclientapp.sensors;

import datacomproject.mqttclientapp.JavaFX.FXScreen;
import datacomproject.mqttclientapp.mqtt.MQTT;

import java.util.Date;
import javafx.application.Platform;

public class DHTSensor extends AbstractSensor {

	private final String programPath = "src/main/Python/DHT11.py";
	public MQTT mqtt = new MQTT();

	/**
	 * Starts the thread to retrieve sensor data from DHT
	 * 
	 * @param fxScreen
	 */
	public void startThread(FXScreen fxScreen) {
		Thread dhtThread = new Thread(() -> {
			while (true) {
				try {
					String output = callProcess(programPath);
					double humidity = Double.parseDouble(output.split(" ")[0]);
					double temperature = Double.parseDouble(output.split(" ")[1]);
					Date timeStamp = new Date();
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							fxScreen.row1.updateDHT(temperature, humidity, timeStamp);
						}
					});
					// Delay 2 seconds
					Thread.sleep(2000);
				} catch (InterruptedException ex) {
					System.err.println("exampleThread thread got interrupted");
				}
			}
		});

		// Start the thread
		dhtThread.start();
	}
}
