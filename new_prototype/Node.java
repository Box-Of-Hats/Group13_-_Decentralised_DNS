public class Node{
    /*
    guid: the unique identifier of the node
    ip: the ip of the node
    fingerTable: Finger[] an array of finger objects
    predecessor: the directly preceding node
    */

    private int guid; //Globally Unique Identifier
    private String ip;
    private Finger[] fingerTable;
    private Node predecessor;
    private NodeClient client;

    public Node(){
        //Constructor for Node class
    }

    public String getIp(){
        //Accessor for Node ip
        return ip;
    }

    public int getGuid(){
        //Accessor for Node guid
        return guid;
    }

    public Node getPredecessor(){
        //Accessor for Node predecessor
        return predecessor;
    }

    public void setGuid(int newGuid){
        //Mutator for node guid
        guid = newGuid;
    }

    public Finger[] getFingerTable(){
        //Accessor for Node fingerTable
        return fingerTable;
    }

    public void join(){
        //When called without a bootstrapIP, the Node is assumed to be the first in the network.
        //Untested but based on Jamie's pseudocode so it should work.
        //This method requires no network code as it is the first node in the network.
        for (int i=1; i == fingerTable.length; i++){
            fingerTable[i].setNode(this);
            predecessor = this;
        }
    }

    public void join(String bootstrapNodeIp){
        //This method requires network code to contact other nodes
        /*
        initFingerTable(bootstrapNodeIp)
        updateOthers()
        */
    }

    public void initFingerTable(String bootstrapNodeIp){
        //This method requires a lot of network code
        /*
        //n' is the node represented by bootstrapNodeIp
        n.finger[0] = n'.findSuccessor(finger[i].start)
        // This line contacts the bootstrap node and asks it to run its findSuccessor code using the id of the current node
        // This finds the succesor node of the current node, to do this the server would simply need to call its node's
        // findSuccessor method
        predecessor = successor.predecessor // this requires the server to have some method to retrieve its node's predecessor
        successor.predecessor = n // This requires the server to have some method to set its node's predecessor
        for i = 0 to m - 1;
            if (finger[i + 1].start is between [n, finger[i].node]):
                finger[i + 1].node = finger[i].node
            else:
                finger[i + 1].node = n'.findSucessor(finger[i + 1].start)
            */
    }

    public void updateOthers(){
        //This method requires network code to update other nodes finger tables
        /*
        for i = 1 to m:
            p = findPredecessor(n - 2^(i - 1))
            p.updateFingerTable(n, i)//This requires the server to call its node updateFingerTable method with the given arguments
        */
    }

    public void updateFingerTable(int s, int i){
        //This method requires network code to step back along the network 
        //!!! Im unsure of the expected types of s and i. Using int for now, as a placeholder.
        /*
        if (s is between [n, finger[i].node]):
        finger[i].node = s;
        p = n.predecessor;
        p.updateFingerTable(s, i)//This requires the server to call its node updateFingerTable method with the given arguments
        */
    }

    public Node findSuccessor(int id){
        //This requires network code to retrieve the sucessor of the target node
        /*
        n' = findPredecessor(id)
        return n'.sucessor()//The contacted server should then retrieve the finger[0].node field of its nod
        */
        return this; //!! PLACEHOLDER !!
    }

    public Node findPredecessor(int id){
        //This requires network code
        /*
        n' = n//where n is the current node
        while (id is not between [n', n'.successor]):
            n' = n'.closestPrecedingFinger(id)//This asks the appropriate server to call its node's closestPrecedingFingerMethod
        return n'
        */
        return this; //!! PLACEHOLDER !!

    }

    public Finger closestPrecedingFinger(int key){
        /*
        This requires no networking code
        closestPrecedingFinger(int key):
        for i = m - 1 down to 1:
            if (finger[i].node is between [n ,id]):
                return finger[i].node;
        return n;
        */
        return fingerTable[0]; //!! PLACEHOLDER !!
    }

}
