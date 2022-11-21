package datacomproject.KeyStore;

import static org.junit.jupiter.api.Assertions.*;

import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.spec.ECGenParameterSpec;
import org.junit.jupiter.api.Test;

import datacomproject.mqttclientapp.KeyStore.KeyStoreHelper;

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
  }

  @Test
  public void testExtractCertificate() throws Exception {
    String alias = "TEST";
    ksh.loadKeyStore(password, filename);
    Certificate certificate = ksh.extractCertificate(alias);
    assertNotNull(certificate);
  }

  @Test
  public void testStoreCertificate() throws Exception {
    String alias = "STORETEST";
    ksh.loadKeyStore(password, filename);
    Certificate originalCertificate = ksh.extractCertificate("TEST");
    assertNotNull(originalCertificate);

    // store the certificate to the new alias STORETEST
    ksh.storeCertificate(originalCertificate, alias);

    // get the certificate from the new alias we stored it to
    Certificate storedCertificate = ksh.extractCertificate(alias);
    assertNotNull(storedCertificate);
  }

  @Test
  public void testLoadKeyStore() throws Exception {
    KeyStore ks = ksh.loadKeyStore(password, filename);
    assertNotNull(ks);
  }

  @Test
  public void testGetKeyStoreInfo() throws Exception {
    ksh.loadKeyStore(password, filename);
    Certificate certificate = ksh.extractCertificate("TEST");
    assertNotNull(certificate);
  }

  @Test
  public void testExtractPrivateKey() throws Exception {
    ksh.loadKeyStore(password, filename);
    PrivateKey pk = ksh.extractPrivateKey("TEST");
    assertNotNull(pk);
  }
}