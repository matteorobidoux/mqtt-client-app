package datacomproject.mqttclientapp;

import java.io.Console;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.text.Normalizer;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;

import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;

import datacomproject.mqttclientapp.KeyStore.KeyStoreHelper;
import datacomproject.mqttclientapp.mqtt.MQTT;
import datacomproject.mqttclientapp.sensors.*;

/**
 *
 * @author Rim Dallali
 */
public class App {

    public KeyStoreHelper ksh = new KeyStoreHelper();
    public static MQTT mqtt = new MQTT();

    public static void main(String[] args) throws Exception {
        App app = new App();
        
        app.initializeKeyStore();
        app.initializeMQTT();
        // mqtt.retrieveMessage();

        app.displayData();
    }

    public void initializeMQTT() throws KeyStoreException, CertificateEncodingException, JSONException, UnsupportedEncodingException {
        // mqtt = new MQTT();
        System.out.println("here1");
        Mqtt5BlockingClient client = mqtt.getMqttClient();
        System.out.println("here2");
        String alias = getUserAlias();
        mqtt.createConnection("rimdallali", "password");
        PublicKey publicKey = ksh.extractCertificate(alias).getPublicKey();
        mqtt.retrieveMessage(publicKey);
        mqtt.subscribe();
        mqtt.publishCertificateMessage(ksh.extractCertificate(alias));
        System.out.println(mqtt.certificates);
    }

    public void displayData() {
        // getting temperature and humidity data
        System.out.println("Capturing temperature and humidity data...");
        DHTSensor dht_sensor = new DHTSensor();
        dht_sensor.startThread();

        // getting doorbell data if pressed
        System.out.println("getting doorbell data if pressed...");
        DoorbellButton doorbell_button = new DoorbellButton();
        doorbell_button.startThread();

        // getting doorbell data if pressed
        System.out.println("getting motion sensor data if detected...");
        MotionSensor motion_sensor = new MotionSensor();
        motion_sensor.startThread();
    }

    public void initializeKeyStore() throws Exception {
        this.getUserInput();
        // boolean validAlias = false;
        String alias = getUserAlias();

        Certificate certificate = ksh.extractCertificate(alias);
        PrivateKey privateKey = ksh.extractPrivateKey(alias);
        System.out.println("------Getting the Public Key-------");
        PublicKey publicKey = certificate.getPublicKey();
        System.out.println("Public Key has been retrieved");
    }

    public void getUserInput() throws Exception {
        Console console = System.console();

        // getting and validating filename
        System.out.println("------------------------------------------------");
        System.out.println("-- Provide path where the keystore is located --");
        boolean validKeyStore = false;
        while (!validKeyStore) {
            try {
                String filepath = getFilename();
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
                    ksh.loadKeyStore(password, filepath);
                }
            } catch (EOFException e) {
                System.out.println("Given file path is INVALID");
                validKeyStore = false;
            } catch (IOException ie) {
                System.out.println("Wrong KeyStore password OR Invalid KeyStore path, try again");
                validKeyStore = false;
            }
        }
    }

    public String getUserAlias() throws KeyStoreException {
        boolean validAlias = false;
        Scanner scanner = new Scanner(System.in);
        String normalizedAlias = null;
        while (!validAlias) {
            try {
                // getting and validating alias
                System.out.println("---------------- Provide alias -----------------");
                String alias = scanner.nextLine();
                // normalize the alias input
                normalizedAlias = Normalizer.normalize(alias, Normalizer.Form.NFKC);
                validAlias = ksh.checkAlias(normalizedAlias);
                scanner.close(); 
                if (!validAlias) {
                    System.out.println("Invalid Alias, please try again");
                } else {
                    validAlias = true;
                }
            } catch (IllegalArgumentException e) {
                validAlias = false;
            }
        }
        return normalizedAlias;
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
        System.out.println(normalizedFilename);
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
}
