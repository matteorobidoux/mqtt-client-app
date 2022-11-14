/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datacomproject.mqttclientapp.mqtt.Camera;

import com.pi4j.context.Context;
import java.util.logging.Logger;

/**
 *
 * @author Ray Hernaez
 */
/**
 * This interface should be implemented by each CrowPi example / application
 */

public interface IApplication {
    /**
     * Represents the main application entry point called by the launcher.
     * This should execute whatever the application is supposed to do.
     *
     * @param pi4j Pi4J context
     */
    void execute(Context pi4j);

    /**
     * Returns a unique name which is used to determine how the application can be called.
     * This uses the simple class name by default, so that the application has the same name as its class.
     *
     * @return Unique application name
     */
    default String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * Returns a human-readable description what this application is doing.
     * This returns a short explanation together with the class FQDN by default.
     *
     * @return Human-readable application description
     */
    default String getDescription() {
        return this.getClass().getName();
    }

    /**
     * Utility function to sleep for the specified amount of milliseconds.
     * An {@link InterruptedException} will be catched and ignored while setting the interrupt flag again.
     *
     * @param milliseconds Time in milliseconds to sleep
     */
    default void delay(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Logger instance
     */
    Logger logger = Logger.getLogger("Pi4J-App");

    default void logInfo(String msg){
        logger.info(() -> msg);
    }
    default void logError(String msg){
        logger.severe(() -> msg);
    }
}

