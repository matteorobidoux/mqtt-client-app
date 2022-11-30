package datacomproject.mqttclientapp.JavaFX;
//package datacomproject.mqttclientapp.mqtt;

import eu.hansolo.tilesfx.*;
import eu.hansolo.tilesfx.Tile.*;
import eu.hansolo.tilesfx.TileBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import javafx.application.Platform;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.*;

/**
 * @author Ray Hernaez
 */

/* Class to Create the GUI with the help of TilesFX library */
public class FXScreen extends HBox {
	// Constructor
	public FXScreen() throws IOException {
		// Build the screen
		this.buildScreen();
	}

	// Build the screen
	private void buildScreen() throws IOException {

		Row row1 = new Row("rim");
		Row row2 = new Row("matteo");
		Row row3 = new Row("ray");

		// Setup a tile with an exit button to end the application
		Button exitButton = new Button("Exit");
		// Setup event handler for the exit button
		exitButton.setOnAction(e -> endApplication());
		// Setup the tile
		Tile exitTile = TileBuilder.create()
				.skinType(SkinType.CUSTOM)
				.prefSize(150, 300)
				.textSize(TextSize.BIGGER)
				.title("Quit the application")
				.graphic(exitButton)
				.build();

		///////////////////////////////////////////////
		VBox tempTilesColumn = new VBox(row1.tempTimeTilesColumn, row3.tempTimeTilesColumn, row3.tempTimeTilesColumn);
		tempTilesColumn.setMinWidth(300);
		tempTilesColumn.setSpacing(5);

		VBox humidtyTilesColumn = new VBox(row1.humidityTimeTilesColumn, row2.humidityTimeTilesColumn,
				row3.humidityTimeTilesColumn);
		humidtyTilesColumn.setMinWidth(300);
		humidtyTilesColumn.setSpacing(5);

		VBox imageTilesColumn = new VBox(row1.imageTile, row2.imageTile, row3.imageTile);
		imageTilesColumn.setMinWidth(300);
		imageTilesColumn.setSpacing(5);
		VBox doorbellTilesColumn = new VBox(row1.doorbellTile, row2.doorbellTile, row3.doorbellTile);
		doorbellTilesColumn.setMinWidth(300);
		doorbellTilesColumn.setSpacing(5);
		
		this.getChildren().addAll(tempTilesColumn, humidtyTilesColumn, imageTilesColumn, doorbellTilesColumn, exitTile);
		this.setSpacing(5);
	}

	private void updateGUI() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				boolean running = true;
				Random rand = new Random();
				while (running = true) {
					final int temp = rand.nextInt(10);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Platform.runLater(new Runnable() {
						@Override
						public void run() {

						}

					});
				}
			}
		});
	}

	// Stop the threads and close the application
	private void endApplication() {
		Platform.exit();
	}
}