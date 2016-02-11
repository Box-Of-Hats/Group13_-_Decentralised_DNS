import java.util.*;
//For aquiring IP address:
import java.net.InetAddress;
import java.net.UnknownHostException;
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

	public String loadIPAddress(){
		//Load the IP address from the computer
		try{
			InetAddress ip;
			ip = InetAddress.getLocalHost();
			String ipString = ip.toString();
			return ipString;
		} catch (UnknownHostException e){
			System.out.println("Could not get IP address.");
		}
	}

	public String getIPAddress(){
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
		id = newId;
	}

	public Map getPredecessor() {
		return predecessor;
	}

	public void setPredecessor(Map precedingNode) {
		predecessor = precedingNode;
	}

	public void join(String bootstrapNodeId) {
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
		TreeMap copiedFingerTable = requestFingerTable(bootstrapNodeId);

		//Hash ip to get potential GUID
		byte[] byteIP = ip.getBytes();
		int potentialId = new BigInteger(Hashing.hash(byteIP)).intValue();
		potentialId = potentialId % 5;

		nodeWithId = findNode(potentialId);
		while (nodeWithId.value() == potentialId) {
			potentialId = (potentialId + 1) % 5;
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
		predecessor = fingerTable[i].getPredecessor();

		//Tell first finger to update its finger table
		updateOthers(fingerTable.firstEntry().getValue(), 1, id, ip);

		//Tell nodes that should point to this node where they should point
		for (int i = 0; i < 3; i++) {
			targetGUID = id - Math.pow(2, (i - 1));
			Map targetNode = findPredecessor(targetGUID);
			updateOthers(targetNode.getValue(), i, id, ip);
		}
	}

	public void leave() {
		//Jammy and Chris
		
		/*	Before an existed node leave the network
		 * 	The key k stored in the leaving node should be passed to the next node, which is the successor(k) in new system
		 * 	Then notify the other nodes in the syetem to update their finger tables
		 * */

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
	
	public int findClosestNodeInFinger(int nodeId) {

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
