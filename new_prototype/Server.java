import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server extends NodeServer implements Runnable{
	private final int PORT = 3333;
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private BufferedReader in;
	private PrintWriter out;
	private Node node;

	//Initialise the server, set up the serverSocket
	public Server()
	{
		try{
			serverSocket = new ServerSocket(PORT);
			node = new Node();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	//Connect client and set IO up there is any connection request
	public boolean acceptConnectionRequest(){
		
		try{
			clientSocket = serverSocket.accept();
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintWriter(clientSocket.getOutputStream(), true);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return clientSocket.isConnected();
	}

	//Disconnect with the client and turn IO off
	public void disconnect(){
		try{
			in.close();
			out.close();
			clientSocket.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//Receive message from client
	public String pullMessage(){
		String string = null;
		try{
			string = in.readLine();
			}catch(Exception e){
			e.printStackTrace();
		}
		return string;
	}

	//Send message to client
	public void pushMessage(String string){
			out.println(string);
	}

	
	//Handle the message from client
	public void messageHandler(String message){

	}

	public void run()
	{
		String str_in,str_out;
		this.acceptConnectionRequest();

		while(true){
			str_in = this.pullMessage();
			str_out = "New Message Received: " + str_in;
			System.out.println(str_out);
			if (str_in==null) break;
		}
		
	}
}
