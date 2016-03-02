class Testing{
	public static void main(String[] args){
		//Node aNode = new Node();
        NodeFactory testFactory = new NodeFactory();
        Node thisNode = testFactory.makeNode();
        System.out.println(thisNode.getIp());
        System.out.println(thisNode.getClient());
	}
}