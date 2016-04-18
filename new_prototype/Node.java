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
    private Finger[] fingerTable = new Finger[8];
    private FingeredNode predecessor;
    private Client client;

    public Node(){
        //Constructor for Node class
        setIp(findIpFromMachine());
        guid = computeId();
        join();
    }

    public Node(Client nodeClient){
        //Constructor for Node class, when a client is passed
        setClient(nodeClient);
        setIp(findIpFromMachine());
        guid = computeId();
        join();
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

    public void join(){
        //When called without a bootstrapIP, the Node is assumed to be the first in the network.
        //Untested but based on Jamie's pseudocode so it should work.
        //This method requires no network code as it is the first node in the network.
        for (int i = 0; i < fingerTable.length; i++) {
            int start = (int)Math.pow(2, i);
            Finger f = new Finger(ip, guid, start);
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

    public FingeredNode closestPrecedingFinger(int key){
        /*
        This requires no networking code
        for i = m - 1 down to 1:
            if (finger[i].node is between [n ,id]):
                return finger[i].node;
        return n;

        m = len finger table
        id = guid?
        n = key?


        !!!!!! This code is correct *but* the m, id and n variables may be assigned to the wrong variables. This will need testing/checking but should be easy to fix if need be

        */
        
        for (int i = fingerTable.length; i == 1 ; i--){
            if ((fingerTable[i].getNode().getId() > key) && (fingerTable[i].getNode().getId() < guid) ){
                return fingerTable[i].getNode();
            }
        }
        //If closest preceding finger is not found, return null:
        return null;
        
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
