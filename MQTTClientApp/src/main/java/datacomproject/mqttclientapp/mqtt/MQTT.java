package datacomproject.mqttclientapp.mqtt;

import datacomproject.mqttclientapp.KeyStore.*;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.exceptions.Mqtt5ConnAckException;

import static com.hivemq.client.mqtt.MqttGlobalPublishFilter.ALL;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;


/**
 *
 * @author Matteo
 */
public class MQTT {
    
    private Mqtt5BlockingClient client;
    private String username;
    private SignatureHelper signatureHelper = new SignatureHelper();
    public List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
    public List<Certificate> certificates = new ArrayList<Certificate>();

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
    
    // Rerieves all messages sent to the client and/or certificate
    public void retrieveMessage(PublicKey publicKey, String alg) {
        client.toAsync().publishes(ALL, publish -> { 
            JSONObject jsonObject = new JSONObject(UTF_8.decode(publish.getPayload().get()).toString());
            if(!jsonObject.has("certificate")){    
                try {
                    byte[] signature = Base64.getDecoder().decode(jsonObject.get("signature").toString());
                    jsonObject.remove("signature");
                    if(signatureHelper.verifySignature(signature, publicKey, alg, jsonObject.toString())){
                        System.out.println("Received message: " +
                            publish.getTopic() + " -> " +
                            jsonObject);
                        jsonObjects.add(jsonObject);
                    }
                } catch (Exception e) {
                    System.out.println("There Was A Problem Verifying The Signature..." + e);
                }
            } else {
                System.out.println("Received Certificate: " +
                            publish.getTopic() + " -> " +
                            jsonObject);
                                try {
                                    CertificateFactory cf   = CertificateFactory.getInstance("X.509");
                                    Certificate certificate = cf.generateCertificate(new ByteArrayInputStream(Base64.getDecoder().decode(jsonObject.get("certificate").toString())));
                                    certificates.add(certificate);
                                } catch (CertificateException | JSONException e) {
                                    System.out.println("Error Retrieving Certificate");
                                }
                          
            }
        });
    }
    
    // Publish message to a specific topic sending a JSON object which contains the datas being sent including the signature
    public void publishDataMessage(PrivateKey privateKey, String topic, JSONObject data) throws Exception{
        byte[] signature = signatureHelper.signMessage(privateKey, data.toString());
        data.put("signature", new String(Base64.getEncoder().encode(signature), "UTF-8"));
        client.publishWith()
                .topic(getTopic(topic))
                .payload(UTF_8.encode(data.toString()))
                .send();
    }

    // Publishes a message with the certificate within it
    public void publishCerificateMessage(String topic, Certificate certificate) throws CertificateEncodingException, JSONException, UnsupportedEncodingException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("certificate", new String(Base64.getEncoder().encode(certificate.getEncoded()), "UTF-8"));
        client.publishWith()
                .topic(getTopic(topic))
                .payload(UTF_8.encode(jsonObject.toString()))
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

    public static void main(String[] args) throws Exception{
        KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
        ECGenParameterSpec ecParaSpec = new ECGenParameterSpec("secp256r1");
        generator.initialize(ecParaSpec);
        KeyPair keys = generator.generateKeyPair();

        PublicKey publicKey = keys.getPublic();
        PrivateKey privateKey = keys.getPrivate();

        MQTT m = new MQTT();
        m.getMqttClient();
        m.createConnection("matteorobidoux", "password");
        m.retrieveMessage(publicKey, "SHA256withECDSA");
        m.subscribe();
        JSONObject j = new JSONObject();
        j.put("Testing","test");
        m.publishDataMessage(privateKey, "test", j);
        KeyStoreHelper k = new KeyStoreHelper();
        k.loadKeyStore("password".toCharArray(), "./MQTTClientApp/src/test/java/datacomproject/KeyStore/ECcertif.ks");
        Certificate c = k.extractCertificate("TEST");
        m.publishCerificateMessage("certificate", c);
    }
}
