/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datacomproject.mqttclientapp;

import java.io.Console;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import static com.hivemq.client.mqtt.MqttGlobalPublishFilter.ALL;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 *
 * @author Matteo
 */
public class MQTT {
    
    private static Scanner scanner = new Scanner(System.in);
    private static Mqtt5BlockingClient client;
    private static String ip;
    
    public static String getUsername(){
        System.out.print("Please Enter Your HiveMQ Username: ");
        String username = scanner.nextLine();
        System.out.println("");
        return username;
    }
    
    public static String getPassword(){
        Console console = System.console();
        System.out.print("Please Enter Your HiveMQ Password: ");
        char[] password = console.readPassword();
        System.out.println("");
        return String.valueOf(password);
    }

    public static Mqtt5BlockingClient getMqttClient(){
        client = MqttClient.builder()
                .useMqttVersion5()
                .serverHost("5a81e32977cf48798ae1059437f7dd15.s1.eu.hivemq.cloud")
                .serverPort(8883)
                .sslWithDefaultConfig()
                .buildBlocking();
        
        return client;
    }
    
    private static void createConnection(String username, String password){
        System.out.println("Connecting to HiveMQ...");
        client.connectWith()
                .simpleAuth()
                .username(username)
                .password(UTF_8.encode(password))
                .applySimpleAuth()
                .send();

        System.out.println("Connected successfully");
    }
    
    private static void subscribe(String topic){
        client.subscribeWith()
                .topicFilter(topic)
                .send();
    }
    
    private static void retrieveMessage(){
        client.toAsync().publishes(ALL, publish -> {
            System.out.println("Received message: " +
                publish.getTopic() + " -> " +
                UTF_8.decode(publish.getPayload().get()));
        });
    }
    
    private static void publishMessage(String topic){
        client.publishWith()
                .topic(topic)
                .payload(UTF_8.encode("Hello"))
                .send();
    }

    private static void getIpAddress(){
        try {
            InetAddress ipv= InetAddress.getLocalHost();
            ip =  ipv.getHostAddress();
        } catch(UnknownHostException e){
            e.printStackTrace();
        }
    }

    private static String getTopic(String topicPath){
        return ip + "/" + topicPath;
    }

    private static void disconnect(){
        client.disconnect();
    }

    public void setup() {
        getMqttClient();
        createConnection(getUsername(), getPassword());
        getIpAddress();
        subscribe(getTopic("Matteo"));
        subscribe(getTopic("Ray"));
        subscribe(getTopic("Rim"));
        retrieveMessage();
        publishMessage(getTopic("Matteo"));
        publishMessage(getTopic("Rim"));
        publishMessage(getTopic("Ray"));
        disconnect();
    }
}
