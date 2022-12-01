package datacomproject.mqttclientapp.Camera;

import com.pi4j.context.Context;

import datacomproject.mqttclientapp.Camera.Camera.PicConfig;
import datacomproject.mqttclientapp.Camera.Camera.VidConfig;

import java.nio.file.Path;
import java.nio.file.Paths;
 
/**
 *
 * @original_author Carlton Davis 
 * @modified_by Rim Dallali 
 */

public class CameraApp implements IApplication {
    
    /**
     *pi4j code to control the camera
     * @param pi4j
     */
   
    @Override
    public void execute(Context pi4j) {
        System.out.println("\nInitializing the camera");
        Camera camera = new Camera();
        
        Path userHome = Paths.get(System.getProperty("user.home"));

        System.out.println("Setting up the config to take a picture.");
        
        //Configure the camera setup
        PicConfig config = Camera.PicConfig.Builder.newInstance()
                .outputPath(userHome + "/Pictures/")
		.delay(3000)
		.disablePreview(true)
		.encoding(Camera.PicEncoding.PNG)
		.useDate(true)
		.quality(93)
		.width(1280)
		.height(800)
		.build();

        //Take the picture
        camera.takeStill(config);

        System.out.println("waiting for camera to take pic");
        delay(4000);

        System.out.println("Taking a video for 3 seconds");
        
        //Configure the video setup
        VidConfig vidconfig = Camera.VidConfig.Builder.newInstance()
                .outputPath(userHome + "/Videos/")
                .disablePreview(true)
		.recordTime(3000)
		.useDate(true)
		.build();
        
        //Take the video
        camera.takeVid(vidconfig); 
    }
}

