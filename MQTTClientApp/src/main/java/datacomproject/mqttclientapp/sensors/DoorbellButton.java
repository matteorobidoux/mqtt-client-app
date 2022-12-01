package datacomproject.mqttclientapp.sensors;

import datacomproject.mqttclientapp.JavaFX.FXScreen;
import javafx.application.Platform;

import java.util.Date;

public class DoorbellButton extends AbstractSensor {

	private final String programPath = "src/main/Python/Doorbell.py";

	/**
	 * Starts the thread to retrieve sensor data from the doorbell
	 * 
	 * @param fxScreen
	 */
	public void startThread(FXScreen fxScreen) {
		Thread doorbellThread = new Thread(() -> {
			boolean buttonState = false;
			while (true) {
				try {
					String output = callProcess(programPath);
					if (output.equals("on") && !buttonState) {
						buttonState = true;
						Date timeStamp = new Date();
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								fxScreen.row1.updateDoorbell(timeStamp);
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

		// Start the thread
		doorbellThread.start();
	}
}
