package datacomproject.mqttclientapp.mqtt.KeyStore;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

/**
 *
 * @author Rim Dallali
 */
/**
 * Code illustrating digital signature generation and verification
 */
public class SignatureHelper {

  /**
   * Method for generating digital signature.
   */
  public byte[] generateSignature(String algorithm, PrivateKey privatekey, String message) throws Exception {

    // Create an instance of the signature scheme for the given signature algorithm
    Signature sig = Signature.getInstance(algorithm);

    // Initialize the signature scheme
    sig.initSign(privatekey);

    // Compute the signature
    sig.update(message.getBytes("UTF-8"));
    byte[] signature = sig.sign();

    return signature;
  }

  /**
   * Method for verifying digital signature.
   */
  public boolean verifySignature(byte[] signature, PublicKey publickey, String algorithm, String msg) throws Exception {

    // Create an instance of the signature scheme for the given signature algorithm
    Signature sig = Signature.getInstance(algorithm, "SunEC");

    // Initialize the signature verification scheme.
    sig.initVerify(publickey);

    // Compute the signature.
    sig.update(msg.getBytes("UTF-8"));

    // Verify the signature.
    boolean validSignature = sig.verify(signature);

    if (validSignature) {
      System.out.println("\nSignature is valid");
    } else {
      System.out.println("\nSignature is NOT valid!!!");
    }
    return validSignature;
  }
}
