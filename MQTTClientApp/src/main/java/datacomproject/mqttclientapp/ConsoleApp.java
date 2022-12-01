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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;

import datacomproject.mqttclientapp.JavaFX.FXScreen;
import datacomproject.mqttclientapp.JavaFX.TilesFXApp;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import datacomproject.mqttclientapp.KeyStore.KeyStoreHelper;
import datacomproject.mqttclientapp.mqtt.MQTT;
import datacomproject.mqttclientapp.sensors.*;

/**
 * RimDallali Rim20021
 *
 * ../KeyStore/ECcertif.ks password DEMO
 */
/**
 *
 * @author Rim Dallali
 */
public class ConsoleApp {

    public KeyStoreHelper ksh = new KeyStoreHelper();
    public MQTT mqtt = new MQTT();
    public String username;
    public String password;
    public PublicKey publicKey;
    public PrivateKey privateKey;
    public String alias;
    Console console = System.console();
    TilesFXApp gui = new TilesFXApp();

    public void initializeMQTT() throws KeyStoreException, CertificateEncodingException, JSONException, UnsupportedEncodingException {
        Mqtt5BlockingClient client = mqtt.getMqttClient();
        boolean validCred = false;
        while (!validCred) {
            getMQTTUserInput();
            validCred = mqtt.createConnection("rimdallali", "password");
        }

        mqtt.retrieveFX(gui.fxScreen);
        mqtt.subscribe();
        mqtt.publishCertificateMessage(ksh.extractCertificate(alias));

        boolean messageRetrieved = false;
        mqtt.retrieveMessage();

        while (messageRetrieved == false) {
            System.out.println(mqtt.certificates.size());
            if (mqtt.certificates.size() > 0) {
                System.out.println(mqtt.certificates.get(0));
                messageRetrieved = true;
            }
        }
        System.out.println(mqtt.certificates);
    }

    public void displayData(FXScreen fxScreen) {
        // getting temperature and humidity data
        System.out.println("Capturing temperature and humidity data...");
        DHTSensor dht_sensor = new DHTSensor();
        dht_sensor.startThread(fxScreen, mqtt, privateKey);

        // getting doorbell data if pressed
        System.out.println("getting doorbell data if pressed...");
        DoorbellButton doorbell_button = new DoorbellButton();
        doorbell_button.startThread(fxScreen, mqtt, privateKey);

        // getting doorbell data if pressed
        System.out.println("getting motion sensor data if detected...");
        MotionSensor motion_sensor = new MotionSensor();
        motion_sensor.startThread(fxScreen, mqtt, privateKey);
    }

    public void initializeKeyStore() throws Exception {
        this.getKeyStoreUserInput();
        this.alias = getUserAlias();

        Certificate certificate = ksh.extractCertificate(this.alias);
        privateKey = ksh.extractPrivateKey(alias);
        System.out.println("------Getting the Public Key-------");
        publicKey = certificate.getPublicKey();
        System.out.println("Public Key has been retrieved");
    }

    public void getMQTTUserInput() {
        // getting and validating username
        System.out.println("------------------------------------------------");
        String user = getMQTTUsername();
        System.out.println("-------- Provide password --------");
        char[] pswd = console.readPassword();

        System.out.println("- Invalid credentials, try again -");
        this.password = new String(pswd);
        this.username = user;
    }

    public void getKeyStoreUserInput() throws Exception {
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

    public String getMQTTUsername() {
        String normalizedUsername = null;
        System.out.println("----------- Enter your MQTT username -----------");
        String user = console.readLine();
        // normalize the username input
        normalizedUsername = Normalizer.normalize(user, Normalizer.Form.NFKC);
        return normalizedUsername;
    }

    public String getUserAlias() throws KeyStoreException {
        boolean validAlias = false;
        String normalizedAlias = null;
        while (!validAlias) {
            try {
                // getting and validating alias
                System.out.println("---------------- Provide alias -----------------");
                String userAlias = console.readLine();
                // normalize the alias input
                normalizedAlias = Normalizer.normalize(userAlias, Normalizer.Form.NFKC);
                validAlias = ksh.checkAlias(normalizedAlias);
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
        boolean validFile = false;
        String normalizedFilename = null;
        // getting and validating filename
        while (!validFile) {
            System.out.println("--- Provide filename ---");
            String filename = console.readLine();

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
     * Password validation Helper method
     *
     * @param password
     * @return boolean
     */
    private boolean validatePassword(char[] password) {
        return (password.length >= 6 && password.length <= 30);
    }
}
