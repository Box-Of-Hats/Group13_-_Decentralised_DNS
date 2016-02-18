/*
 * Contributors: Jiaming Ke
*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server implements Runnable{
	private final int PORT = 3333;
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private BufferedReader in;
	private PrintWriter out;
	public String ip;

	public Server()
	{
		try{
			serverSocket = new ServerSocket(PORT);
			ip = Inet4Address.getLocalHost().getHostAddress();
			System.out.println("Server's IP address is: " + ip);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	public Socket acceptConnectionRequest(){
		Socket clientSocket = null;
		try{
			clientSocket = serverSocket.accept();
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintWriter(clientSocket.getOutputStream(), true);
		}catch(Exception e){
			e.printStackTrace();
		}
		return clientSocket;
	}
	
	public String pullMessage(){
		String string = null;
		try{
			string = in.readLine();
			}catch(Exception e){
			e.printStackTrace();
		}
		return string;
	}

	public void pushMessage(String string){
			out.println(string);
	}

	public void closeIOStream(){
		try{
			in.close();
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void run()
	{
		String str_in,str_out;
		this.acceptConnectionRequest();
		str_in = this.pullMessage();
		str_out = "You say: " + str_in;
		System.out.println(str_out);
		this.pushMessage(str_out);
	}
}
