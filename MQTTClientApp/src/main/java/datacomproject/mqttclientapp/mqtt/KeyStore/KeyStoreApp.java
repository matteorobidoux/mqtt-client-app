package datacomproject.mqttclientapp.mqtt.KeyStore;

import java.util.Base64;
import java.util.Scanner;
import java.io.Console;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.*;
import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.SecretKey;

/**
 * @author Rim Dallali
 */
public class KeyStoreApp {

    /**
     * Access point for MQTT to get desired certificate
     * Get trusted certificate from keystore using input alias
     * 
     * @param alias
     * @throws Exception
     * @return Certificate
     */
    public static Certificate extractCertificate(String alias) throws Exception {
        Scanner scanner = new Scanner(System.in);
        Console console = System.console();

        // getting and validating filename
        System.out.println("------------------------------------------------");
        System.out.println("-- Provide path where the keystore is located --");
        boolean validKeyStore = false;
        String filename = null;
        while (!validKeyStore) {
            try {
                filename = getFilename();
                validKeyStore = true;
                if (validKeyStore) {
                    // getting and validating password
                    System.out.println("-------- Provide password --------");
                    char[] password = console.readPassword();
                    while (!validatePassword(password)) {
                        System.out.println("--- Invalid password, try again ---");
                        password = console.readPassword();
                    }
                    //loading the keystore and getting the certificate
                    KeyStore ks = loadKeyStore(password, filename, alias);
                    return getKeyStoreInfo(ks, password, filename, alias);
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
        return null;
    }

    /**
     * Access point for MQTT to store given certificate
     * Stores the given certificate to the given alias in the keystore
     * 
     * @param alias
     * @param certificate
     * @throws Exception
     */
    public static void storeCertificate(String alias, Certificate certificate) throws Exception {
        Scanner scanner = new Scanner(System.in);
        Console console = System.console();

        // getting and validating filename
        System.out.println("------------------------------------------------");
        System.out.println("-- Provide path where the keystore is located --");
        boolean validKeyStore = false;
        String filename = null;
        while (!validKeyStore) {
            try {
                filename = getFilename();
                validKeyStore = true;
                if (validKeyStore) {
                    // getting and validating password
                    System.out.println("-------- Provide password --------");
                    char[] password = console.readPassword();
                    while (!validatePassword(password)) {
                        System.out.println("--- Invalid password, try again ---");
                        password = console.readPassword();
                    }
                    System.out.println(filename);

                    KeyStore ks = loadKeyStore(password, filename, alias);
                    storeToKeyStore(ks, alias, certificate, password);
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
     * Helper for MQTT to extract public key from input certificate
     * @param certificate
     * @return PublicKey
     */
    public static PublicKey extractPublicKey(Certificate certificate) {
        PublicKey publicKey = certificate.getPublicKey();
        return publicKey;
    }

    /**
     * Password validation
     * 
     * @param password
     * @return boolean
     */
    private static boolean validatePassword(char[] password) {
        return (password.length >= 6 && password.length <= 30);
    }

    /**
     * Load keyStore from filename and password user input
     * 
     * @param password
     * @param filename
     * @return KeyStore
     * @throws Exception
     */
    private static KeyStore loadKeyStore(char[] password, String filename, String alias) throws Exception {
        System.out.println("loading the key store");
        System.out.println("------------------------------------");
        FileInputStream fis = null;
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType(), "SunEC");
        fis = new FileInputStream(filename);
        ks.load(fis, password);
        System.out.println("KeyStore has been loaded");
        return ks;
    }

    /**
     * Get trusted certificate from keyStore at the given alias
     * 
     * @param ks
     * @param password
     * @param filename
     * @return Certificate
     * @throws Exception
     */
    private static Certificate getKeyStoreInfo(KeyStore ks, char[] password, String filename, String alias) throws Exception {
        System.out.println("Get Trusted Certificate");
        System.out.println("------------------------------------");

        // get private key and trusted certificate from keystore
        Certificate trustedCertificate =  ks.getCertificate(alias);;
        return trustedCertificate;
    }

    /**
     * Store the input certificate to the keystore at input alias
     * 
     * @param keyStore
     * @param alias
     * @param certificate
     * @param password
     * @throws Exception
     */
    private static void storeToKeyStore(KeyStore keyStore, String alias, Certificate certificate, char[] password) throws Exception {
        System.out.println("Storing the certificate");
        System.out.println("------------------------------------");
        keyStore.setCertificateEntry(alias, certificate);
        System.out.println("certificate alias entry has been set\nalias: " + alias + "\ncertificate: " + certificate.getPublicKey().toString());
    }

    /**
     * Sign and Verify a string message
     * 
     * @param publicKey
     * @param privateKey
     * @param password
     * @param secretKey
     * @throws Exception
     */
    private static void signMessage(PublicKey publicKey, PrivateKey privateKey, char[] password, SecretKey secretKey,
            String message) throws Exception {
        System.out.println("Signing and Verifying a message");
        System.out.println("------------------------------------");
        SignatureHelper sighelp = new SignatureHelper();

        String algorithm = "SHA256withECDSA";
        // Generate signature for the message.
        byte[] signature = sighelp.generateSignature(algorithm, privateKey, message);
        System.out.println("\nSignature: " + Base64.getEncoder().encodeToString(signature));
    }

    /**
     * Verify digital signature given as input
     * 
     * @param signature
     * @param publicKey
     * @param algorithm
     * @param receivedMsg
     * @param sighelp
     * @return boolean
     * @throws Exception
     */
    private static boolean verifySignature(byte[] signature, PublicKey publicKey, String algorithm, String receivedMsg,
            SignatureHelper sighelp) throws Exception {
        boolean verified = sighelp.verifySignature(signature, publicKey, algorithm, receivedMsg);
        if (verified) {
            System.out.println("message has been verified");
        } else {
            System.out.println("message has NOT been verified");
        }
        return verified;
    }

    /**
     * Prompts the user to enter a filename for where to store the secret key
     * Helper method
     * 
     * @return String
     * @throws EOFException
     */
    private static String getFilename() throws EOFException {
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
}
