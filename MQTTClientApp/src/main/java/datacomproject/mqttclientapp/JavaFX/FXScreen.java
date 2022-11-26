package datacomproject.mqttclientapp.JavaFX;
//package datacomproject.mqttclientapp.mqtt;

import eu.hansolo.tilesfx.*;
import eu.hansolo.tilesfx.Tile.*;
import eu.hansolo.tilesfx.TileBuilder;
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
    
    //Constructor 
    public FXScreen() throws IOException {
        //Build the screen
        this.buildScreen();
    }
    
    //Build the screen
    private void buildScreen() throws IOException {

        // Generate timestamps
        Date timeStampDHT = new Date();
        Date timeStampMotion = new Date();
        Date timeStampDoorbell = new Date();
        
        // Encoded Images (base64 string)
        String encodedImageRay = "";
        String encodedImageRim = "";
        String encodedImageMatteo = "";
        
        // Temperature reading - Ray
        Tile tempTileRay = TileBuilder.create()
                       .skinType(SkinType.GAUGE)
                       .prefSize(300, 150)
                       .title("Ray's Temperature")
                       .unit("°C")
                       .threshold(100)
                       .build();
        
        // TextArea to display the temperature timestamp - Ray
        TextArea textAreaTempRay = new TextArea();
        textAreaTempRay.setEditable(false);
        textAreaTempRay.setStyle("-fx-control-inner-background: #2A2A2A; "
                + "-fx-text-inner-color: white;"
                + "-fx-text-box-border: transparent;");
        textAreaTempRay.setText("\n\nTaken at: \n" + timeStampDHT);
        VBox textAreaTempVBoxRay = new VBox(textAreaTempRay);
        
        // Temperature Timestamp - Ray
        Tile tempTimeTileRay = TileBuilder.create()
                .skinType(SkinType.CUSTOM)
                .prefSize(300, 150)
                .textSize(TextSize.BIGGER)
                .title("Ray's Temperature")
                .graphic(textAreaTempVBoxRay)
                .build();
        
        // Humidity reading - Ray
        Tile humidityTileRay = TileBuilder.create()
                        .skinType(SkinType.PERCENTAGE)
                        .prefSize(300, 150)
                        .title("Ray's Humidity")
                        .unit("%")
                        .maxValue(100)
                        .text("Taken at: " + timeStampDHT)
                        .build();
        
        // TextArea to display the humidity timestamp - Ray
        TextArea textAreaHumidityRay = new TextArea();
        textAreaHumidityRay.setEditable(false);
        textAreaHumidityRay.setStyle("-fx-control-inner-background: #2A2A2A; "
                + "-fx-text-inner-color: white;"
                + "-fx-text-box-border: transparent;");
        textAreaHumidityRay.setText("\n\nTaken at: \n" + timeStampDHT);
        VBox textAreaHumidityVBoxRay = new VBox(textAreaHumidityRay);
        
        // Temperature Timestamp - Ray
        Tile humidityTimeTileRay = TileBuilder.create()
                .skinType(SkinType.CUSTOM)
                .prefSize(300, 150)
                .textSize(TextSize.BIGGER)
                .title("Ray's Humidity")
                .graphic(textAreaHumidityVBoxRay)
                .build();
        
        // Image taken after motion was detected - Ray
        Tile imageTileRay = TileBuilder.create()
                .skinType(SkinType.IMAGE)
                .prefSize(300, 300)
                .title("Ray's Motion Detected Image")
                .image(new Image(encodedImageRay)) //add imagePath string
                .imageMask(ImageMask.ROUND)
                .text("Taken at: " + timeStampMotion)
                .build();
        
        // TextArea to display the doorbell timestamp - Ray
        TextArea textAreaRay = new TextArea();
        textAreaRay.setEditable(false);
        textAreaRay.setStyle("-fx-control-inner-background: #2A2A2A; "
                + "-fx-text-inner-color: white;"
                + "-fx-text-box-border: transparent;");
        textAreaRay.setText("\n\nDoorbell pressed at: \n" + timeStampDoorbell);
        VBox textAreaVBoxRay    = new VBox(textAreaRay);
        
        // Doorbell Buzzer (Date and Time display) - Ray
        Tile doorbellTileRay = TileBuilder.create()
                .skinType(SkinType.CUSTOM)
                .prefSize(300, 300)
                .textSize(TextSize.BIGGER)
                .title("Ray's Doorbell Buzzer")
                .graphic(textAreaVBoxRay)
                .build();
        
        //
        // Temperature reading - Rim
        Tile tempTileRim = TileBuilder.create()
                       .skinType(SkinType.GAUGE)
                       .prefSize(300, 150)
                       .title("Rim's Temperature")
                       .unit("°C")
                       .threshold(100)
                       .text("Taken at: " + timeStampDHT)
                       .build();
        
        // TextArea to display the temperature timestamp - Rim
        TextArea textAreaTempRim = new TextArea();
        textAreaTempRim.setEditable(false);
        textAreaTempRim.setStyle("-fx-control-inner-background: #2A2A2A; "
                + "-fx-text-inner-color: white;"
                + "-fx-text-box-border: transparent;");
        textAreaTempRim.setText("\n\nTaken at: \n" + timeStampDHT);
        VBox textAreaTempVBoxRim = new VBox(textAreaTempRim);
        
        // Temperature Timestamp - Rim
        Tile tempTimeTileRim = TileBuilder.create()
                .skinType(SkinType.CUSTOM)
                .prefSize(300, 150)
                .textSize(TextSize.BIGGER)
                .title("Rim's Temperature")
                .graphic(textAreaTempVBoxRim)
                .build();
        
        // Humidity reading - Rim
        Tile humidityTileRim = TileBuilder.create()
                        .skinType(SkinType.PERCENTAGE)
                        .prefSize(300, 150)
                        .title("Rim's Humidity")
                        .unit("%")
                        .maxValue(100)
                        .text("Taken at: " + timeStampDHT)
                        .build();
        
        // TextArea to display the humidity timestamp - Rim
        TextArea textAreaHumidityRim = new TextArea();
        textAreaHumidityRim.setEditable(false);
        textAreaHumidityRim.setStyle("-fx-control-inner-background: #2A2A2A; "
                + "-fx-text-inner-color: white;"
                + "-fx-text-box-border: transparent;");
        textAreaHumidityRim.setText("\n\nTaken at: \n" + timeStampDHT);
        VBox textAreaHumidityVBoxRim = new VBox(textAreaHumidityRim);
        
        // Temperature Timestamp - Rim
        Tile humidityTimeTileRim = TileBuilder.create()
                .skinType(SkinType.CUSTOM)
                .prefSize(300, 150)
                .textSize(TextSize.BIGGER)
                .title("Rim's Humidity")
                .graphic(textAreaHumidityVBoxRim)
                .build();
        
        // Image taken after motion was detected - Rim
        Tile imageTileRim = TileBuilder.create()
                .skinType(SkinType.IMAGE)
                .prefSize(300, 300)
                .title("Rim's Motion Detected Image")
                .image(new Image(encodedImageRim)) //add imagePath string
                .imageMask(ImageMask.ROUND)
                .text("Taken at: " + timeStampMotion)
                .build();
        
        // TextArea to display the doorbell timestamp - Rim
        TextArea textAreaRim = new TextArea();
        textAreaRim.setEditable(false);
        textAreaRim.setStyle("-fx-control-inner-background: #2A2A2A; "
                + "-fx-text-inner-color: white;"
                + "-fx-text-box-border: transparent;");
        textAreaRim.setText("\n\nDoorbell pressed at: \n" + timeStampDoorbell);
        VBox textAreaVBoxRim    = new VBox(textAreaRim);
        
        // Doorbell Buzzer (Date and Time display) - Rim
        Tile doorbellTileRim = TileBuilder.create()
                .skinType(SkinType.CUSTOM)
                .prefSize(300, 300)
                .textSize(TextSize.BIGGER)
                .title("Rim's Doorbell Buzzer")
                .graphic(textAreaVBoxRim)
                .build();
        
        //
        // Temperature reading - Matteo
        Tile tempTileMatteo = TileBuilder.create()
                       .skinType(SkinType.GAUGE)
                       .prefSize(300, 150)
                       .title("Matteo's Temperature")
                       .unit("°C")
                       .threshold(100)
                       .text("Taken at: " + timeStampDHT)
                       .build();
        
        // TextArea to display the temperature timestamp - Matteo
        TextArea textAreaTempMatteo = new TextArea();
        textAreaTempMatteo.setEditable(false);
        textAreaTempMatteo.setStyle("-fx-control-inner-background: #2A2A2A; "
                + "-fx-text-inner-color: white;"
                + "-fx-text-box-border: transparent;");
        textAreaTempMatteo.setText("\n\nTaken at: \n" + timeStampDHT);
        VBox textAreaTempVBoxMatteo = new VBox(textAreaTempMatteo);
        
        // Temperature Timestamp - Matteo
        Tile tempTimeTileMatteo = TileBuilder.create()
                .skinType(SkinType.CUSTOM)
                .prefSize(300, 150)
                .textSize(TextSize.BIGGER)
                .title("Matteo's Temperature")
                .graphic(textAreaTempVBoxMatteo)
                .build();
        
        // Humidity reading - Matteo
        Tile humidityTileMatteo = TileBuilder.create()
                        .skinType(SkinType.PERCENTAGE)
                        .prefSize(300, 150)
                        .title("Matteo's Humidity")
                        .unit("%")
                        .maxValue(100)
                        .text("Taken at: " + timeStampDHT)
                        .build();
        
        // TextArea to display the humidity timestamp - Matteo
        TextArea textAreaHumidityMatteo = new TextArea();
        textAreaHumidityMatteo.setEditable(false);
        textAreaHumidityMatteo.setStyle("-fx-control-inner-background: #2A2A2A; "
                + "-fx-text-inner-color: white;"
                + "-fx-text-box-border: transparent;");
        textAreaHumidityMatteo.setText("\n\nTaken at: \n" + timeStampDHT);
        VBox textAreaHumidityVBoxMatteo = new VBox(textAreaHumidityMatteo);
        
        // Temperature Timestamp - Matteo
        Tile humidityTimeTileMatteo = TileBuilder.create()
                .skinType(SkinType.CUSTOM)
                .prefSize(300, 150)
                .textSize(TextSize.BIGGER)
                .title("Matteo's Humidity")
                .graphic(textAreaHumidityVBoxMatteo)
                .build();
        
        // Image taken after motion was detected - Matteo
        Tile imageTileMatteo = TileBuilder.create()
                .skinType(SkinType.IMAGE)
                .prefSize(300, 300)
                .title("Matteo's Motion Detected Image")
                .image(new Image(encodedImageMatteo)) //add imagePath string
                .imageMask(ImageMask.ROUND)
                .text("Taken at: " + timeStampMotion)
                .build();
        
        // TextArea to display the doorbell timestamp - Matteo
        TextArea textAreaMatteo = new TextArea();
        textAreaMatteo.setEditable(false);
        textAreaMatteo.setStyle("-fx-control-inner-background: #2A2A2A; "
                + "-fx-text-inner-color: white;"
                + "-fx-text-box-border: transparent;");
        textAreaMatteo.setText("\n\nDoorbell pressed at: \n" + timeStampDoorbell);
        VBox textAreaVBoxMatteo    = new VBox(textAreaMatteo);
        
        // Doorbell Buzzer (Date and Time display) - Matteo
        Tile doorbellTileMatteo = TileBuilder.create()
                .skinType(SkinType.CUSTOM)
                .prefSize(300, 300)
                .textSize(TextSize.BIGGER)
                .title("Matteo's Doorbell Buzzer")
                .graphic(textAreaVBoxMatteo)
                .build();
        
        //Setup a tile with an exit button to end the application
        var exitButton = new Button("Exit");

        //Setup event handler for the exit button
        exitButton.setOnAction(e -> endApplication());

        //Setup the tile
        var exitTile = TileBuilder.create()
                .skinType(SkinType.CUSTOM)
                .prefSize(150, 300)
                .textSize(TextSize.BIGGER)
                .title("Quit the application")
                .graphic(exitButton)
                .build();
        
        //
        //Add the tiles to VBoxes
        VBox tempTimeTilesColumn = new VBox(tempTileRay, tempTimeTileRay);
        tempTimeTilesColumn.setMinWidth(300);
        
        VBox tempTimeTilesColumn2 = new VBox(tempTileRim, tempTimeTileRim);
        tempTimeTilesColumn2.setMinWidth(300);
        
        VBox tempTimeTilesColumn3 = new VBox(tempTileMatteo, tempTimeTileMatteo);
        tempTimeTilesColumn3.setMinWidth(300);
        
        VBox humidityTimeTilesColumn = new VBox(humidityTileRay, humidityTimeTileRay);
        humidityTimeTilesColumn.setMinWidth(300);
        
        VBox humidityTimeTilesColumn2 = new VBox(humidityTileRim, humidityTimeTileRim);
        humidityTimeTilesColumn2.setMinWidth(300);
        
        VBox humidityTimeTilesColumn3 = new VBox(humidityTileMatteo, humidityTimeTileMatteo);
        humidityTimeTilesColumn3.setMinWidth(300);
                
        VBox tempTilesColumn = new VBox(tempTimeTilesColumn, tempTimeTilesColumn2, tempTimeTilesColumn3);
        tempTilesColumn.setMinWidth(300);
        tempTilesColumn.setSpacing(5);

        VBox humidtyTilesColumn = new VBox(humidityTimeTilesColumn, humidityTimeTilesColumn2, humidityTimeTilesColumn3);
        humidtyTilesColumn.setMinWidth(300);
        humidtyTilesColumn.setSpacing(5);
        
        VBox imageTilesColumn = new VBox(imageTileRay, imageTileRim, imageTileMatteo);
        imageTilesColumn.setMinWidth(300);
        imageTilesColumn.setSpacing(5);
        
        VBox doorbellTilesColumn = new VBox(doorbellTileRay, doorbellTileRim, doorbellTileMatteo);
        doorbellTilesColumn.setMinWidth(300);
        doorbellTilesColumn.setSpacing(5);
        
        
        //Add the VBoxes to the root layout, which is a HBox
        this.getChildren().addAll(tempTilesColumn, humidtyTilesColumn, imageTilesColumn, doorbellTilesColumn, exitTile);
        this.setSpacing(5);
        
    }
    
    //Stop the threads and close the application
    private void endApplication() {
        Platform.exit();
    }
    
}


