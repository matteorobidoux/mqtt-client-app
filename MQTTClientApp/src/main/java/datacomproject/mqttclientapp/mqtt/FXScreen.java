//package datacomproject.mqttclientapp.mqtt;
//
//import eu.hansolo.tilesfx.Tile;
//import eu.hansolo.tilesfx.Tile.SkinType;
//import eu.hansolo.tilesfx.Tile.TextSize;
//import eu.hansolo.tilesfx.TileBuilder;
//import java.io.IOException;
//import java.util.*;
//import javafx.application.Platform;
//import javafx.scene.layout.*;
//import javafx.scene.paint.Color;
//import javafx.scene.control.*;
//
///**
// * @author Rim Dallali
// */
//
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
