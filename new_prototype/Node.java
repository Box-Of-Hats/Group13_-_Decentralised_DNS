import java.net.*;
import java.util.*;

public class Node{
    /*
    guid: the unique identifier of the node
    ip: the ip of the node
    fingerTable: Finger[] an array of finger objects
    predecessor: the directly preceding node
    */

    private int guid; //Globally Unique Identifier
    private String ip;
    private Finger[] fingerTable = new Finger[3];
    private int[] idealFingertable = new int[3];
    private FingeredNode predecessor;
    private Client client;

    public Node(){
        //Constructor for Node class
        setIp(findIpFromMachine());
        guid = Math.abs(computeId() % 8);
        calculateIdealFingertable();
        join();
    }

    public Node(Client nodeClient){
        //Constructor for Node class, when a client is passed
        setClient(nodeClient);
        setIp(findIpFromMachine());
        guid = Math.abs(computeId() % 8);
        calculateIdealFingertable();
        join();
        System.out.println("Joined Network");
    }

    public String getIp(){
        //Accessor for Node ip
        return ip;
    }

    public int getGuid(){
        //Accessor for Node guid
        return guid;
    }

    public FingeredNode getPredecessor(){
        //Accessor for Node predecessor
        return predecessor;
    }

    public Finger[] getFingerTable(){
        //Accessor for Node fingerTable
        return fingerTable;
    }

    public Client getClient(){
        //Accessor for Node's Client
        return client;
    }

    public FingeredNode getSuccessor(){
        //Accessor for successor 
        return fingerTable[0].getNode();
    }

    private void setGuid(int newGuid){
        //Mutator for node guid
        guid = newGuid;
    }

    public void setPredecessor(FingeredNode newPredecessor) {
        predecessor = newPredecessor;
    }

    private void setIp(String newIp){
        //Mutator for node ip
        ip = newIp;
    }

    private void setClient(Client newClient){
        //Mutator for Node client
        client = newClient;
    }

    public void calculateIdealFingertable(){
        //Calculate the ideal fingertable for the current node
        int m = fingerTable.length;
        int n = guid;
        
        for (int i = 0; i < m; i++){
            idealFingertable[i] = (n + ((int)Math.pow(2, i))) % 8;
        }
    }


    public void join(){
        //When called without a bootstrapIP, the Node is assumed to be the first in the network.
        //Untested but based on Jamie's pseudocode so it should work.
        //This method requires no network code as it is the first node in the network.
        for (int i = 0; i < fingerTable.length; i++) {
            int start = (int)Math.pow(2, i);
            Finger f = new Finger(ip, guid, start);
            fingerTable[i] = f;
        }
    }

    public void join(String bootstrapNodeIp){
        //This method requires network code to contact other nodes
        /*
        //MAKE SURE ID ISNT TAKEN REDO IF NEEDED
        initFingerTable(bootstrapNodeIp)
        updateOthers()
        */
    }

    public void initFingerTable(String bootstrapNodeIp){
        //This method requires a lot of network code
        //m is the length of the finger table
        //n' is the node represented by bootstrapNodeIp
        //Connect to bootstrap node
        //client.connectToServer(bootstrapNodeIp);
        //Call Get successor on bootstrap node with start of first finger. FSU = Find Successor
        //n.fingerTable[0] = client.pushMessage("FSU," + Integer.toString(guid + 1));

        /*
        for i = 0 to m - 1;
            if (finger[i + 1].start is between [n, finger[i].node]):
                finger[i + 1].node = finger[i].node
            else:
                finger[i + 1].node = n'.findSucessor(finger[i + 1].start)'

        for (int i = 1; i < fingerTable.length; i++){
            fingerTable[i].node = client.sendMessage("FSU", Integer.toString(idealFingerTable[i]));
        }

        predecessor = successor.predecessor // this requires the server to have some method to retrieve its node's predecessor
        successor.predecessor = n // This requires the server to have some method to set its node's predecessor
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

    public FingeredNode findSuccessor(int id){
        //This requires network code to retrieve the sucessor of the target node
        /*
        n' = findPredecessor(id)
        return n'.sucessor()//The contacted server should then retrieve the finger[0].node field of its node
        */
        /*
        FingeredNode node = findPredecessor(id);
        FingeredNode successor = ;//Call get Successor across network
        
        return successor; //!! PLACEHOLDER !!
        */
        return null;
    }

    public FingeredNode findPredecessor(int id){
        //This requires network code
        /*
        n' = n//where n is the current node
        while (id is not between [n', n'.successor]):
            n' = n'.closestPrecedingFinger(id)//This asks the appropriate server to call its node's closestPrecedingFingerMethod
        return n'
        */
        
        Boolean currentNode = true;
        FingeredNode node = new FingeredNode(ip, guid);
        int nodeId = node.getId();
        int successorId = fingerTable[0].getNode().getId();
        
        //If finger table only contains current node, just return the current node
        if (nodeId == successorId) {
            return node;
        }
        /*
        //Deals with ring architecture
        if (id < nodeId) {
            id = id + 8;
        }
        if (successorId <= nodeId)
            successorId = successorId + 8;
        
        //Repeats until it finds a node that should have the ID as its successor
        while (!((nodeId < id) && (id < successorId)) {
            if (currentNode) {
                if (id > 8)
                    id = id - 8;
                node = closestPrecedingFinger(id);
                currentNode = false;
            } else {
                if (id > 8)
                    id = id - 8;
                client.connectToServer(node.getIp());
                node = client.pushMessage("CPF", Interger.toString(node.getId()));
            }
        }
        
        
        
        /*Boolean currentNode = true;
        FingeredNode node = new FingeredNode(ip, guid);
        FingeredNode successor = fingerTable[0];
        while ((id > node.getId()) && id < successor.getId()){
            if (currentNode == true){
                node = closestPrecedingFinger(id);
                successor = //Get successor of node across network
                currentNode = false;
            } else {
                //JAKE WROTE THIS BIT, SHOULD CHECK IT:
                client.connectToServer(node.getIp());
                node = client.pushMessage("CPF,", Integer.toString(node.getId()));
                //Although a message is pushed to the server, I am unsure as to how to access the response.
                //Im thinking I might have to call pullMessage. pushMessage returns void by the looks of it but I need
                //to check with someone else.
                //JAKE FINISH


                node = //Call Closest Preceding Finger across network
                successor = //Call get Successor across network
            }
        }*/
                
        return node; //!! PLACEHOLDER !!

    }

    public FingeredNode closestPrecedingFinger(int id){
        //DONE
        //This appears to be working!
        /*
        This requires no networking code
        for i = m - 1 down to 1:
            if (finger[i].node is between [n ,id]):
                return finger[i].node;
        return n;
        */
        System.out.println("THIS");
        //Deals with Ring Architecture
        if (id < guid)
            id = id + 8;
        
       //iterates backwards through the finger table checking each finger against the requested ID
        for (int i = fingerTable.length - 1; i >= 0; i--) {
            int fingerId = fingerTable[i].getNode().getId();
            if (fingerId < guid)
                fingerId = fingerId + 8;
            if ((fingerId > guid) && (fingerId < id)) {
                return fingerTable[i].getNode();
            }
        }
        
        FingeredNode curNode = new FingeredNode(ip, guid);
        return curNode;
        
        
        /*
        for (int i = fingerTable.length; i == 1 ; i--){
            int fingerId = fingerTable[i].getNode().getId();
            if (((fingerId > guid) && (fingerId < key)) || ((fingerId > key) && (fingerId < guid))){
                return fingerTable[i].getNode();
            }
        }
        //If closest preceding finger is not found, return the current node's details:
        FingeredNode curNode = new FingeredNode(ip, guid);
        return curNode;
        */
        
    }

    public static String findIpFromMachine(){
        //Gets the local IP address of the computers Node
        InetAddress ip = null;
        try{
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(nets)) {
                    Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
                    for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                        if (!inetAddress.isAnyLocalAddress() && !inetAddress.isLinkLocalAddress() && !inetAddress.isLoopbackAddress() && !inetAddress.isMulticastAddress()) {
                            ip = inetAddress;
                            break;
                        }
                    }
                }   
        }
        catch(Exception e){
        }
        return ip.toString().substring(1);
    }

    private int computeId(){
        /*
         *convert the ip type: String to byte[]
         *hash the ip
         *convert the hash result to int
        */
        int value = 0;
        byte[] ipBytes = ip.getBytes();
    	byte[] resultOfHashing = new Hashing().hash(ipBytes);
        for(int i = 0 ; i < resultOfHashing.length ; i++){
            value = value * 32;
            value += resultOfHashing[i] & 0xFF;
        }
        return value;
    }


}
