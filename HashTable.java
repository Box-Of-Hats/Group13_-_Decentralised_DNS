import java.net.InetAddress;
import java.net.UnknownHostException;

public class HashTable {
	
	int arraySize = 0;
	static String[] hashtable;
	
	public static void main( String args[]) throws UnknownHostException {
		HashTable ht = new HashTable(30);

		String[] elementsToAdd2 = { "192.168.0.1","192.168.0.2","192.168.0.3", "192.168.0.4","192.168.0.5",
									"192.168.0.6","192.168.0.7","192.168.0.8", "192.168.0.9", "192.168.0.10",
									"192.168.0.11","192.168.0.12","192.168.0.13", "192.168.0.14","192.168.0.15",
									"192.168.0.16","192.168.0.17","192.168.0.18", "192.168.0.19", "192.168.0.20",
									"192.168.0.21","192.168.0.22","192.168.0.23", "192.168.0.24","192.168.0.25",
									"192.168.0.26","192.168.0.27","192.168.0.28", "192.168.0.29", "192.168.0.30"
				};
		ht.setValue(elementsToAdd2, hashtable);
		ht.displayTheStack();
	}
	HashTable(int size) {
		arraySize = size;
		//creates string array of size elements
		hashtable = new String[size];
		
	}
	public void setValue(String[] elementsToSet, String[] hashtable) throws UnknownHostException {
		for(int i = 0; i < elementsToSet.length; i++) {
			
			int ipAsInt = ipToInt(InetAddress.getByName(elementsToSet[i]).getAddress());
			int index = ipAsInt % (arraySize - 1);
			System.out.println(ipAsInt);
			
			while(hashtable[index] != null) {
				//if collision has occurred, try next index up, index + 1
				++index;
				
				//if no empty index, go back to 0
				index %= arraySize;
			}
			hashtable[index] = elementsToSet[i];
		}
	}
	public int ipToInt(byte[] bytes) {
		int val = 0;
		
		for(int i = 0; i < bytes.length; i++ ) {
			val <<= 8;
			val |= bytes[i] & 0xff;
		}
		return val *= -1;
			 
	}
	public void displayTheStack() {

		int increment = 0;

		for (int m = 0; m < 3; m++) {

			increment += 10;

			for (int n = 0; n < 71; n++)
				System.out.print("-");

			System.out.println();

			for (int n = increment - 10; n < increment; n++) {

				System.out.format("| %3s " + " ", n);

			}

			System.out.println("|");

			for (int n = 0; n < 71; n++)
				System.out.print("-");

			System.out.println();

			for (int n = increment - 10; n < increment; n++) {

				if (hashtable[n].equals("-1"))
					System.out.print("|      ");

				else
					System.out
							.print(String.format("| %3s " + " ", hashtable[n]));

			}

			System.out.println("|");

			for (int n = 0; n < 71; n++)
				System.out.print("-");

			System.out.println();

		}

	}
}
