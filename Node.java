import java.util.*;
//For aquiring IP address:
import java.net.*;
//

public class Node {
	/*
	Okay so this is the basic skeleton of the node class.
	It's not 100% accuarte yet, for example not all of the return
	types are correct and I couldn't remember what the finger table
	type will be so please change and update them. :)
	*/
	private TreeMap fingerTable = new TreeMap();
	private Map predecessor;
	private Map successor;
	private int id;
	private Server nodeServer;
	private Client nodeClient;
	private String ip;

	public Node () {
		//Get the ip address
		ip = loadIPAddress();
	}

	public String loadIPAddress() throws SocketException{
		//Load the IP address from the computer
		Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets)) {
            Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        	for (InetAddress inetAddress : Collections.list(inetAddresses)) {
	        	if (!inetAddress.isAnyLocalAddress() && !inetAddress.isLinkLocalAddress() && !inetAddress.isLoopbackAddress() && !inetAddress.isMulticastAddress()) {
	        		return inetAddress.toString();
	        	}
	        }
        }
	}

	private void getSendID(int functionID) {
		Long ts = new Date().getTime();
		String timestamp = Long.toString(ts);
		String nID = Integer.toString(id);
		String fID = Integer.toString(functionID);
		System.out.println(nID + fID + timestamp);
	}

	public String getIPAddress(){
		//Accessor for node ip address
		return ip;
	}

	public Map getFingerTable() {
		//Jake and Jamie
		//Accessor for fingerTable
		return fingerTable;
	}

	public int getId(){
		//Accessor method for id
		return id;
	}

	public void setId(int newId){
		//Setter method for Node ID
		id = newId;
	}

	public Map getPredecessor() {
		//Accessor for predecessor
		return predecessor;
	}

	public void setPredecessor(Map precedingNode) {
		//Setter method for precedingNode
		predecessor = precedingNode;
	}

	public void join(String bootstrapNodeIp) {
		//Jake and Jamie
		/* The new Node, n, given the IP of an arbitray existing node, p, gained my some outside mechanism
		   first copies the existing finger table of p, and then calculates the nodes to which it should point
		   in its finger table.
		   The node needs to then find its direct predecessor in the network.
		   The node then uses the finger table to look up the nodes it should point to, if the node is not
		   currently part of the network then it finds the closest node whose id is past the fingered node.
		   (This can be extended to reduce the O time of the look up, ONLY DO THIS IF WE HAVE TIME)
		   n must then inform the network that it has joined, to do this it finds the first node that could 
		   possibly have it as the ith finger in its table, and changes the table entry to point to itself.
		   (In the full version the node will then go to its direct successor and take responsibility for
		   the keys it should have.)
		   The node must also inform its direct succesor (i.e. the first finger in its table) that it has
		   joined so that it can update its predecessor.
		*/

		//Get finger table of given IP
		TreeMap copiedFingerTable = requestFingerTable(bootstrapNodeIp);

		//Hash ip to get potential GUID
		byte[] byteIP = ip.getBytes();
		int potentialId = new BigInteger(Hashing.hash(byteIP)).intValue();
		potentialId = potentialId % 5;

		nodeWithId = findNode(potentialId);
		int nodeCount = 0;
		while (nodeWithId.value() == potentialId) {
			potentialId = (potentialId + 1) % 5;
			nodeCount++;
			if (nodeCount >= 5)
				throw new Exception("Chord network is full");
		}

		id = potentialId;

		//Calculate ideal finger table
		idealFingers = new int[3];
		for (int i = 0; i < 3; i++) {
			idealFingers[i] = (id + Math.pow(2, i));
		}

		//Get IPs of Nodes in ideal finger table, or closest to
		for (int i = 0; i < 3; i++) {
			fingerTable.put(findNode(idealFingers[i]));
		}

		//Get direct by getting the current predecessor of the first node in the finger table
		predecessor = requestPredecessor(fingerTable.firstEntry());

		//Tell first finger to update its finger table
		updateOthers(fingerTable.firstEntry().getValue(), 1, id, ip);

		//Tell nodes that should point to this node where they should point
		for (int i = 0; i < 3; i++) {
			targetGUID = id - Math.pow(2, (i - 1));
			HashMap targetNode = findPredecessor(targetGUID);
			HashMap thisNode = new HashMap();
			thisNode.put(id, ip);
			updateOthers(targetNode.getValue(), i, thisNode);
		}
	}

	public TreeMap requestFingerTable(String nodeIp) {
		//Uses Jacks code to call the getFingerTable method of the connecting Node
	}

	public HashMap requestPredecessor(String targetIp) {
		//This uses Jacks code and calls the getPredecessor method of the target Node
	}

	public void updateOthers(String targetIp, int fingerNum, HashMap node) {
		/*Uses jacks code to contact a node that needs to update its finger table and tells
		  It which finger the node should replace and gives it the nodes Ip and ID for the table
		  from this node it then walks backwards and checks which nodes should contain the nodes
		  ip in there finger table as it is the new closest id to one of there ideal nodes
		*/
	}

	public void leave() {
		// Jammy and Chris
		
		/*	Before an existed node leave the network
		 * 	The key k stored in the leaving node should be passed to the next node, which is the successor(k) in new system
		 * 	Then notify the other nodes in the system to update their finger tables
		 * */
	}

		
		// Set successor to predecessor
		setPredecessor(this.successor);
		
		
		//loop through all finger tables
		for(int i = 0; i < fingerTable.size(); i++) {
			//update each finger table by remove the reference to this.id
		}

		//close terminal
		System.exit();
	}
	/*
	public Map findClosest(int nodeId) {
		/* Find Closest works to find the node with the given GUID or if such a node does not exist it finds
		   the closest node past the searched for GUID. It finds this by walking through the the chord using
		   its finger table and the finger table of the other nodes.
		   The Node will iterate through its finger table to find the target GUID, if not found it will contact
		   the node in its finger table with the highest GUID that is smaller than the target GUID, until either
		   the target GUID is found or the first finger in a node's look up table is greater than the target GUID
		   in this case this finger is taken as the closest
		
	}
	*/
	
	public HashMap findNode (int nodeId, String direction) {
		/*Checks to see if the current node is the predecessor of where the node should be
		  and if so returns the first successor of where the node should be*/
		if (id < nodeId && nodeId < fingerTable.firstEntry().getKey())
			return fingerTable.firstEntry()

		/*Checks to see if the id is in the local finger table and if so returns it, while keeping track
		of the closest one to the searched for node*/
		int closestNode = 0;
		for (Map.Entry<Interger, String> entry : fingerTable.entrySet()) {
			if (entry.getKey() == nodeId)
				return entry;

			if (entry.getKey() < nodeId) {
				
			}
		}
		
	}

	/*public int findClosestNodeInFinger(int nodeId) {

		int check = 10;
        int count = 0;

        Set set = fingerTable.entrySet();
        Iterator i = set.iterator();
        int shortestDist = 0;
        int closestNode = 0;
        while(i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();
            int dist = Math.abs(check - (int)me.getKey());
            if (count == 0 ) {
                shortestDist = dist;
                closestNode = (int)me.getKey();
            } else {
                if (dist < shortestDist) {
                    shortestDist = dist;
                    closestNode = (int)me.getKey();
                }
            }
            count++;
        }

        return closestNode;
	}
	/*
	public String findNodeIP(int nodeID) {
		//Geoff
		//Based on the assumption fingerTable is a HashMap
		if (fingerTable.containsKey(nodeID)) {
			return fingerTable.get(nodeID);
		} else {
			//find closest node in finger table then return that IP
			String nearestNodeIP = fingerTable.get(findClosestNodeInFinger(nodeID));
			//Use networking class to coonect call NodeIP function on another node
			return sendGetNodeIP(nearestNodeIP,nodeIP);
		}
	}

	public boolean sendMessage(String message, int destNode) {
		//Geoff
		String ip = findNodeIP(node);
		//Sending message using networking class
		//This is a guess and will need editing
		Network.sendMessage(ip, new Message(MessageType.recieveMessage,message));
	}
	*/
	public boolean recieveMessage(String message) {
		//Geoff
		System.out.println(message);
		return true;
	}
}
