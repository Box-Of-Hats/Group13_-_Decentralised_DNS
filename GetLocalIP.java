import java.net.*;
import java.util.*;

public class GetLocalIP {
	public static void main (String args[]) throws SocketException{
		Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets)) {
            Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        	for (InetAddress inetAddress : Collections.list(inetAddresses)) {
	        	if (!inetAddress.isAnyLocalAddress() && !inetAddress.isLinkLocalAddress() && !inetAddress.isLoopbackAddress() && !inetAddress.isMulticastAddress()) {
	        		System.out.printf("%s", inetAddress);
	        		System.out.println("This is the public address");
	        	}
	        }
        }
	}
}
