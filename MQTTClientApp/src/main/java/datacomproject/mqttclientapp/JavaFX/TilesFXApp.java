package datacomproject.mqttclientapp.JavaFX;

import datacomproject.mqttclientapp.ConsoleApp;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * GUI application
 * GUI is only launched once all the console information has been entered
 */
public class TilesFXApp extends Application {

	private Stage theStage;
	public static FXScreen fxScreen;
	private ConsoleApp consoleApp;

	@Override
	public void start(Stage stage) throws IOException {
		fxScreen = new FXScreen();
		consoleApp = new ConsoleApp();

		try {
			consoleApp.initializeKeyStore();
			consoleApp.initializeMQTT();
			consoleApp.captureData(fxScreen);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		Scene scene = new Scene(fxScreen); // 1060, 910
		theStage = stage;
		// Set the active scene
		theStage.setScene(scene);
		theStage.show();

		// Make sure the application quits completely on close
		theStage.setOnCloseRequest(t -> {
			Platform.exit();
			System.exit(0);
		});
	}

	public static void main(String[] args) {
		launch();
	}
}