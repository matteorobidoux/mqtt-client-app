package datacomproject.mqttclientapp.mqtt.KeyStore;

import java.util.Base64;
import java.util.Scanner;
import java.io.Console;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.cert.Certificate;
import java.security.*;
import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Rim Dallali
 */
public class KeyStoreHelper {

    private String filepath;
    private char[] password;
    private KeyStore ks;
    private SignatureHelper sighelp = new SignatureHelper();

    public void getUserInput() throws Exception {
        Scanner scanner = new Scanner(System.in);
        Console console = System.console();

        // getting and validating filename
        System.out.println("------------------------------------------------");
        System.out.println("-- Provide path where the keystore is located --");
        boolean validKeyStore = false;
        while (!validKeyStore) {
            try {
                filepath = getFilename();
                validKeyStore = true;
                if (validKeyStore) {
                    // getting and validating password
                    System.out.println("-------- Provide password --------");
                    char[] password = console.readPassword();
                    while (!validatePassword(password)) {
                        System.out.println("--- Invalid password, try again --");
                        password = console.readPassword();
                    }
                    // load the keystore
                    this.ks = loadKeyStore(password, filepath);
                }
            } catch (EOFException e) {
                System.out.println("Given file path is INVALID");
                validKeyStore = false;
            } catch (IOException ie) {
                System.out.println("Wrong KeyStore password OR Invalid KeyStore path, try again");
                validKeyStore = false;
            }
        }
        scanner.close();
    }

    /**
     * Load keyStore from filename and password user input
     * 
     * @param password
     * @param filename
     * @return KeyStore
     * @throws Exception
     */
    public KeyStore loadKeyStore(char[] password, String filename) throws Exception {
        System.out.println("loading the key store");
        System.out.println("---------------------");
        FileInputStream fis = null;
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        fis = new FileInputStream(filename);
        ks.load(fis, password);
        System.out.println("KeyStore has been loaded");
        return ks;
    }

    /**
     * Get trusted certificate from keyStore at the given alias
     * 
     * @param alias
     * @return Certificate
     * @throws Exception
     */
    public Certificate extractCertificate(String alias) throws KeyStoreException {
        System.out.println("Get Trusted Certificate");
        System.out.println("-----------------------");

        Certificate trustedCertificate = ks.getCertificate(alias);
        return trustedCertificate;
    }

    /**
     * Store the input certificate to the keystore at input alias
     * returns a boolean representing whether the store succeeded or not
     * TODO do we want to overwrite the certificate at given alias or not?
     * 
     * @param alias
     * @param certificate
     * @return boolean
     * @throws Exception
     */
    public void storeCertificate(String alias, Certificate certificate) throws Exception {
        System.out.println("Storing the certificate");
        System.out.println("-----------------------");
        ks.setCertificateEntry(alias, certificate);
        System.out.println("certificate alias entry has been set\nalias: " + alias + "\ncertificate: "
                + certificate.getPublicKey().toString());
    }

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
        SignatureHelper sighelp = new SignatureHelper();
        String algorithm = "SHA256withECDSA";
        // Generate signature for the message.
        byte[] signature = sighelp.generateSignature(algorithm, privateKey, message);
        System.out.println("\nSignature: " + Base64.getEncoder().encodeToString(signature));
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
        boolean verified = sighelp.verifySignature(signature, publicKey, alg, msg);
        if (verified) {
            System.out.println("message has been verified");
        } else {
            System.out.println("message has NOT been verified");
        }
        return verified;
    }

    /**
     * Checks if the given alias is part of the aliases in the keystore
     * 
     * @param alias
     * @return boolean
     * @throws KeyStoreException
     */
    public boolean checkAlias(String alias) throws KeyStoreException {
        while (ks.aliases().hasMoreElements()) {
            if (ks.aliases().nextElement().equals(alias)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Prompts the user to enter a filename for where to store the secret key
     * Helper method
     * 
     * @return String
     * @throws EOFException
     */
    private String getFilename() throws EOFException {
        Scanner scanner = new Scanner(System.in);
        boolean validFile = false;
        String normalizedFilename = null;
        // getting and validating filename
        while (!validFile) {
            System.out.println("--- Provide filename ---");
            String filename = scanner.nextLine();

            // normalize the filename input
            normalizedFilename = Normalizer.normalize(filename, Normalizer.Form.NFKC);

            // check the filename only contains allowed characters
            Pattern filePattern = Pattern.compile("^[\\\\/.-_:a-zA-Z0-9]{1,255}$");
            Matcher matcher = filePattern.matcher(normalizedFilename);

            // checks if the file actually exists
            File file = new File(normalizedFilename);
            if (file.exists() && file.isFile() && matcher.find()) {
                validFile = true;
            } else if (!file.exists() || file.isFile()) {
                if (file.getParentFile() != null) {
                    file.getParentFile().mkdirs();
                }
                validFile = matcher.find();
            }
        }
        return normalizedFilename;
    }

    /**
     * Password validation
     * Helper method
     * 
     * @param password
     * @return boolean
     */
    private boolean validatePassword(char[] password) {
        return (password.length >= 6 && password.length <= 30);
    }

    /**
     * @param keyStore
     * 
     * TEST HELPER
     * sets the keystore class instance
     */
    public void setKeyStore(KeyStore keyStore) {
        ks = keyStore;
    }

    /**
     * Private class containing digital signature generation and verification helper
     * methods
     */
    private class SignatureHelper {

        /**
         * @param algorithm
         * @param privatekey
         * @param message
         * @return byte[]
         * @throws UnsupportedEncodingException
         * @throws SignatureException
         * @throws InvalidKeyException
         * @throws NoSuchAlgorithmException
         * @throws Exception
         * Method for generating digital signature.
         */
        public byte[] generateSignature(String algorithm, PrivateKey privatekey, String message)
                throws SignatureException, UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
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
         * @param signature
         * @param pk
         * @param alg
         * @param msg
         * @return boolean
         * @throws Exception
         * Method for verifying digital signature.
         */
        public boolean verifySignature(byte[] signature, PublicKey pk, String alg, String msg) throws Exception {
            // Create an instance of the signature scheme for the given signature algorithm
            Signature sig = Signature.getInstance(alg);

            // Initialize the signature verification scheme.
            sig.initVerify(pk);

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
}