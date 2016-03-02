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
    }

    public void initFingerTable(String bootstrapNodeIp){
        //This method requires a lot of network code
    }

    public void updateOthers(){
        //This method requires network code to update other nodes finger tables
    }

    public void updateFingerTable(int s, int i){
        //This method requires network code to step back along the network 
        //!!! Im unsure of the expected types of s and i.
    }

    public Node findSuccessor(int id){
        //This requires network code to retrieve the sucessor of the target node
        return this; //!! PLACEHOLDER !!
    }

    public Node findPredecessor(int id){
        //This requires network code
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


