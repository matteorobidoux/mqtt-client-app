package datacomproject.mqttclientapp.mqtt;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.ECGenParameterSpec;

import org.junit.jupiter.api.Test;

import datacomproject.mqttclientapp.mqtt.KeyStore.SignatureHelper;

/**
 * @author Rim Dallali
 */

public class SignatureHelperTest {

  PublicKey publicKey;
  PrivateKey privateKey;

  public SignatureHelperTest() throws Exception {
    KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
    ECGenParameterSpec ecParaSpec = new ECGenParameterSpec("secp256r1");
    generator.initialize(ecParaSpec);
    KeyPair keys = generator.generateKeyPair();

    publicKey = keys.getPublic();
    privateKey = keys.getPrivate();
  }

  @Test
  public void testSignVerifySignature() throws Exception {
    SignatureHelper sigHelp = new SignatureHelper();
    String algorithm = "SHA256withECDSA";

    String messageToBeSigned = "This is the message to be signed.";
    String receivedMsg = "This is the message to be signed.";

    byte[] signature = sigHelp.generateSignature(algorithm, privateKey, messageToBeSigned);
    assertTrue(sigHelp.verifySignature(signature, publicKey, algorithm, receivedMsg));
  }
}
