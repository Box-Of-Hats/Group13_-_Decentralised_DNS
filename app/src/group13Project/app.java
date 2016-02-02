package group13Project;

import group13Project.GUI.*;
import group13Project.security.Cryptography;

import java.io.File;
import java.io.IOException;



public class app {
	public static void main(String[] args) throws Exception{
		
		GUIMainFrame frame = new GUIMainFrame(true);
		
		Internet internetConf = new Internet();
		frame.updateStatus("Checking Internet Connection");
		if(internetConf.hasValidInternetConnection()) {
			frame.updateStatus("Checking Configuration");
			Config conf = new Config();
			
			if(conf.isSetUp() == true) {
				
				/*
				 * 
				 * Existing User
				 * 
				 */
				
				//conf.getAllDetails();
				
				
			} else if(conf.isSetUp() == false && conf.hasConfig() == false) {
				
				
				/*
				 * 
				 *  A new User joins the network
				 *  
				 *  
				 */
				
				conf.createConfig();
				
				
				
				//Where you setup the chord system.
			} else {
				
				/*
				 * 
				 * New user adding personal details
				 * 
				 */
				
				frame.newDeviceView(internetConf.getDeviceIPAddressToString());
				if(frame.newDeviceSetUp()) {
					
					//save all new details into properties
					String[] newDetails = frame.getAllDetails();
					conf.addProp("forename", newDetails[0]);
					conf.addProp("surname", newDetails[2]);
					conf.addProp("alias", newDetails[3]);
					conf.addProp("IP", internetConf.getDeviceIPAddressToString());
				}
			}
			
			
		
			
		} else {
			frame.updateStatus("No Internget connection available");
		}
	}
}
