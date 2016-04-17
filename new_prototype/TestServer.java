class Testing{
	public static void main(String[] args){
		//Node aNode = new Node();
        NodeFactory testFactory = new NodeFactory();
        Node thisNode = testFactory.makeNode();
        System.out.println("This Node IP: " + thisNode.getIp());
	}
}