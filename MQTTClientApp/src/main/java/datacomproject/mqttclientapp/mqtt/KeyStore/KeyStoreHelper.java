package datacomproject.mqttclientapp.mqtt.KeyStore;

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

/**
 * @author Rim Dallali
 */
public class KeyStoreHelper {

    private String filepath;
    private char[] password;
    private KeyStore ks;

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
                    password = console.readPassword();
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
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        FileInputStream fis = new FileInputStream(filename);
        ks.load(fis, password);
        System.out.println("KeyStore has been loaded");
        return ks;
    }

    /**
     * Get trusted certificate from keyStore at the given alias
     * 
     * @param alias
     * @return Certificate
     * @throws KeyStoreException
     */
    public Certificate extractCertificate(String alias) throws KeyStoreException {
        System.out.println("Get Trusted Certificate");
        System.out.println("-----------------------");

        Certificate trustedCertificate = ks.getCertificate(alias);
        return trustedCertificate;
    }

    /**
     * Gets the private key from given certificate at the specified alias
     * 
     * @param password
     * @param alias 
     * @return PrivateKey
     * @throws Exception
     * TODO can we pass the certificate instead of the alias
     */
    public PrivateKey extractPrivateKey(String alias, char[] password) throws Exception {
        System.out.println("------Getting the Private Key-------");
        System.out.println("------------------------------------");
        KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(password);

        KeyStore.PrivateKeyEntry privateKeyEntry = null;

        // get private key from keystore 
        privateKeyEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias, protParam); 
        PrivateKey privateKey = privateKeyEntry.getPrivateKey();
        return privateKey;
    } 

    /**
     * Store the input certificate to the keystore at input alias
     * returns a boolean representing whether the store succeeded or not
     * 
     * @param alias
     * @param certificate
     * @return boolean
     */
    public boolean storeCertificate(String alias, Certificate certificate) {
        System.out.println("Storing the certificate");
        System.out.println("-----------------------");
        try {
            ks.setCertificateEntry(alias, certificate);
            System.out.println("certificate alias entry has been set\nalias: " + alias + "\ncertificate: "
                + certificate.getPublicKey().toString());
            return true;
        } catch (KeyStoreException e) {
            return false;
        } 
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
}