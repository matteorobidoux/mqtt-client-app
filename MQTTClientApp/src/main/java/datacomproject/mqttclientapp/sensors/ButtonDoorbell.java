package datacomproject.mqttclientapp.sensors;

import java.io.IOException;

/**
 *
 * @author Rim Dallali
 */
public class ButtonDoorbell {

    private final String programPath = "src/main/Python/Doorbell.py";

    public void startThread() {
        Thread exampleThread = new Thread(() -> {
            boolean buttonState = false;
            while (true) {
                try {
                    String output = callProcess();
                    if (output.equals("on") && !buttonState) {
                        buttonState = true;
                        System.out.println("button state => pressed");
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
        exampleThread.start();
    }

    private String callProcess() {
        String output = "";
        try {
            MyProcessBuilder myProcessBuilder = new MyProcessBuilder(programPath);
            output = myProcessBuilder.startProcess();

        } catch (IOException e) {
            System.err.println("startProcess failed");
        }
        return output;
    }
}
