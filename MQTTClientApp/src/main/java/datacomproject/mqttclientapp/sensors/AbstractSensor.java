package datacomproject.mqttclientapp.sensors;

import java.io.IOException;

public abstract class AbstractSensor {
  abstract public void startThread();

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
