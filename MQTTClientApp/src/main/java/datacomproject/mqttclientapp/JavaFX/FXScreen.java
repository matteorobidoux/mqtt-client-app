package datacomproject.mqttclientapp.JavaFX;
//package datacomproject.mqttclientapp.mqtt;

import eu.hansolo.tilesfx.*;
import eu.hansolo.tilesfx.Tile.*;
import eu.hansolo.tilesfx.TileBuilder;
import java.io.IOException;
import java.util.*;
import javafx.application.Platform;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.control.*;
import java.util.logging.*;
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
    
//        //Custom Tile containing a TextField
//        Label input = new Label("Input");
//        input.setTextFill(Color.WHITE);
//        TextField textfield = new TextField();
//        Button tfButton = new Button("Submit");
//        Button clearButton = new Button("Clear");
//
//        //Event handlers to get the input
//        tfButton.setOnAction(e -> System.out.println("You entered: " + textfield.getText()));
//        clearButton.setOnAction(e -> textfield.clear());
//
//        //Layout to contain the TextField and Buttons
//        GridPane tfContainer = new GridPane();
//
//        //Add space between the columns of the GridPane
//        tfContainer.setHgap(5);
//
//        //Add space between the rows of the GridPane
//        tfContainer.setVgap(5);
//
//        //Add the nodes to the GridPane
//        tfContainer.add(input, 1, 4);
//        tfContainer.add(textfield, 2, 4);
//        tfContainer.add(clearButton, 1, 5);
//        tfContainer.add(tfButton, 3, 5);

        // Generate timestamps
        Date timeStampDHT = new Date();
        Date timeStampMotion = new Date();
        Date timeStampDoorbell = new Date();
        
        // Image Paths
        String imagePathRay = "";
        String imagePathRim = "";
        String imagePathMatteo = "";
        
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
                .image(new Image(FXScreen.class.getResourceAsStream(imagePathRay))) //add imagePath string
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
                .image(new Image(FXScreen.class.getResourceAsStream(imagePathRim))) //add imagePath string
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
                .image(new Image(FXScreen.class.getResourceAsStream(imagePathMatteo))) //add imagePath string
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
        
        //
        //Add the tiles to VBoxes
        VBox tempTimeTilesColumn = new VBox(tempTileRay, tempTimeTileRay);
        tempTimeTilesColumn.setMinWidth(300);
        tempTimeTilesColumn.setSpacing(5);
        
        VBox tempTimeTilesColumn2 = new VBox(tempTileRim, tempTimeTileRim);
        tempTimeTilesColumn2.setMinWidth(300);
        tempTimeTilesColumn2.setSpacing(5);
        
        VBox tempTimeTilesColumn3 = new VBox(tempTileMatteo, tempTimeTileMatteo);
        tempTimeTilesColumn3.setMinWidth(300);
        tempTimeTilesColumn3.setSpacing(5);
        
        VBox humidityTimeTilesColumn = new VBox(humidityTileRay, humidityTimeTileRay);
        humidityTimeTilesColumn.setMinWidth(300);
        humidityTimeTilesColumn.setSpacing(5);
        
        VBox humidityTimeTilesColumn2 = new VBox(humidityTileRim, humidityTimeTileRim);
        humidityTimeTilesColumn2.setMinWidth(300);
        humidityTimeTilesColumn2.setSpacing(5);
        
        VBox humidityTimeTilesColumn3 = new VBox(humidityTileMatteo, humidityTimeTileMatteo);
        humidityTimeTilesColumn3.setMinWidth(300);
        humidityTimeTilesColumn3.setSpacing(5);
                
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
        this.getChildren().addAll(tempTilesColumn, humidtyTilesColumn, imageTilesColumn, doorbellTilesColumn);
        this.setSpacing(5);
        
    }
    
    //Stop the threads and close the application
    private void endApplication() {
        Platform.exit();
    }
    
}


///* Class to Create the GUI with the help of TilesFX library */
//public class FXScreen extends HBox {
//    private static boolean running = true;
//    private Tile gaugeTile;
//    private Tile percentageTile;
//    private final String programPath = "src/main/Python/21.1.1_DHT11/DHT11.py";
////    private final MyProcessBuilder myProcessBuilder = new MyProcessBuilder(programPath);
//    
//
//    //Constructor 
//    public FXScreen() throws IOException {
//        //Start the thread
//        this.startThread();
//        
//        //Build the screen
//        this.buildScreen();
//    }
//
//    //Build the screen
//    private void buildScreen() throws IOException {
//
//        // Define our local setting (used by the clock)
//        Locale locale = new Locale("en", "CA");
//
//        //Custom Tile containing a TextField
//        Label input = new Label("Input");
//        input.setTextFill(Color.WHITE);
//        TextField textfield = new TextField();
//        Button tfButton = new Button("Submit");
//        Button clearButton = new Button("Clear");
//
//        //Event handlers to get the input
//        tfButton.setOnAction(e -> System.out.println("You entered: " + textfield.getText()));
//        clearButton.setOnAction(e -> textfield.clear());
//
//        //Layout to contain the TextField and Buttons
//        GridPane tfContainer = new GridPane();
//
//        //Add space between the columns of the GridPane
//        tfContainer.setHgap(5);
//
//        //Add space between the rows of the GridPane
//        tfContainer.setVgap(5);
//
//        //Add the nodes to the GridPane
//        tfContainer.add(input, 1, 4);
//        tfContainer.add(textfield, 2, 4);
//        tfContainer.add(clearButton, 1, 5);
//        tfContainer.add(tfButton, 3, 5);
//
//        // LAB: A clock tile showing the current date and time
//        Tile clockTile = TileBuilder.create()
//                .skinType(SkinType.CLOCK)
//                .prefSize(350, 300)
//                .title("Clock Tile")
//                .dateVisible(true)
//                .locale(locale)
//                .running(true)
//                .build();
//        
//        // LAB: A gauge tile to display temperature reading obtained from the DHT11 sensor
//        this.gaugeTile = TileBuilder.create()
//                       .skinType(SkinType.GAUGE)
//                       .prefSize(350, 300)
//                       .title("Gauge Tile")
//                       .unit("Degrees")
//                       .threshold(100)
//                       .build();
//        
//        // LAB: A percentage tile to display humidity data obtained from the DHT11 sensor
//        this.percentageTile = TileBuilder.create()
//                        .skinType(SkinType.PERCENTAGE)
//                        .prefSize(350, 300)
//                        .title("Percentage Tile")
//                        .unit("%")
//                        .description("Humidity")
//                        .maxValue(100)
//                        .build();
//
//        // Setup tile with update button to update output
//        Button updateButton = new Button("Update");
//        updateButton.setOnAction(e -> {
//            UpdateData updateDataObj = new UpdateData();
//            updateDataObj.updateOutput();
//        });
//
//        // Generate a timestamp 
//        Date timeStamp = new Date();
//
//        //A custom tile containing update button and a timestamp
//        Tile updateOutputTile = TileBuilder.create()
//                .skinType(SkinType.CUSTOM)
//                .prefSize(350, 300)
//                .title("Update the output")
//                .textSize(TextSize.BIGGER)
//                .text("Last update date and time: " + timeStamp)
//                .textColor(Color.MIDNIGHTBLUE)
//                .backgroundColor(Color.LIGHTBLUE)
//                .titleColor(Color.BLUE)
//                .graphic(updateButton)
//                .roundedCorners(true)
//                .build();
//
//        /*Text tile to display output from external program */
//        //The command to execute
//        String helloCmd = "src/main/Python/helloWorld.py";//src/main/Python/21.1.1_DHT11/DHT11.py
//
//        //ProcessBuilder object use to run the external command
//        MyProcessBuilder helloProcessBuilder = new MyProcessBuilder(helloCmd);
//        
//
//        //Get the output from the process
//        String theOutput = helloProcessBuilder.startProcess();
//
//        //Generate a timestamp
//        Date timeStamp2 = new Date();
//
//        //Setup tile with TextArea to display output from external program
//        TextArea textArea = new TextArea();
//
//        //Make the TextArea non editable
//        textArea.setEditable(false);
//
//        /*Change the background and the font color of the TextArea
//           and make the border of the TextArea transparent
//         */
//        textArea.setStyle("-fx-control-inner-background: #2A2A2A; "
//                + "-fx-text-inner-color: white;"
//                + "-fx-text-box-border: transparent;");
//
//        //Write output to TextArea
//        textArea.setText("\n\nOutput from external program at" + "\n" + timeStamp2 + ":" + "\n" + theOutput);
//
//        //Layout to contain the TextArea
//        VBox textAreaVbox = new VBox(textArea);
//
//        // LAB: A TextArea tile to display output from the Hello World python program
//        Tile textAreaTile = TileBuilder.create()
//                .skinType(SkinType.CUSTOM)
//                .prefSize(350, 300)
//                .textSize(TextSize.BIGGER)
//                .title("Output from external program to TextArea tile")
//                .graphic(textAreaVbox)
//                .build();
//
//        //Label for the choiceBox
//        Label cbLabel = new Label("Select your choice:  ");
//        cbLabel.setTextFill(Color.WHITE);
//
//        //Setup a tile with an exit button to end the application
//        Button exitButton = new Button("Exit");
//
//        //Setup event handler for the exit button
//        exitButton.setOnAction(e -> endApplication());
//
//        //Setup the tile
//        Tile exitTile = TileBuilder.create()
//                .skinType(SkinType.CUSTOM)
//                .prefSize(350, 300)
//                .textSize(TextSize.BIGGER)
//                .title("Quit the application")
//                .graphic(exitButton)
//                .roundedCorners(false)
//                .build();
//
//        //Add the tiles to VBoxes
//        VBox tilesColumn1 = new VBox(clockTile, gaugeTile);
//        tilesColumn1.setMinWidth(350);
//        tilesColumn1.setSpacing(5);
//
//        VBox tilesColumn2 = new VBox(percentageTile, updateOutputTile);
//        tilesColumn2.setMinWidth(350);
//        tilesColumn2.setSpacing(5);
//
//        VBox tilesColumn3 = new VBox(textAreaTile, exitTile);
//        tilesColumn3.setMinWidth(350);
//        tilesColumn3.setSpacing(5);
//
//        //Add the VBoxes to the root layout, which is a HBox
//        this.getChildren().addAll(tilesColumn1, tilesColumn2, tilesColumn3);
//        this.setSpacing(5);
//    }
//
//    //Setup a thread using Lambda implementation to update timestamp (threaded)
//    private void startThread() {
//        Thread exampleThread = new Thread(() -> {
//            while (running) {
//                try {
//                    //Delay 5 seconds
//                    Thread.sleep(5000);
//                } catch (InterruptedException ex) {
//                    System.err.println("exampleThread thread got interrupted");
//                }
//
//                //Needed to be to update an active node
//                Platform.runLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        String output = callProcess();
//                        double humidity = Double.parseDouble(output.split(" ")[0]);
//                        double temperature = Double.parseDouble(output.split(" ")[1]);
//                        gaugeTile.setValue(temperature);
//                        percentageTile.setValue(humidity);
//                    }
//                });
//            }
//        });
//
//        //Start the thread
//        exampleThread.start();
//    }
//    
//    public String callProcess() {
//        String output="";
//        try{
//            MyProcessBuilder myProcessBuilder = new MyProcessBuilder(programPath);
//            output = myProcessBuilder.startProcess();
//        } catch (IOException e) {
//            System.err.println("startProcess failed");
//        }
//        
//        return output;
//    }
//
//    
//    //Stop the threads and close the application
//    private void endApplication() {
//        this.running = false;
//        Platform.exit();
//    }
//}
