import java.net.*;
import java.util.*;

public class GetLocalIP {
	public static InetAddress get(){
		InetAddress ip = null;
		try{
			Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
			for (NetworkInterface netint : Collections.list(nets)) {
        			Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        			for (InetAddress inetAddress : Collections.list(inetAddresses)) {
	        			if (!inetAddress.isAnyLocalAddress() && !inetAddress.isLinkLocalAddress() && !inetAddress.isLoopbackAddress() && !inetAddress.isMulticastAddress()) {
	        				ip = inetAddress;
	        				break;
	        			}
	        		}
        		}	
		}catch(Exception e){
		}
		return ip;
	}
}
