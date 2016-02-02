package group13Project;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

public class Internet {
	private Boolean status;
	
	public Internet() {
		
	}
	
	public Boolean hasValidInternetConnection() throws UnknownHostException, IOException {

		
		/*this could be another node?  Could be changed at a later date.
		 * Using google as it is reliable and up time is > 99.999%
		 */
		
		try {
			//
			try {
				URL url = new URL("http://www.google.com");
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.connect();
				if (con.getResponseCode() == 200){
					this.setStatus(true);
				}
			} catch (Exception exception) {
				this.setStatus(false);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return this.status;
	}
	public String getDeviceIPAddressToString() throws UnknownHostException {
		//Need looking at
		return InetAddress.getLocalHost().getHostAddress();
	}
	private void setStatus (Boolean sat) {
		this.status = sat;
	}
}