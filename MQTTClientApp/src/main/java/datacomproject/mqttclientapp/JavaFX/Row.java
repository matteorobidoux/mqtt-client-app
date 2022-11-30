package datacomproject.mqttclientapp.JavaFX;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Scanner;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.Tile.ImageMask;
import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.Tile.TextSize;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

public class Row {
  Date timeStampDHT = new Date();
  Date timeStampMotion = new Date();
  Date timeStampDoorbell = new Date();

  Tile tempTile;
  Tile tempTimeTile;
  Tile humidityTile;
  Tile doorbellTile;
  Tile humidityTimeTile;

  Tile imageTile;
  String encodedImage;

  VBox textAreaTempVBox;
  TextArea textAreaTemp;

  VBox textAreaHumidityVBox;
  TextArea textAreaHumidity;

  TextArea doorbellTextArea;
  VBox doorbellTextAreaVBox;

  VBox humidityTimeTilesColumn;
  VBox tempTimeTilesColumn;

  String username;

  public Row(String username) {
    this.username = username;
    // this.encodedImage = readImage("image");
  }

  public void updateDHT() {

  }

  public void updateImage() {

  }

  public void updateDoorbell() {

  }

  public void buildRow() {
    tempTile = TileBuilder.create()
        .skinType(SkinType.GAUGE)
        .prefSize(300, 150)
        .title(username + "'s Temperature")
        .unit("°C")
        .threshold(100)
        .build();

    // container for timestamp for temperature DHT11 sensors
    TextArea textAreaTemp = new TextArea();
    textAreaTemp.setEditable(false);
    textAreaTemp.setStyle("-fx-control-inner-background: #2A2A2A; "
        + "-fx-text-inner-color: white;"
        + "-fx-text-box-border: transparent;");
    textAreaTemp.setText("\n\nTaken at: \n" + timeStampDHT);
    textAreaTempVBox = new VBox(textAreaTemp);

    tempTimeTile = TileBuilder.create()
        .skinType(SkinType.CUSTOM)
        .prefSize(300, 150)
        .textSize(TextSize.BIGGER)
        .title(username + "'s Temperature")
        .graphic(textAreaTempVBox)
        .build();

    humidityTile = TileBuilder.create()
        .skinType(SkinType.PERCENTAGE)
        .prefSize(300, 150)
        .title(username + "'s Humidity")
        .unit("%")
        .maxValue(100)
        .text("Taken at: " + timeStampDHT)
        .build();

    TextArea textAreaHumidity = new TextArea();
    textAreaHumidity.setEditable(false);
    textAreaHumidity.setStyle("-fx-control-inner-background: #2A2A2A; "
        + "-fx-text-inner-color: white;"
        + "-fx-text-box-border: transparent;");
    textAreaHumidity.setText("\n\nTaken at: \n" + timeStampDHT);
    textAreaHumidityVBox = new VBox(textAreaHumidity);

    humidityTimeTile = TileBuilder.create()
        .skinType(SkinType.CUSTOM)
        .prefSize(300, 150)
        .textSize(TextSize.BIGGER)
        .title(username + "'s Humidity")
        .graphic(textAreaHumidityVBox)
        .build();

    imageTile = TileBuilder.create()
        .skinType(SkinType.IMAGE)
        .prefSize(300, 300)
        .title(username + "'s Motion Detected Image")
        // .image(new Image(encodedImage)) // add imagePath string
        .imageMask(ImageMask.ROUND)
        .text("Taken at: " + timeStampMotion)
        .build();

    doorbellTextArea = new TextArea();
    doorbellTextArea.setEditable(false);
    doorbellTextArea.setStyle("-fx-control-inner-background: #2A2A2A; "
        + "-fx-text-inner-color: white;"
        + "-fx-text-box-border: transparent;");
    doorbellTextArea.setText("\n\nDoorbell pressed at: \n" + timeStampDoorbell);
    VBox doorbellTextAreaVBox = new VBox(doorbellTextArea);

    // Doorbell Buzzer (Date and Time display) -
    doorbellTile = TileBuilder.create()
        .skinType(SkinType.CUSTOM)
        .prefSize(300, 300)
        .textSize(TextSize.BIGGER)
        .title(username + "'s Doorbell Buzzer")
        .graphic(doorbellTextAreaVBox)
        .build();

    tempTimeTilesColumn = new VBox(tempTile, tempTimeTile);
    tempTimeTilesColumn.setMinWidth(300);

    humidityTimeTilesColumn = new VBox(humidityTile, humidityTimeTile);
    humidityTimeTilesColumn.setMinWidth(300);
  }

  private String readImage(String imagePath) {
    try {
      File myObj = new File(imagePath);
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        String data = myReader.nextLine();
        return data;
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    return null;
  }
}
