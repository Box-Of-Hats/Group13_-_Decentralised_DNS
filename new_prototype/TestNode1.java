class TestNode1{
	public static void main(String[] args){
		//Node aNode = new Node();
        NodeFactory testFactory = new NodeFactory();
        Node thisNode = testFactory.makeNode();
        thisNode.join();
        System.out.println("This Node IP: " + thisNode.getIp());
        System.out.println("This Node ID: " + thisNode.getGuid());
	}
}