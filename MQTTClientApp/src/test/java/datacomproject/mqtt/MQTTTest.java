package datacomproject.mqtt;

import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;

import datacomproject.mqttclientapp.KeyStore.KeyStoreHelper;
import datacomproject.mqttclientapp.mqtt.MQTT;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.spec.ECGenParameterSpec;

/**
 *
 * @author Matteo
 */
public class MQTTTest {
	MQTT mqtt = new MQTT();
	JSONObject json = new JSONObject();
	PublicKey publicKey;
	PrivateKey privateKey;
	Certificate certificate;

	public MQTTTest() throws Exception {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
		ECGenParameterSpec ecParaSpec = new ECGenParameterSpec("secp256r1");
		generator.initialize(ecParaSpec);
		KeyPair keys = generator.generateKeyPair();
		publicKey = keys.getPublic();
		privateKey = keys.getPrivate();
		String alias = "TEST";
		KeyStoreHelper ksh = new KeyStoreHelper();
		ksh.loadKeyStore("password".toCharArray(), "src/test/java/datacomproject/KeyStore/ECcertif.ks");
		certificate = ksh.extractCertificate(alias);
	}

	@Test
	public void MQTTGetClientTest() {
		Mqtt5BlockingClient client = mqtt.getMqttClient();
		assertNotNull(client);
	}

	@Test
	public void MQTTCreateConnectionTest() {
		mqtt.getMqttClient();
		boolean connectionEstablished = mqtt.createConnection("matteorobidoux", "password");
		assertEquals(true, connectionEstablished);
	}

	@Test
	public void MQTTPublishDataMessageTest() throws Exception {
		mqtt.getMqttClient();
		mqtt.createConnection("matteorobidoux", "password");
		JSONObject message = mqtt.publishDataMessage(privateKey, "Test", json);
		assertNotNull(message);
	}

	@Test
	public void MQTTPublishCertificateMessageTest() throws Exception {
		mqtt.getMqttClient();
		mqtt.createConnection("matteorobidoux", "password");
		JSONObject message = mqtt.publishCertificateMessage(certificate);
		assertNotNull(message);
	}
}
