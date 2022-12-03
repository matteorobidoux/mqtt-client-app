package datacomproject.KeyStore;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.ECGenParameterSpec;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import datacomproject.mqttclientapp.KeyStore.SignatureHelper;

public class SignatureHelperTest {

	PublicKey publicKey;
	PrivateKey privateKey;
	SignatureHelper sighelp;

	public SignatureHelperTest() throws Exception {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
		ECGenParameterSpec ecParaSpec = new ECGenParameterSpec("secp256r1");
		generator.initialize(ecParaSpec);
		KeyPair keys = generator.generateKeyPair();

		publicKey = keys.getPublic();
		privateKey = keys.getPrivate();
		sighelp = new SignatureHelper();
	}

	@Test
	public void testSignVerifySignature() throws Exception {
		String algorithm = "SHA256withECDSA";

		String messageToBeSigned = "This is the message to be signed.";
		String receivedMsg = "This is the message to be signed.";

		byte[] signature = sighelp.signMessage(privateKey, messageToBeSigned);
		assertTrue(sighelp.verifySignature(signature, publicKey, algorithm, receivedMsg));
	}

	@Test
	public void testFailSignVerifySignature() throws Exception {
		String algorithm = "SHA256withECDSA";

		String messageToBeSigned = "This is the message to be signed.";
		String receivedMsg = "Message to fail signature";

		byte[] signature = sighelp.signMessage(privateKey, messageToBeSigned);
		assertFalse(sighelp.verifySignature(signature, publicKey, algorithm, receivedMsg));
	}
}
