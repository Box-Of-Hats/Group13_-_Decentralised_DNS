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
	public String getNodeIP(nodeID) {
		//Geoff
		//Based on the assumption fingerTable is a HashMap
		if (fingerTable.containsKey(nodeID)) {
			return fingerTable.get(nodeID);
		} else {
			//find closest node in finger table then return that IP
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