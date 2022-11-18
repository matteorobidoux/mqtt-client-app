package datacomproject.mqttclientapp.sensors;

import java.util.Date;

/**
 *
 * @author Rim Dallali
 */
public class DoorbellButton extends AbstractSensor {

    private final String programPath = "src/main/Python/Doorbell.py";

    public void startThread() {
        Thread doorbellThread = new Thread(() -> {
            boolean buttonState = false;
            while (true) {
                try {
                    String output = callProcess(programPath);
                    if (output.equals("on") && !buttonState) {
                        buttonState = true;
                        Date timeStamp = new Date();
                        System.out.println("button state => pressed at " + timeStamp);
                        //take timestamp here
                    } else if (output.equals("off") && buttonState) {
                        System.out.println("button state => released");
                        buttonState = false;
                    }

                } catch (Exception ex) {
                    System.err.println("exampleThread thread got interrupted");
                }
            }
        });

        //Start the thread
        doorbellThread.start();
    }
}
