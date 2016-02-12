import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TestInet {
	public static void main(String[] args){
		Server server = new Server();
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		String sysin = null;
		String sysout;
		new Thread(server).start();
		Client client = new Client();
		client.connectToServer("127.0.1.1");
		try{
			sysin = bf.readLine();
		}catch(Exception e){
		}
		while(!sysin.isEmpty()){
			client.pushMessageToServer(sysin);
			sysout = client.pullMessageFromServer();
			try{
			sysin = bf.readLine();	
			System.out.println(sysin);
			}catch(Exception e){
			}
		}
		
		client.pushMessageToServer(sysin);
		// System.out.println(client.pullMessageFromServer());
	}
}
