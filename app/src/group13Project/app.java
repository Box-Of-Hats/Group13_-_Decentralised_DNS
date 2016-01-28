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
		if(internetConf.hasValidInternetConnection()){
			frame.updateStatus("Checking Configuration");
			Config conf = new Config();
			
			if(conf.hasConfig() == false) {
				
				conf.createConfig();
				
			}
			File config = conf.getConfig();
			
			if(frame.newDeviceView(internetConf.getDeviceIPAddressToString())) {
				Cryptography crypt = new Cryptography();
				byte[] a = crypt.encrypt("Hello World");
				System.out.println(a);
				System.out.println(crypt.verifySig(a));
			}
			
			
			//set up new node
			
		}
	}
}
