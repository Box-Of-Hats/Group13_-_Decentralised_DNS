public class Node{
    /*
    guid: the unique identifier of the node
    ip: the ip of the node
    fingerTable: Finger[] an array of finger objects
    predecessor: the directly preceding node
    */

    private int guid;
    private String ip;
    private Finger[] fingerTable;
    private Node predecessor;

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

    public Finger[] getFingerTable(){
        //Accessor for Node fingerTable
        return fingerTable;
    }

    public void join(){
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
        //This requires no networking code
        return fingerTable[0]; //!! PLACEHOLDER !!
    }
    

}
