package datacomproject.mqttclientapp.mqtt;

import datacomproject.mqttclientapp.KeyStore.*;

import java.io.Console;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONObject;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.exceptions.Mqtt5ConnAckException;

import static com.hivemq.client.mqtt.MqttGlobalPublishFilter.ALL;
import static java.nio.charset.StandardCharsets.UTF_8;


/**
 *
 * @author Matteo
 */
public class MQTT {
    
    private Mqtt5BlockingClient client;
    private String username;
    private SignatureHelper signatureHelper = new SignatureHelper();

    // Retrives MQTT Client
    public Mqtt5BlockingClient getMqttClient(){
        try{
            System.out.println("Retrieving MQTT Client...");
            client = MqttClient.builder()
                    .useMqttVersion5()
                    .serverHost("5a81e32977cf48798ae1059437f7dd15.s1.eu.hivemq.cloud")
                    .serverPort(8883)
                    .sslWithDefaultConfig()
                    .buildBlocking();
        } catch(Mqtt5ConnAckException e){
            System.out.println("Unable to Retrieve MQTT Client!");
            System.exit(0);
        }  
        System.out.println("Retrieved MQTT Client!");   
        return client;
    }
    
    // Connects to HiveMQ using client credentials
    public boolean createConnection(String username, String password){
        System.out.println("Connecting to HiveMQ...");
        try{
            client.connectWith()
                .simpleAuth()
                .username(username)
                .password(UTF_8.encode(password))
                .applySimpleAuth()
                .send();
            System.out.println("Connected successfully");
            this.username = username;
            return true;
        } catch(Mqtt5ConnAckException | NullPointerException e){
            System.out.println("Invalid Username or Password, Try Again...");
            return false;
        }
    }
    
    // Client subscribes to all topics begining with mqtt
    public void subscribe(){
        client.subscribeWith()
                .topicFilter("mqtt/#")
                .send();
    }
    
    // Rerieves all messages sent to the client
    public List<JSONObject> retrieveMessage(byte[] signature, PublicKey publicKey, String alg) {
        List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
        client.toAsync().publishes(ALL, publish -> {
            JSONObject jsonObject = new JSONObject(UTF_8.decode(publish.getPayload().get()).toString());
            try {
                if(signatureHelper.verifySignature(signature, publicKey, alg, jsonObject.toString())){
                    System.out.println("Received message: " +
                        publish.getTopic() + " -> " +
                        jsonObject);
                    jsonObjects.add(jsonObject);
                }
            } catch (Exception e) {
                System.out.println("There Was A Problem Verifying The Signature...");
            }
        });
        return jsonObjects;
    }
    
    // Publish message to a specific topic sending a JSON object which contains the datas being sent and returns the signature of the message
    public byte[] publishDataMessage(PrivateKey privateKey, String topic, JSONObject data) throws Exception{
        byte[] signature = signatureHelper.signMessage(privateKey, data.toString());
        client.publishWith()
                .topic(getTopic(topic))
                .payload(UTF_8.encode(data.toString()))
                .send();
        return signature;
    }

    // Publishes a message with the certificate within it
    public void publishCerificateMessage(String topic, Certificate certificate){
        client.publishWith()
                .topic(getTopic(topic))
                .payload(UTF_8.encode(certificate.toString()))
                .send();
    }

    // Gets the topic using the topic path and client username to create it
    private String getTopic(String topicPath){
        return "mqtt/" + username + "/" + topicPath;
    }

    // Disconnect from the client
    public void disconnect(){
        client.disconnect();
    }
}
