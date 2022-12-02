package datacomproject.mqttclientapp.JavaFX;
//package datacomproject.mqttclientapp.mqtt;

import eu.hansolo.tilesfx.*;
import eu.hansolo.tilesfx.Tile.*;
import eu.hansolo.tilesfx.TileBuilder;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.layout.*;
import javafx.scene.control.*;

/* Class to Create the GUI with the help of TilesFX library */
public class FXScreen extends HBox {

    public Row row1;
    public Row row2;
    public Row row3;

    // Constructor
    public FXScreen() throws IOException {
        row1 = new Row("rimdallali");
        row2 = new Row("matteorobidoux");
        row3 = new Row("carletondavis");
        // Build the screen
        this.buildScreen();
    }

    // Build the screen
    private void buildScreen() throws IOException {
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
        VBox tempTilesColumn = new VBox(row1.tempTimeTilesColumn, row2.tempTimeTilesColumn, row3.tempTimeTilesColumn);
        tempTilesColumn.setMinWidth(300);
        tempTilesColumn.setSpacing(5);

        VBox humidtyTilesColumn = new VBox(row1.humidityTimeTilesColumn, row2.humidityTimeTilesColumn, row3.humidityTimeTilesColumn);
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

    // Stop the threads and close the application
    private void endApplication() {
        Platform.exit();
    }
}