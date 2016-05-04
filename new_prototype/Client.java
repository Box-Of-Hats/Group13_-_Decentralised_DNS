import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends NodeClient{
	private final int PORT = 3333;
	private String ip;
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	
	public Client()
	{
	
	}
	
	//connect to the server and return whether it is connected
	public boolean connectToServer(String ip)
	{
		try
		{
			socket = new Socket(ip, PORT);
			out = new PrintWriter(socket.getOutputStream(), true);
    		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch (IOException e)
		{
			//System.out.println(e.getMessage());
		}
		return socket.isConnected();
	}
	
	//disconnect with the server
	public void disconnect()
	{
		try
		{
			in.close();
			out.close();
			socket.close();
			//System.out.println("Socket is closed.");
		}
		catch (IOException e)
		{
			//System.out.println(e.getMessage());
		}
	}
	
	//Send message to the server
	public void pushMessage(String string){
		try{
			out.println(string);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//Receive message from the server
	public String pullMessage(){
		String string = null;
		try{
			string = in.readLine();
		}catch(Exception e){
			e.printStackTrace();
		}
		return string;
	}
	
}
