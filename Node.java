import java.util.*;

public class Node {
	/*
	Okay so this is the basic skeleton of the node class.
	It's not 100% accuarte yet, for example not all of the return
	types are correct and I couldn't remember what the finger table
	type will be so please change and update them. :)
	*/
	private Map fingerTable;
	private Map predecessor;
	private int id;

	public Map getFingerTable() {
		//Jake and Jamie
		//Accessor for fingertable
		return fingerTable;
	}

	public int getID (){
		//Accessor method for id
		return id;
	}

	public int setID(int newId){
		id = newId;
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

		//Get direct Predecessor

		//Calculate ideal finger table

		//Get IPs of Nodes in ideal finger table, or closest to
		for (int i = 0; i < /*However long the finger table is*/; i++) {
			fingerTable[i] = findClosest(i);
		}

		//
	}

	public void leave() {
		//Jammy and Chris
	}
	
	public int findClosestNodeInFinger(int nodeId) {
		int check = 10;
        int count = 0;

        Set set = m1.entrySet();
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
		String ip = getNodeIP(node);
		//Sending message using networking class
		//This is a guess and will need editing
		sendMessage(ip, new Message(MessageType.recieveMessage,message));
	}

	public boolean recieveMessage(String message) {
		//Geoff
		System.out.println(message);
		return true;
	}
}