import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class Hashing{
	private final static String DEFAULT_METHOD =  "SHA-1";
	private static String hashMethod;
	private static MessageDigest md;
	
	public static void setMethod(String hashMethod)
	{
		Hashing.hashMethod = hashMethod;
	}

	public static byte[] hash(byte[] key)
	{
		// SHA - 1 implementation
		if (hashMethod == null)
		{
			hashMethod = DEFAULT_METHOD;
		}

		try
		{
			md = MessageDigest.getInstance(hashMethod);
		}
		catch (NoSuchAlgorithmException e)
		{
		}
		synchronized(Hashing.class) {
			md.reset();
			md.update(key);
			return md.digest();
		}
	}
}
