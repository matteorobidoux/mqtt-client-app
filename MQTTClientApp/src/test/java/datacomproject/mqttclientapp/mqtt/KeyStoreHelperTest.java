package datacomproject.mqttclientapp.mqtt;

import static org.junit.jupiter.api.Assertions.*;

import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.spec.ECGenParameterSpec;
import org.junit.jupiter.api.Test;
import datacomproject.mqttclientapp.mqtt.KeyStore.KeyStoreHelper;

/**
 * @author Rim Dallali
 */

public class KeyStoreHelperTest {

  KeyStoreHelper ksh;
  KeyStore ks;
  String filename = "src/test/java/datacomproject/mqttclientapp/mqtt/ECcertif.ks";
  char[] password = new char[] { 'p', 'a', 's', 's', 'w', 'o', 'r', 'd' };

  public KeyStoreHelperTest() throws Exception {
    KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
    ECGenParameterSpec ecParaSpec = new ECGenParameterSpec("secp256r1");
    generator.initialize(ecParaSpec);

    ksh = new KeyStoreHelper();
    ks = ksh.loadKeyStore(password, filename);
    ksh.setKeyStore(ks);
  }

  @Test
  public void testExtractCertificate() throws Exception {
    String alias = "TEST";
    Certificate certificate = ksh.extractCertificate(alias);
    assertNotNull(certificate);
  }

  @Test
  public void testStoreCertificate() throws Exception {
    String alias = "STORETEST";
    Certificate originalCertificate = ksh.extractCertificate("TEST");
    assertNotNull(originalCertificate);

    // store the certificate to the new alias STORETEST
    ksh.storeCertificate(alias, originalCertificate);

    // get the certificate from the new alias we stored it to
    Certificate storedCertificate = ksh.extractCertificate(alias);
    assertNotNull(storedCertificate);
  }

  @Test
  public void testLoadKeyStore() throws Exception {
    assertNotNull(ks);
  }

  @Test
  public void testGetKeyStoreInfo() throws Exception {
    Certificate certificate = ksh.extractCertificate("TEST");
    assertNotNull(certificate);
  }

  @Test
  public void testExtractPrivateKey() throws Exception {
    // Certificate certificate = ksh.extractCertificate("TEST");
    PrivateKey pk = ksh.extractPrivateKey("TEST", password);
    assertNotNull(pk);
  }
}