package datacomproject.mqttclientapp.sensors;

import java.util.Date;

/**
 *
 * @author Rim Dallali
 */
public class DHTSensor extends AbstractSensor {

    private final String programPath = "src/main/Python/DHT11.py";

    public void startThread() {
        Thread dhtThread = new Thread(() -> {
            while (true) {
                try {
                    String output = callProcess(programPath);
                    double humidity = Double.parseDouble(output.split(" ")[0]);
                    double temperature = Double.parseDouble(output.split(" ")[1]);
                    Date timeStamp = new Date();
                    System.out.println("DHT sensor data captured at: " + timeStamp);
                    System.out.println("temparature => " + temperature);
                    System.out.println("humidity    => " + humidity);
                    //Delay 2 seconds
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    System.err.println("exampleThread thread got interrupted");
                }
            }
        });

        //Start the thread
        dhtThread.start();
    }
}
