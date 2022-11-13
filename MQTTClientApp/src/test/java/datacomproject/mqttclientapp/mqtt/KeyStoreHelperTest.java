package datacomproject.mqttclientapp.mqtt;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.spec.ECGenParameterSpec;
import org.junit.jupiter.api.Test;
import datacomproject.mqttclientapp.mqtt.KeyStore.KeyStoreHelper;

/**
 * @author Rim Dallali
 */

public class KeyStoreHelperTest {

  PublicKey publicKey;
  PrivateKey privateKey;
  KeyStoreHelper ksh;
  KeyStore ks;
  String filename = "src/test/java/datacomproject/mqttclientapp/mqtt/ECcertif.ks";
  char[] password = new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
  

  public KeyStoreHelperTest() throws Exception {
    KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
    ECGenParameterSpec ecParaSpec = new ECGenParameterSpec("secp256r1");
    generator.initialize(ecParaSpec);
    KeyPair keys = generator.generateKeyPair();

    publicKey = keys.getPublic();
    privateKey = keys.getPrivate();
    ksh = new KeyStoreHelper();
  }

  @Test
  public void testExtractCertificate() throws Exception {
    String alias = "TEST";
    ks = ksh.loadKeyStore(password, filename);
    Certificate certificate = ksh.getKeyStoreInfo(ks, password, filename, alias);
    assertNotNull(certificate);
  }

  @Test
  public void testStoreCertificate() throws Exception {
    String alias = "STORETEST";
    //get certificate we already have from keystore
    ks = ksh.loadKeyStore(password, filename);
    Certificate originalCertificate = ksh.getKeyStoreInfo(ks, password, filename, "TEST");
    assertNotNull(originalCertificate);

    //store the certificate to the new alias SCORETEST
    ksh.storeToKeyStore(ks, alias, originalCertificate);

    //get the certificate from the new alias we stored it to
    Certificate storedCertificate = ksh.getKeyStoreInfo(ks, password, filename, alias);
    assertNotNull(storedCertificate);
  }
}
