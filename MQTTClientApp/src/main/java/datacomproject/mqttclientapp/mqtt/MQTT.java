package datacomproject.mqttclientapp.mqtt;

import datacomproject.mqttclientapp.JavaFX.FXScreen;
import datacomproject.mqttclientapp.KeyStore.*;

import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.exceptions.Mqtt5ConnAckException;

import static com.hivemq.client.mqtt.MqttGlobalPublishFilter.ALL;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MQTT {

	private Mqtt5BlockingClient client;
	private String username;
	private final String HOST = "5a81e32977cf48798ae1059437f7dd15.s1.eu.hivemq.cloud";
	private final String USER1 = "rimdallali";
	private final String USER2 = "matteorobidoux";
	private final String USER3 = "carletondavis";

	private SignatureHelper signatureHelper = new SignatureHelper();
	public FXScreen fxScreen;
	public KeyStoreHelper ksh;

	/**
	 * Retrieve MQTT client
	 * 
	 * @return Mqtt5BlockingClient
	 */
	public Mqtt5BlockingClient getMqttClient() {
		try {
			System.out.println("Retrieving MQTT Client...");
			client = MqttClient.builder()
					.useMqttVersion5()
					.serverHost(HOST)
					.serverPort(8883)
					.sslWithDefaultConfig()
					.buildBlocking();
		} catch (Mqtt5ConnAckException e) {
			System.out.println("Unable to Retrieve MQTT Client!");
			System.exit(0);
		}
		System.out.println("Retrieved MQTT Client!");
		return client;
	}

	/**
	 * Connect to HiveMQ using the user credentials
	 * 
	 * @param username
	 * @param password
	 * @return boolean
	 */
	public boolean createConnection(String username, String password) {
		System.out.println("Connecting to HiveMQ...");
		try {
			client.connectWith()
					.simpleAuth()
					.username(username)
					.password(UTF_8.encode(password))
					.applySimpleAuth()
					.send();
			System.out.println("Connected successfully");
			this.username = username;
			return true;
		} catch (Mqtt5ConnAckException | NullPointerException e) {
			System.out.println("Invalid Username or Password, Try Again...");
			return false;
		}
	}

	/**
	 * Subscribe to all topics beginning with mqtt/
	 */
	public void subscribe() {
		client.subscribeWith()
				.topicFilter("mqtt/#")
				.send();
	}

	/**
	 * Retrieve all messages and/or certificate sent to the client
	 * Verify signature if it's a message and add data to correct user list
	 */
	public void retrieveMessage() {
		client.toAsync().publishes(ALL, publish -> {
			JSONObject jsonObject = new JSONObject(UTF_8.decode(publish.getPayload().get()).toString());
			if (!jsonObject.has("certificate")) {
				try {
					byte[] signature = Base64.getDecoder().decode(jsonObject.get("signature").toString());
					jsonObject.remove("signature");
					Certificate certificate = ksh.extractCertificate(publish.getTopic().toString().split("/")[1]);

					if (signatureHelper.verifySignature(signature, certificate.getPublicKey(), "SHA256withECDSA",
							jsonObject.toString())) {
						System.out.println("Received message: " + publish.getTopic() + " -> " + jsonObject);
						if (publish.getTopic().toString().contains(USER2)) {
							if (fxScreen.row2.username.contains(USER2)) {
								if (publish.getTopic().toString().contains("dht")) {
									fxScreen.row2.updateDHT(jsonObject.getDouble("temperature"), jsonObject.getDouble("humidity"),
											jsonObject.getString("timestamp"));
								} else if (publish.getTopic().toString().contains("image")) {
									fxScreen.row2.updateImage(imageToInputStream(jsonObject.get("image")),
											jsonObject.getString("motionTimestamp"));
								} else if (publish.getTopic().toString().contains("doorbell")) {
									fxScreen.row2.updateDoorbell(jsonObject.getString("doorbell"));
								}
							}
						}
						if (publish.getTopic().toString().contains(USER1)) {
							if (fxScreen.row1.username.contains(USER1)) {
								if (publish.getTopic().toString().contains("dht")) {
									fxScreen.row1.updateDHT(jsonObject.getDouble("temperature"), jsonObject.getDouble("humidity"),
											jsonObject.getString("timestamp"));
								} else if (publish.getTopic().toString().contains("image")) {
									fxScreen.row1.updateImage(imageToInputStream(jsonObject.get("image")),
											jsonObject.getString("motionTimestamp"));
								} else if (publish.getTopic().toString().contains("doorbell")) {
									fxScreen.row1.updateDoorbell(jsonObject.getString("doorbell"));
								}
							}
						}
						if (publish.getTopic().toString().contains(USER3)) {
							if (fxScreen.row3.username.contains(USER3)) {
								if (publish.getTopic().toString().contains("dht")) {
									fxScreen.row3.updateDHT(jsonObject.getDouble("temperature"), jsonObject.getDouble("humidity"),
											jsonObject.getString("timestamp"));
								} else if (publish.getTopic().toString().contains("image")) {
									fxScreen.row3.updateImage(imageToInputStream(jsonObject.get("image")),
											jsonObject.getString("motionTimestamp"));
								} else if (publish.getTopic().toString().contains("doorbell")) {
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
					CertificateFactory cf = CertificateFactory.getInstance("X.509");
					Certificate c = cf.generateCertificate(
							new ByteArrayInputStream(Base64.getDecoder().decode(jsonObject.get("certificate").toString())));
					ksh.storeCertificate(c, publish.getTopic().toString().split("/")[1]);

				} catch (JSONException e) {
					System.out.println("Error Retrieving Certificate");
				} catch (CertificateException ex) {
					Logger.getLogger(MQTT.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		});
	}

	/**
	 * Publish message to a specific topic sending a JSON object which contains the
	 * data being sent including the signature.
	 * 
	 * @param privateKey
	 * @param topic
	 * @param data
	 * @return JSONObject
	 * 
	 * @throws Exception
	 */
	public JSONObject publishDataMessage(PrivateKey privateKey, String topic, JSONObject data) throws Exception {
		byte[] signature = signatureHelper.signMessage(privateKey, data.toString());
		data.put("signature", new String(Base64.getEncoder().encode(signature), "UTF-8"));
		client.publishWith()
				.topic(getTopic(topic))
				.payload(UTF_8.encode(data.toString()))
				.retain(true)
				.send();
		return data;
	}

	/**
	 * Publish a message with the certificate within it
	 * 
	 * @param certificate
	 * @return JSONObject
	 * 
	 * @throws Exception
	 */
	public JSONObject publishCertificateMessage(Certificate certificate)
			throws Exception {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("certificate", new String(Base64.getEncoder().encode(certificate.getEncoded()), "UTF-8"));
		client.publishWith()
				.topic(getTopic("certificate"))
				.payload(UTF_8.encode(jsonObject.toString()))
				.retain(true)
				.send();
		return jsonObject;
	}

	/**
	 * Get the topic using the topic path and client username to create it
	 * 
	 * @param topicPath
	 * @return String
	 */
	private String getTopic(String topicPath) {
		return "mqtt/" + username + "/" + topicPath;
	}

	/**
	 * Disconnect from the client
	 */
	public void disconnect() {
		client.disconnect();
	}

	/**
	 * Return certificate corresponding the given jsonObject and key
	 * 
	 * @param jsonCertificate
	 * @param key
	 * @return Certificate
	 * 
	 * @throws CertificateException
	 */
	public Certificate retrieveCertificate(JSONObject jsonCertificate, String key) throws CertificateException {
		if (jsonCertificate.has(key)) {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			return cf.generateCertificate(
					new ByteArrayInputStream(Base64.getDecoder().decode(jsonCertificate.get(key).toString())));
		} else {
			return null;
		}
	}

	/**
	 * Set the fxscreen and keystore helper objects
	 * 
	 * @param setFxScreen
	 * @param ksh1
	 */
	public void setVariables(FXScreen setFxScreen, KeyStoreHelper ksh1) {
		fxScreen = setFxScreen;
		ksh = ksh1;
	}

	/**
	 * Convert image to inputStream
	 * 
	 * @param image
	 * @return InputStream
	 */
	public InputStream imageToInputStream(Object image) throws IOException {
		byte[] img = Base64.getDecoder().decode(image.toString());
		return new ByteArrayInputStream(img);
	}
}
