package datacomproject.mqttclientapp.KeyStore;

import java.io.FileInputStream;
import java.security.cert.Certificate;
import java.security.*;

public class KeyStoreHelper {

	private char[] password;
	private KeyStore ks;

	/**
	 * Load keyStore from filename and password user input
	 * 
	 * @param password
	 * @param filename
	 * @return KeyStore
	 * @throws Exception
	 */
	public KeyStore loadKeyStore(char[] password, String filename) throws Exception {
		this.password = password;
		System.out.println("loading the key store");
		System.out.println("---------------------");
		this.ks = KeyStore.getInstance(KeyStore.getDefaultType());
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
		System.out.println("------Getting the Certificate-------");
		Certificate trustedCertificate = ks.getCertificate(alias);
		System.out.println("Trusted Certificate has been retrieved");
		return trustedCertificate;
	}

	/**
	 * Gets the private key from given certificate at the specified alias
	 * 
	 * @param password
	 * @param alias
	 * @return PrivateKey
	 * @throws Exception
	 */
	public PrivateKey extractPrivateKey(String alias) throws Exception {
		System.out.println("------Getting the Private Key-------");
		KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(password);
		KeyStore.PrivateKeyEntry privateKeyEntry = null;

		// get private key from keystore
		privateKeyEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias, protParam);
		PrivateKey privateKey = privateKeyEntry.getPrivateKey();
		System.out.println("Private key has been retrieved");
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
	public boolean storeCertificate(Certificate certificate, String alias) {
		System.out.println("Storing the certificate");
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
		boolean validAlias = ks.containsAlias(alias);
		if (!validAlias) {
			throw new IllegalArgumentException("Alias given is invalid");
		}
		return true;
	}
}