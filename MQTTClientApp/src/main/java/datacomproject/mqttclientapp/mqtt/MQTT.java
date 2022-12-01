package datacomproject.mqttclientapp.mqtt;

import datacomproject.mqttclientapp.JavaFX.FXScreen;
import datacomproject.mqttclientapp.KeyStore.*;

import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
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
    public List<JSONObject> jsonObjectsMatteo = new ArrayList<JSONObject>();
    public List<JSONObject> jsonObjectsRim = new ArrayList<JSONObject>();
    public List<JSONObject> certificates = new ArrayList<JSONObject>();
    public FXScreen fxScreen;

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
    
    // Client subscribes to all topics begining with mqtt/
    public void subscribe(){
        client.subscribeWith()
            .topicFilter("mqtt/#")
            .send();
    }
    
    // Rerieves all messages sent to the client and/or certificate, verifies signature if it is a message and adds the data to the correct user List
    public void retrieveMessage() {
        client.toAsync().publishes(ALL, publish -> { 
            JSONObject jsonObject = new JSONObject(UTF_8.decode(publish.getPayload().get()).toString());
            if(!jsonObject.has("certificate")){    
                try {
                    byte[] signature = Base64.getDecoder().decode(jsonObject.get("signature").toString());
                    jsonObject.remove("signature");
                    Certificate certificate = null;
                    for (JSONObject certificateObject : certificates) {
                        if(certificateObject.has(publish.getTopic().toString().split("/")[1])){
                            certificate = retrieveCertificate(certificateObject, publish.getTopic().toString().split("/")[1]);
                        }
                    }
                    if(signatureHelper.verifySignature(signature, certificate.getPublicKey(), "SHA256withECDSA", jsonObject.toString())){
                        System.out.println("Received message: " + publish.getTopic() + " -> " + jsonObject);
                        if(publish.getTopic().toString().contains("matteorobidoux")){
                            if(fxScreen.row1.username.contains("matteorobidoux")){
                                if(publish.getTopic().toString().contains("dht")){
                                    fxScreen.row1.updateDHT(jsonObject.getDouble("temperature"), jsonObject.getDouble("humidity"), jsonObject.getString("timestamp"));
                                } else if(jsonObject.has("image")){
                                    fxScreen.row1.updateImage(null);
                                } else if(publish.getTopic().toString().contains("doorbell")){
                                    fxScreen.row1.updateDoorbell(jsonObject.getString("doorbell"));
                                }
                            } else if(fxScreen.row2.username.contains("matteorobidoux")){
                                if(publish.getTopic().toString().contains("dht")){
                                    fxScreen.row2.updateDHT(jsonObject.getDouble("temperature"), jsonObject.getDouble("humidity"), jsonObject.getString("timestamp"));
                                } else if(jsonObject.has("image")){
                                    fxScreen.row2.updateImage(null);
                                } else if(publish.getTopic().toString().contains("doorbell")){
                                    fxScreen.row2.updateDoorbell(jsonObject.getString("doorbell"));
                                }
                            } else if(fxScreen.row3.username.contains("matteorobidoux")){
                                if(publish.getTopic().toString().contains("dht")){
                                    fxScreen.row3.updateDHT(jsonObject.getDouble("temperature"), jsonObject.getDouble("humidity"), jsonObject.getString("timestamp"));
                                } else if(jsonObject.has("image")){
                                    fxScreen.row3.updateImage(null);
                                } else if(publish.getTopic().toString().contains("doorbell")){
                                    fxScreen.row3.updateDoorbell(jsonObject.getString("doorbell"));
                                }
                            }
                        } if(publish.getTopic().toString().contains("rimdallali")){
                            if(fxScreen.row1.username.contains("rimdallali")){
                                if(publish.getTopic().toString().contains("dht")){
                                    fxScreen.row1.updateDHT(jsonObject.getDouble("temperature"), jsonObject.getDouble("humidity"), jsonObject.getString("timestamp"));
                                } else if(jsonObject.has("image")){
                                    fxScreen.row1.updateImage(null);
                                } else if(publish.getTopic().toString().contains("doorbell")){
                                    fxScreen.row1.updateDoorbell(jsonObject.getString("doorbell"));
                                }
                            } else if(fxScreen.row2.username.contains("rimdallali")){
                                if(publish.getTopic().toString().contains("dht")){
                                    fxScreen.row2.updateDHT(jsonObject.getDouble("temperature"), jsonObject.getDouble("humidity"), jsonObject.getString("timestamp"));
                                } else if(jsonObject.has("image")){
                                    fxScreen.row2.updateImage(null);
                                } else if(publish.getTopic().toString().contains("doorbell")){
                                    fxScreen.row2.updateDoorbell(jsonObject.getString("doorbell"));
                                }
                            } else if(fxScreen.row3.username.contains("rimdallali")){
                                if(publish.getTopic().toString().contains("dht")){
                                    fxScreen.row3.updateDHT(jsonObject.getDouble("temperature"), jsonObject.getDouble("humidity"), jsonObject.getString("timestamp"));
                                } else if(jsonObject.has("image")){
                                    fxScreen.row3.updateImage(null);
                                } else if(publish.getTopic().toString().contains("doorbell")){
                                    fxScreen.row3.updateDoorbell(jsonObject.getString("doorbell"));
                                }
                            }
                        } if(publish.getTopic().toString().contains("carletondavis")){
                            if(fxScreen.row1.username.contains("carletondavis")){
                                if(jsonObject.has("dht")){
                                    fxScreen.row1.updateDHT(jsonObject.getDouble("temperature"), jsonObject.getDouble("humidity"), jsonObject.getString("timestamp"));
                                } else if(jsonObject.has("image")){
                                    fxScreen.row1.updateImage(null);
                                } else if(jsonObject.has("doorbell")){
                                    fxScreen.row1.updateDoorbell(jsonObject.getString("doorbell"));
                                }
                            } else if(fxScreen.row2.username.contains("carletondavis")){
                                if(jsonObject.has("dht")){
                                    fxScreen.row2.updateDHT(jsonObject.getDouble("temperature"), jsonObject.getDouble("humidity"), jsonObject.getString("timestamp"));
                                } else if(jsonObject.has("image")){
                                    fxScreen.row2.updateImage(null);
                                } else if(jsonObject.has("doorbell")){
                                    fxScreen.row2.updateDoorbell(jsonObject.getString("doorbell"));
                                }
                            } else if(fxScreen.row3.username.contains("carletondavis")){
                                if(jsonObject.has("dht")){
                                    fxScreen.row3.updateDHT(jsonObject.getDouble("temperature"), jsonObject.getDouble("humidity"), jsonObject.getString("timestamp"));
                                } else if(jsonObject.has("image")){
                                    fxScreen.row3.updateImage(null);
                                } else if(jsonObject.has("doorbell")){
                                    fxScreen.row3.updateDoorbell(jsonObject.getString("doorbell"));
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("There Was A Problem Verifying The Signature..." + e);
                }
            } else {
                System.out.println("Received Certificate: " + publish.getTopic() + " -> " + jsonObject);
                try {
                    JSONObject certificateJSONObject = new JSONObject();
                    certificateJSONObject.put(publish.getTopic().toString().split("/")[1], jsonObject.get("certificate"));
                    certificates.add(certificateJSONObject);
                } catch (JSONException e) {
                    System.out.println("Error Retrieving Certificate");
                }
            }
        });
    }
    
    // Publish message to a specific topic sending a JSON object which contains the datas being sent including the signature
    public JSONObject publishDataMessage(PrivateKey privateKey, String topic, JSONObject data) throws Exception{
        byte[] signature = signatureHelper.signMessage(privateKey, data.toString());
        data.put("signature", new String(Base64.getEncoder().encode(signature), "UTF-8")); 
        client.publishWith()
                .topic(getTopic(topic))
                .payload(UTF_8.encode(data.toString()))
                .retain(true)
                .send();
        return data;
    }

    // Publishes a message with the certificate within it
    public JSONObject publishCertificateMessage(Certificate certificate) throws CertificateEncodingException, JSONException, UnsupportedEncodingException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("certificate", new String(Base64.getEncoder().encode(certificate.getEncoded()), "UTF-8"));
        client.publishWith()
                .topic(getTopic("certificate"))
                .payload(UTF_8.encode(jsonObject.toString()))
                .retain(true)
                .send();
        return jsonObject;
    }

    // Gets the topic using the topic path and client username to create it
    private String getTopic(String topicPath){
        return "mqtt/" + username + "/" + topicPath;
    }

    // Disconnects from the client
    public void disconnect(){
        client.disconnect();
    }

    // Takes in a jsonObject and returns a certificate
    public Certificate retrieveCertificate(JSONObject jsonCertificate, String key) throws CertificateException{
        if(jsonCertificate.has(key)){
            CertificateFactory cf   = CertificateFactory.getInstance("X.509");
            return cf.generateCertificate(new ByteArrayInputStream(Base64.getDecoder().decode(jsonCertificate.get(key).toString())));
        } else {
            return null;
        }
    }

    public void retrieveFX(FXScreen setFxScreen){
        fxScreen = setFxScreen;
    }
}
