import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;

public class DigitalSignature{
	private static Signature dsa;
	private static KeyPairGenerator keyGen;
	private static SecureRandom random;
	private static KeyPair pair;
	private static PrivateKey priKey;
	private static PublicKey pubKey;
	
	public DigitalSignature(){
		this(1024);
	}

	public DigitalSignature(int keysize){
		try{
			dsa = Signature.getInstance("SHA1withDSA");
			keyGen = KeyPairGenerator.getInstance("DSA");
			random = SecureRandom.getInstance("SHA1PRNG");
			keyGen.initialize(keysize, random);
			pair = keyGen.generateKeyPair();
			priKey = pair.getPrivate();
		}catch(Exception e){
		}
	}

	public PublicKey getPublicKey(){
		return pair.getPublic();
	}

	public void setPublicKey(PublicKey publicKey){
		pubKey = publicKey;
	}

	public byte[] sign(byte[] dataBytes){
		byte[] signature = null;
		try{
			dsa.initSign(priKey);
			dsa.update(dataBytes);
			signature = dsa.sign();
		}catch(Exception e){
			
		}
		return signature;
	}

	public boolean verify(byte[] signature, byte[] dataBytes){
		boolean matched = false;
		try{
			dsa.initVerify(pubKey);
			dsa.update(dataBytes);
			matched = dsa.verify(signature);
		}catch(Exception e){
			
		}
		return matched;
	}

}
