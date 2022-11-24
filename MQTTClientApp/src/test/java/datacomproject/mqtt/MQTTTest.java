package datacomproject.mqtt;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;

import datacomproject.mqttclientapp.mqtt.MQTT;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Matteo
 */
public class MQTTTest {
    MQTT mqtt = new MQTT();
    JSONObject json = new JSONObject();
    
    @Test
    public void MQTTGetClientTest(){
         Mqtt5BlockingClient client = mqtt.getMqttClient();
         assertNotNull(client);
    }
    
    @Test
    public void MQTTCreateConnectionTest(){
        boolean connectionEstablished = mqtt.createConnection("TestUsername", "TestPassword");
        assertEquals(false, connectionEstablished);
    }

    @Test
    public void MQTTPublishMessageTestNullPointerException(){
        assertThrows(NullPointerException.class, () -> {
            //mqtt.publishMessage("Test", json);
        });
    }
    
    @Test
    public void MQTTSubscribeTestNullPointerException(){
        assertThrows(NullPointerException.class, () -> {
            mqtt.subscribe();
        });
    }
    
    @Test
    public void MQTTRetrieveMessageTestNullPointerException(){
        assertThrows(NullPointerException.class, () -> {
            //mqtt.retrieveMessage();
        });
    }

    @Test
    public void MQTTDisconnectTestNullPointerException(){
        assertThrows(NullPointerException.class, () -> {
            mqtt.disconnect();
        });
    }
}
