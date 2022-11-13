package datacomproject.mqttclientapp.mqtt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.junit.jupiter.api.Test;

import datacomproject.mqttclientapp.mqtt.KeyStore.SignatureHelper;

/**
 * @author Rim Dallali
 */
 
public class AsymmetricKeyCryptoTest {

  PublicKey publicKey;
  PrivateKey privateKey;

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
