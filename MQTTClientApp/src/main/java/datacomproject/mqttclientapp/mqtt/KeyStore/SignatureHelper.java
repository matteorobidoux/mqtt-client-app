package datacomproject.mqttclientapp.mqtt.KeyStore;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class SignatureHelper {

    /**
     * Sign and Verify a string message
     *
     * @param privateKey
     * @param message
     * @return byte[]
     * @throws Exception
     */
    public byte[] signMessage(PrivateKey privateKey, String message) throws Exception {
        System.out.println("Signing and Verifying a message");
        System.out.println("-------------------------------");
        String algorithm = "SHA256withECDSA";
        // Create an instance of the signature scheme for the given signature algorithm
        Signature sig = Signature.getInstance(algorithm);

        // Initialize the signature scheme
        sig.initSign(privateKey);

        // Compute the signature
        sig.update(message.getBytes("UTF-8"));
        // Generate signature for the message.
        byte[] signature = sig.sign();

        System.out.println("Message has been signed");
        return signature;
    }

    /**
     * Verify digital signature given as input
     *
     * @param signature
     * @param publicKey
     * @param alg
     * @param msg
     * @return boolean
     * @throws Exception
     */
    public boolean verifySignature(byte[] signature, PublicKey publicKey, String alg, String msg) throws Exception {
        // Create an instance of the signature scheme for the given signature algorithm
        Signature sig = Signature.getInstance(alg);

        // Initialize the signature verification scheme.
        sig.initVerify(publicKey);

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
