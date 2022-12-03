package datacomproject.mqttclientapp;

import java.io.Console;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import datacomproject.mqttclientapp.JavaFX.FXScreen;
import datacomproject.mqttclientapp.JavaFX.TilesFXApp;
import datacomproject.mqttclientapp.KeyStore.KeyStoreHelper;
import datacomproject.mqttclientapp.mqtt.MQTT;
import datacomproject.mqttclientapp.sensors.*;

public class ConsoleApp {

	private KeyStoreHelper ksh = new KeyStoreHelper();
	private MQTT mqtt = new MQTT();
	private String username;
	private String password;
	private PrivateKey privateKey;
	private String alias;
	private Console console = System.console();
	private TilesFXApp gui = new TilesFXApp();

	/**
	 * Promp the user for MQTT credentials until a connection can be established
	 * Subscribes to topics and publishes the certificate and starts retrieving
	 * published data
	 */
	public void initializeMQTT() throws Exception {
		mqtt.getMqttClient();
		boolean validCred = false;
		while (!validCred) {
			getMQTTUserInput();
			validCred = mqtt.createConnection(this.username, this.password);
			if (!validCred) {
				System.out.println("- Invalid credentials, try again -");
			}
		}

		mqtt.setVariables(gui.fxScreen, ksh);
		mqtt.subscribe();
		mqtt.publishCertificateMessage(ksh.extractCertificate(alias));
		mqtt.retrieveMessage();
	}

	/**
	 * Start capturing data from the sensors in separate threads
	 * 
	 * @param fxScreen
	 */
	public void captureData(FXScreen fxScreen) {
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

	/**
	 * Prompts the user for keystore information and sets private & public keys
	 * 
	 * @throws Exception
	 */
	public void initializeKeyStore() throws Exception {
		this.getKeyStoreUserInput();
		this.alias = getUserAlias();

		privateKey = ksh.extractPrivateKey(alias);
	}

	/**
	 * Get MQTT credentials from user input
	 */
	public void getMQTTUserInput() {
		System.out.println("------------------------------------------------");
		String user = getMQTTUsername();
		System.out.println("-------- Provide password --------");
		char[] pswd = console.readPassword();

		this.password = new String(pswd);
		this.username = user;
	}

	/**
	 * Get Keystore credentials from user input
	 * 
	 * @throws Exception
	 */
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

	/**
	 * Prompts the user to get their MQTT username
	 * 
	 * @returns String
	 */
	public String getMQTTUsername() {
		String normalizedUsername = null;
		System.out.println("----------- Enter your MQTT username -----------");
		String user = console.readLine();
		// normalize the username input
		normalizedUsername = Normalizer.normalize(user, Normalizer.Form.NFKC);
		return normalizedUsername;
	}

	/**
	 * Prompts the user to enter their keystore alias
	 * 
	 * @return String
	 * 
	 * @throws KeyStoreException
	 */
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
	 * Prompts the user to enter a filepath for where the keystore is stored
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
