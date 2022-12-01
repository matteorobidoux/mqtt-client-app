package datacomproject.mqttclientapp.JavaFX;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Date;

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
	InputStream imageInputStream;

	VBox textAreaTempVBox;
	TextArea textAreaTemp;

	VBox textAreaHumidityVBox;
	TextArea textAreaHumidity;

	TextArea doorbellTextArea;
	VBox doorbellTextAreaVBox;

	VBox humidityTimeTilesColumn;
	VBox tempTimeTilesColumn;

	String username;

	public Row(String username) throws IOException {
		this.username = username;
		Path imagePath = Paths.get("./src/loading.png");
		byte[] imageBytes = Files.readAllBytes(imagePath);
		this.imageInputStream = new ByteArrayInputStream(imageBytes);
		this.buildRow();
	}

	/**
	 * Update the DHT sensor tiles with the retrieved data
	 * 
	 * @param temperature
	 * @param humidity
	 * @param timestamp
	 */
	public void updateDHT(double temperature, double humidity, Date timestamp) {
		tempTile.setValue(temperature);
		humidityTile.setValue(humidity);
		textAreaTemp.setText(timestamp.toString());
		textAreaHumidity.setText(timestamp.toString());
	}

	/**
	 * Update the image tile with the retrieved image
	 * 
	 * @param imageIS
	 */
	public void updateImage(InputStream imageIS) {
		this.imageInputStream = imageIS;
		imageTile.setImage(new Image(imageIS));
	}

	/**
	 * Update the doorbell tile with the retrieved timestamp it was pressed at
	 * 
	 * @param timeStamp
	 */
	public void updateDoorbell(Date timeStamp) {
		doorbellTextArea.setText("\n\nDoorbell pressed at: \n" + timeStamp);
	}

	/**
	 * Builds the Row with all the required tiles for one person
	 */
	public void buildRow() {
		tempTile = TileBuilder.create()
				.skinType(SkinType.GAUGE)
				.prefSize(450, 225)
				.title(username + "'s Temperature")
				.unit("°C")
				.threshold(100)
				.build();
		// container for timestamp for temperature DHT11 sensors
		textAreaTemp = new TextArea();
		textAreaTemp.setEditable(false);
		textAreaTemp.setStyle("-fx-control-inner-background: #2A2A2A; "
				+ "-fx-text-inner-color: white;"
				+ "-fx-text-box-border: transparent;");
		textAreaTemp.setText("" + timeStampDHT);
		textAreaTempVBox = new VBox(textAreaTemp);

		tempTimeTile = TileBuilder.create()
				.skinType(SkinType.CUSTOM)
				.prefSize(150, 75)
				.textSize(TextSize.BIGGER)
				.title("Temperature taken at: ")
				.graphic(textAreaTempVBox)
				.build();

		humidityTile = TileBuilder.create()
				.skinType(SkinType.PERCENTAGE)
				.prefSize(450, 225)
				.title(username + "'s Humidity")
				.unit("%")
				.maxValue(100)
				.text("" + timeStampDHT)
				.build();
		textAreaHumidity = new TextArea();
		textAreaHumidity.setEditable(false);
		textAreaHumidity.setStyle("-fx-control-inner-background: #2A2A2A; "
				+ "-fx-text-inner-color: white;"
				+ "-fx-text-box-border: transparent;");
		textAreaHumidity.setText("" + timeStampDHT);
		textAreaHumidityVBox = new VBox(textAreaHumidity);

		humidityTimeTile = TileBuilder.create()
				.skinType(SkinType.CUSTOM)
				.prefSize(150, 75)
				.textSize(TextSize.BIGGER)
				.title("Humidity taken at: ")
				.graphic(textAreaHumidityVBox)
				.build();

		imageTile = TileBuilder.create()
				.skinType(SkinType.IMAGE)
				.prefSize(300, 300)
				.title(username + "'s Motion Detected Image")
				.image(new Image(imageInputStream))
				.imageMask(ImageMask.ROUND)
				.text("Taken at: " + timeStampMotion)
				.build();

		doorbellTextArea = new TextArea();
		doorbellTextArea.setEditable(false);
		doorbellTextArea.setStyle("-fx-control-inner-background: #2A2A2A; "
				+ "-fx-text-inner-color: white;"
				+ "-fx-text-box-border: transparent;");
		doorbellTextArea.setText("\n\nDoorbell pressed at: \n" + timeStampDoorbell);
		doorbellTextAreaVBox = new VBox(doorbellTextArea);

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
}
