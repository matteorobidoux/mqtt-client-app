package datacomproject.mqttclientapp.JavaFX;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class TilesFXApp extends Application {
    
    //Allows the stage be easily accessible
    public static Stage theStage;
    public static FXScreen fxScreen;
    
    
    @Override
    public void start(Stage stage) throws IOException {
        fxScreen = new FXScreen();
        Scene scene = new Scene(fxScreen); //1060, 910
        TilesFXApp.theStage = stage;
        //Set the active scene
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