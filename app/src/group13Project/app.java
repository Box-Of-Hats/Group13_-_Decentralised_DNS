package group13Project;

import group13Project.GUI.*;

import java.io.File;
import java.io.IOException;



public class app {
	public static void main(String[] args) throws IOException{
		
		GUIMainFrame frame = new GUIMainFrame(true);
		
		Internet internetConf = new Internet();
		frame.updateStatus("Checking Internet Connection");
		if(internetConf.hasValidInternetConnection()){
			frame.updateStatus("Checking Configuration");
			Config conf = new Config();
			
			if(conf.hasConfig() == false) {
				
				conf.createConfig();
				
			}
			File config = conf.getConfig();
			
			if(frame.newDeviceView(internetConf.getDeviceIPAddressToString())) {
				Cryptography crypt = new Cryptography();
			}
			
			
			//set up new node
			
		}
	}
}
