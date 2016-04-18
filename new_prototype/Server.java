import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
	public Server(Node node)
	{
		try{
			serverSocket = new ServerSocket(PORT);
			this.node = node;
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	//Connect client and set IO up if there is any connection request
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

	public void recieveString(String message){
        //Called by the Server of the node, whenever a message is recieved.
        //Structure for messages will be "AAA,DATA"
        //'AAA' is the method code that will be used
        //'DATA' is the data that will be passed to the relevant method
        if (message != null){ 
            System.out.println("Node Recieved Message: " + message);
            String[] parts = message.split(",");
            String part1 = parts[0];
            String part2 = parts[1];
            System.out.println("\tPart1: " + part1);
            System.out.println("\tPart2: " + part2);

            switch (part1){
                //Add cases here for the different requests that a Node should be able to recieve:
                //part2 is the data to be processed for the function.
                case "SPD": 
                			break;
                case "UFT": 
                			break;
            }
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

	//Send message to remotely-connected client
	public void pushMessage(String string){
			out.println(string);
	}

	public void run()
	{
		String str_in,str_out;
		this.acceptConnectionRequest();

		while(true){
			str_in = this.pullMessage();
			//str_out = "New Message Received: " + str_in;
			recieveString(str_in);
			//System.out.println(str_out);
			if (str_in==null) break;
		}
		
	}

	/*
	                                    
	Now, that I have your attention:
		When adding the getter methods for the Server's Node, be sure to make them syncronised since the Server utilises multithreading.
		Thanks! :)

	*/

}
