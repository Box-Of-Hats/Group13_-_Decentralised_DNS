package group13Project;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
	
	private boolean hasConfig = false;
	private String configFolderDir;
	private File config = null;
	private boolean setup;
	
	
	
	
	
	public Config() {
		
		//check if config file exist on local system
		setConfigFolderDir(System.getProperty("user.home") + File.separator+ "Desktop/group13ProjectFolder");
		File configFile = new File(System.getProperty("user.home"), "Desktop"+ File.separator+ "group13ProjectFolder" + File.separator +"config.properties");
		if(configFile.exists()) {
			setConfig(configFile);
			setHasConfig(true);
		}
		
		
	}
	private void setConfigFolderDir(String dir) {
		this.configFolderDir = dir;
	}
	private void setHasConfig(Boolean hasConfig) {
		this.hasConfig = hasConfig;
	}
	public void createConfig() throws IOException {
		if(this.hasConfig == false) {
			
			File dir = new File(this.configFolderDir);
			File config = new File(dir ,  File.separator+"config.properties");
			
			//Check if folder is created
			if(!dir.exists()) {
				
				//make new directory
				dir.mkdirs();
				
				try {
					setHasConfig(config.createNewFile());
					setConfig(config);
					this.setup = false;
					
				} catch(IOException e) {
					e.printStackTrace();
				}
			} else if(!config.exists()) {
				try {
					setHasConfig(config.createNewFile());
					setConfig(config);
					Properties properties = new Properties();
					properties.load(new FileInputStream(this.configFolderDir + File.separator + "config.properties"));
					if(properties.getProperty("setup") == "")
						this.setup = false;
					else
						this.setup = true;
					
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}
	public void updateStatus(String[] values) {
		
		// loop through each key-value pair
		
		// write to properties file
		
	}
	public void addProp(String keyName, String keyValue) {
		Properties prop = new Properties();
		try  {
			prop.load(new FileInputStream(this.configFolderDir + File.separator + "config.properties"));
			prop.setProperty(keyName, keyValue);
			prop.store(new FileOutputStream(this.configFolderDir + File.separator + "config.properties"), "Added or updated a properity.");
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public File getConfig(){
		return this.config;
	}
	public void setConfig(File f) {
		this.config = f;
	}
	public boolean hasConfig(){
		return hasConfig;
	}
	public boolean isSetUp() {
		return this.setup;
	}
}
