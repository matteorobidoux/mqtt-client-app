package datacomproject.mqttclientapp;

import java.io.Console;
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
    
    private Scanner scanner = new Scanner(System.in);
    private Mqtt5BlockingClient client;
    private String username;
    
    
    // Asks client for their HiveMQ username
    private String getUsername(){
        System.out.print("Please Enter Your HiveMQ Username: ");
        String user = scanner.nextLine();
        username = user;
        System.out.println("");
        return user;
    }
    
    // Asks client for their HiveMQ password
    private String getPassword(){
        Console console = System.console();
        System.out.print("Please Enter Your HiveMQ Password: ");
        char[] password = console.readPassword();
        System.out.println("");
        return String.valueOf(password);
    }

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
    public void createConnection(){
        boolean valid = false;
        while(!valid){
            username = getUsername();
            String password = getPassword();
            System.out.println("Connecting to HiveMQ...");
            try{
                client.connectWith()
                    .simpleAuth()
                    .username(username)
                    .password(UTF_8.encode(password))
                    .applySimpleAuth()
                    .send();
                valid = true;
            } catch(Mqtt5ConnAckException e){
                System.out.println("Invalid Username or Password, Try Again...");
            }
        }

        System.out.println("Connected successfully");
    }
    
    // Client subscribes to a specific topic
    public void subscribe(String topic){
        client.subscribeWith()
                .topicFilter(getTopic(topic))
                .send();
    }
    
    // Rerieves all messages sent to the client
    public List<JSONObject> retrieveMessage(){
        List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
        client.toAsync().publishes(ALL, publish -> {
            JSONObject jsonObject = new JSONObject(UTF_8.decode(publish.getPayload().get()).toString());
            System.out.println("Received message: " +
                publish.getTopic() + " -> " +
                jsonObject);
            jsonObjects.add(jsonObject);
        });
        return jsonObjects;
    }
    
    // Publish message to a specific topic sending a JSON object which contains the datas being sent
    public void publishMessage(String topic, JSONObject data){
        client.publishWith()
                .topic(getTopic(topic))
                .payload(UTF_8.encode(data.toString()))
                .send();
    }

    // Gets the topic using the topic path and client username to create it
    private String getTopic(String topicPath){
        return username + "/" + topicPath;
    }

    // Disconnect from the client
    public void disconnect(){
        client.disconnect();
    }
}
