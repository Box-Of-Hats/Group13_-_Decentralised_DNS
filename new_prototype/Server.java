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
	private String returnMessage;
	//Initialise the server, set up the serverSocket
	public Server(Node node)
	{
		try{
			serverSocket = new ServerSocket(PORT);
			this.node = node;
		}catch(Exception e){
			//System.out.println(e.getMessage());
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
            //System.out.println("Node Recieved Message: " + message);
            String[] parts = message.split(",");
            String part1 = parts[0];
            String part2 = parts[1];
            //System.out.println("\tPart1: " + part1);
            //System.out.println("\tPart2: " + part2);

            switch (part1){
                //Add cases here for the different requests that a Node should be able to recieve:
                //part2 is the data to be processed for the function.

                //Set Predecessor:
                case "SPD": 
					//System.out.println("SPD request found");
					String[] predecessorDetails = part2.split(";");
					String preIP = predecessorDetails[0];
					int preID = Integer.parseInt(predecessorDetails[1]);
					FingeredNode spdPredecessor = new FingeredNode(preIP, preID);
					node.setPredecessor(spdPredecessor);
					pushMessage("1");
				//Get Predecessor:
				case "GPD":
					//System.out.println("GPD Request Found");
					FingeredNode gpdPredecessor = node.getPredecessor();
					String gpdResponseMessage = "1," + gpdPredecessor.getIp() + ";" + Integer.toString(gpdPredecessor.getId());
					pushMessage(gpdResponseMessage);
					break;
				//FindPredecessor
				case "FPD":
					//System.out.println("FPD Request Found");
					FingeredNode fpdNode = node.findPredecessor(Integer.parseInt(parts[1]));
					String fpdResponse = "1," + fpdNode.getIp() + ";" + Integer.toString(fpdNode.getId());
					pushMessage(fpdResponse);
					break;
				//Update Finger Table:
                case "UFT":
					//System.out.println("UFT Request Found");
					String[] fingerDetails = part2.split(";");
					int i = Integer.parseInt(fingerDetails[0]);
					String fingerIp = fingerDetails[1];
					int fingerId = Integer.parseInt(fingerDetails[2]);
					FingeredNode newFingerNode = new FingeredNode(fingerIp, fingerId);
					node.updateFingerTable(newFingerNode, i);
                	break;
				//Find Successor
				case "FSU":
					//System.out.println("FSU Request Found");
					int id = Integer.parseInt(part2);
					FingeredNode successor = node.findSuccessor(id);
					String fsuResponse = "1," + successor.getIp() + ";" + Integer.toString(successor.getId());
					pushMessage(fsuResponse);
					break;
				case "GSU":
					//System.out.println("GSU Request Found");
					FingeredNode nodeSuccessor = node.getSuccessor();
					String gsuResponse = "1, nodeSuccessor.getIp()" + ";" + Integer.toString(nodeSuccessor.getId());
					pushMessage(gsuResponse);
					break;
				//Closest Preceding Finger
				case "CPS":
					//System.out.println("CPS Request found");
					int cpsId = Integer.parseInt(part2);
					FingeredNode closestPredecessor = node.closestPrecedingFinger(cpsId);
					returnMessage = "1," + closestPredecessor.getIp() + ";" + Integer.toString(closestPredecessor.getId());
					pushMessage(returnMessage);
					break;
				//Get URL data
				case "GUD":
					//System.out.println("GUD Request found");
					returnMessage = "1," + node.fetchData(part2);
					pushMessage(returnMessage);
					node.getData(part2);
					break;
				//Add URL Data
				case "AUD":
					//System.out.println("AUD Request found");
					String[] messages = parts[1].split(";");
					Boolean success = node.passData(messages[0],messages[1]);
					String audResponse;
					if (success == true) {
						audResponse = "1";
						pushMessage(audResponse);
					} else {
						audResponse = "0";
						pushMessage(audResponse);
					}
					break;
				//Delete URL Data
				case "DUD":
					//System.out.println("DUD Request found");
					node.removeData(parts[1]);
					pushMessage("1");
					break;
				//Ownership Request
				case "OWN":
					//System.out.println("OWN request found");
					int requestId = Integer.parseInt(parts[1]);
					String ownResponse = "1," + node.transferOwnership(requestId);
					//System.out.println(ownResponse);
					pushMessage(ownResponse);
					break;
				//Force FingerTable update REQUEST AND RESPONSE
				case "FUT":
					//System.out.println("FUT request found");
					String[] futRequest = parts[1].split(";");
					//System.out.println("0: " + futRequest[0]);
					//System.out.println("1: " + futRequest[1]);
					//System.out.println("2: " + futRequest[2]);
					//System.out.println("3: " + futRequest[3]);
					FingeredNode updateNode = new FingeredNode(futRequest[1], Integer.parseInt(futRequest[2]));
					node.forceUpdateFingerTable(updateNode, Integer.parseInt(futRequest[0]), Integer.parseInt(futRequest[3]));
					String futResponse = "1";
					pushMessage(futResponse);
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

		while(true){
			String str_in,str_out;
			this.acceptConnectionRequest();
			str_in = this.pullMessage();
			//str_out = "New Message Received: " + str_in;
			recieveString(str_in);
			disconnect();
			////System.out.println(str_out);
			//if (str_in==null) break;
		}
		
	}

}
