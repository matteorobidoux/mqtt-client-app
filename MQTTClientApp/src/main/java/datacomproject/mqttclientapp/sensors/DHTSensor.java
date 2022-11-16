package datacomproject.mqttclientapp.sensors;

import java.io.IOException;

/**
 *
 * @author Rim Dallali
 */
public class DHTSensor {

    private final String programPath = "src/main/Python/DHT11.py";

    public void startThread() {
        Thread exampleThread = new Thread(() -> {
            while (true) {
                try {
                    String output = callProcess();
                    double humidity = Double.parseDouble(output.split(" ")[0]);
                    double temperature = Double.parseDouble(output.split(" ")[1]);
                    System.out.println("temparature => " + temperature);
                    System.out.println("humidity    => " + humidity);
                    //Delay 5 seconds
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
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
