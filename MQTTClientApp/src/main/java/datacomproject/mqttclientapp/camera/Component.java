/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datacomproject.mqttclientapp.camera;

import java.util.logging.Logger;

/**
 *
 * @author Ray Hernaez
 */
public abstract class Component {
    /**
     * Logger instance
     */
    private final Logger logger = Logger.getLogger(getClass().getName());

    protected void logInfo(String msg) {
        logger.info(() -> msg);
    }

    protected void logError(String msg) {
        logger.severe(() -> msg);
    }

    protected void logConfig(String msg) {
        logger.config(() -> msg);
    }

    protected void logDebug(String msg) {
        logger.fine(() -> msg);
    }

    /**
     * Utility function to sleep for the specified amount of milliseconds.
     * An {@link InterruptedException} will be caught and ignored while setting the interrupt flag again.
     *
     * @param milliseconds Time in milliseconds to sleep
     */
    void delay(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
