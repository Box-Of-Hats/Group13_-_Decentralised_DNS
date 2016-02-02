package group13Project.security;


import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.X509EncodedKeySpec;


public class Cryptography {
	private PublicKey publicKey;
	private PrivateKey privateKey;
	private Signature sig;
	private final int minStrength = 512;
	private final int normStrength = 1024;
	private byte[] encMessage;
	
	public Cryptography() throws Exception {
		try {
			
			//create a Random value
			final SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			//Create a RSA public a private key
			final KeyPairGenerator key = KeyPairGenerator.getInstance("DSA", "SUN");
			key.initialize(this.normStrength, random);
			
			//Assign key pair to pair
			KeyPair pair = key.genKeyPair();
			
			//Assign private key to class attribute
			this.privateKey = pair.getPrivate();
			//Assign public key to class attribute
			this.publicKey = pair.getPublic();
			
			//Create Digital Signature
			this.sig = Signature.getInstance("SHA1withDSA", "SUN");
			this.sig.initSign(this.privateKey);
			
			
			
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public Cryptography(int strength) throws Exception {
		try {
			
			//create a Random value
			final SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			
			//Create a RSA public a private key
			final KeyPairGenerator key = KeyPairGenerator.getInstance("DSA", "SUN");
			
			if(strength > this.minStrength)
				key.initialize(strength, random);
			else
				key.initialize(this.normStrength, random);
			
			//Assign key pair to pair
			KeyPair pair = key.genKeyPair();
			
			//Assign private key to class attribute
			this.privateKey = pair.getPrivate();
			//Assign public key to class attribute
			this.publicKey = pair.getPublic();
			
			//Create Digital Signature
			this.sig = Signature.getInstance("SHA1withDSA", "SUN");
			this.sig.initSign(this.privateKey);

		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	  * Encrypt message using private key.
	  * 
	  * @param message
	  *          :encrypted message
	  *
	  * @return encrypted message
	  * @throws SignatureException 
	  */
	public byte[] encrypt(String message) throws SignatureException {
		
		
		//add message to Signature
		sig.update(message.getBytes());
		
		//Sign Signature
		this.encMessage = sig.sign();
		
		//Return Signed message
		return this.encMessage;
	}
	/**
	 * Check if the integrity of the message
	 * 
	 * @param signature
	 * 			:boolean isVerified
	 * 
	 * @return boolean
	 * @throws SignatureException 
	 */
	public boolean verifySig(byte[] signature) {
		try {
			
			boolean verifies = false;
			// Check if signature is intact 
			return verifies;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			
		} 
	}
}

