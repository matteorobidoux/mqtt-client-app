package datacomproject.mqttclientapp.sensors;

import datacomproject.mqttclientapp.JavaFX.FXScreen;
import datacomproject.mqttclientapp.mqtt.MQTT;

import java.io.IOException;
import java.security.PrivateKey;

public abstract class AbstractSensor {
  abstract public void startThread(FXScreen fxScreen, MQTT mqtt, PrivateKey privateKey);

  // Start the process and get the output
  protected String callProcess(String programPath) {
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
