public class NodeFactory {
    /*
    A factory that creates a linked Server, Node and Client triplet.
    */
    public NodeFactory(){
        //Constructor for NodeFactory
    }

    public Node makeNode(){
        Client tempClient = new Client();
        Node tempNode = new Node(tempClient);
        new Thread(new Server(tempNode)).start();
        return tempNode;
    }

}