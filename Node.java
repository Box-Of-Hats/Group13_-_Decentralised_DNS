import java.util.*;

public class Node {
	/*
	Okay so this is the basic skeleton of the node class.
	It's not 100% accuarte yet, for example not all of the return
	types are correct and I couldn't remember what the finger table
	type will be so please change and update them. :)
	*/
	private Map fingerTable;
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

	public void join() {
		//Jake and Jamie
	}

	public void leave() {
		//Jammy and Chris
	}

	public void findClosest() {
		//Geoff: wasn't sure what find closest was, so wrote this instead
		//Geoff: sorry if this is what findClosest() should do
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