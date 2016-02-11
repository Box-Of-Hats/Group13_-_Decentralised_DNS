import java.util.*;

public class node {
	/*
	Okay so this is the basic skeleton of the node class.
	It's not 100% accuarte yet, for example not all of the return
	types are correct and I couldn't remember what the finger table
	type will be so please change and update them. :)
	*/
	private Map fingerTable;
	public void join() {
		//Jake and Jamie
	}
	public void leave() {
		//Jammy and Chris
	}
	public void getFingerTable() {
		//Jake and Jamie
	}
	public void findClosest() {
		//
	}
	//wasn't sure what find closest was, so wrote this instead
	//sorry if this is what findClosest() should do
	public int findClosestNodeInFinger(int nodeId) {
		int check = 10;
        int count = 0;

        Set set = m1.entrySet();
        Iterator i = set.iterator();
        int shortestDist;
        int closestNode;
        while(i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();
            int dist = Math.abs(check - (int)me.getKey());
            if (count == 0 ) {
                shortestDist = dist;
                closestNode = me.getKey();
            } else {
                if (dist < shortestDist) {
                    shortestDist = dist;
                    closestNode = me.getKey();
                }
            }
        }

        return closestNode;
	}
	public String getNodeIP(nodeID) {
		//Geoff
		//Based on the assumption fingerTable is a HashMap
		if (fingerTable.containsKey(nodeID)) {
			return fingerTable.get(nodeID);
		} else {
			//find closest node in finger table then return that IP
			fingerTable.get(findClosestNodeInFinger(nodeID));
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