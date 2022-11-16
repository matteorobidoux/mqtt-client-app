/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package datacomproject.mqttclientapp.mqtt.Camera;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import datacomproject.mqttclientapp.mqtt.Camera.Camera.PicConfig;
import datacomproject.mqttclientapp.mqtt.Camera.Camera.PicConfig.Builder;
import datacomproject.mqttclientapp.mqtt.Camera.Camera.VidConfig;

/**
 *
 * @author Ray Hernaez
 */

public class CameraApp implements IApplication {
    
    //Pi4J code to control camera

    /**
     *
     * @param pi4j
     */
   
    @Override
    public void execute(Context pi4j) {
        System.out.println("\nInitializing the camera");
        Camera camera = new Camera();

        System.out.println("Setting up the config to take a picture.");
        
        //Configure the camera setup
        PicConfig config = Camera.PicConfig.Builder.newInstance()
                .outputPath("/home/cdavis/Pictures/")
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
                .outputPath("/home/cdavis/Videos/")
                .disablePreview(true)
		.recordTime(3000)
		.useDate(true)
		.build();
        
        //Take the video
        camera.takeVid(vidconfig); 
    }

    
    public static void main(String[] args) {

        //Initialize the Pi4J Runtime Context
        Context pi4j = Pi4J.newAutoContext();

        CameraApp runApp = new CameraApp();
        runApp.execute(pi4j);
        
        // Shutdown Pi4J
        pi4j.shutdown();

    }
    
}

