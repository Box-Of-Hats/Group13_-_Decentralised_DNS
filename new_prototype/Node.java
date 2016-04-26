import java.net.*;
import java.util.*;

public class Node{
    /*
    guid: the unique identifier of the node
    ip: the ip of the node
    fingerTable: Finger[] an array of finger objects
    predecessor: the directly preceding node
    */
    
    public static final int MAXSIZE = 8;
    
    private int guid; //Globally Unique Identifier
    private String ip;
    private Finger[] fingerTable = new Finger[3];
    private int[] idealFingertable = new int[3];
    private FingeredNode predecessor;
    private Client client;
    private Map<String,String> data;

    public Node(){
        //Constructor for Node class
        setIp(findIpFromMachine());
        guid = Math.abs(computeId() % MAXSIZE);
        calculateIdealFingertable();
        data = new HashMap<String,String>();
    }

    public Node(Client nodeClient){
        //Constructor for Node class, when a client is passed
        this();
        setClient(nodeClient);       
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
            idealFingertable[i] = (n + ((int)Math.pow(2, i))) % MAXSIZE;
        }
    }


    public void join(){
        //When called without a bootstrapIP, the Node is assumed to be the first in the network.
        //Untested but based on Jamie's pseudocode so it should work.
        //This method requires no network code as it is the first node in the network.
        predecessor = new FingeredNode(ip, guid);
        for (int i = 0; i < fingerTable.length; i++) {
            int start = (int)Math.pow(2, i);
            Finger f = new Finger(ip, guid, start);
            fingerTable[i] = f;
        }
        System.out.println("Started new network");
    }

    public void join(String bootstrapNodeIp){
        //This method requires network code to contact other nodes
        //MAKE SURE ID ISNT TAKEN REDO IF NEEDED
        int id = guid;
        Boolean idInNetwork = true;
        while(idInNetwork) {
            System.out.println("CHECKING ID");
            client.connectToServer(bootstrapNodeIp);
            String request = "FSU," + Integer.toString(Math.abs((guid - 1) % 8));
            client.pushMessage(request);
            String response = client.pullMessage();
            client.disconnect();
            String[] parts = response.split(",");
            String[] responseNode = parts[1].split(";");
            id = Integer.parseInt(responseNode[1]);
            if (id != guid)
                idInNetwork = false;
            else
                guid = (guid + 1) % MAXSIZE;
        }
        //Set up Finger Table and announces exsistance to network
        initFingerTable(bootstrapNodeIp);
        updateOthers();
        System.out.println("Joined Specified Network");
    }
    
    public void quit(){
        //Doesn't seem to work, currently.
        String message;
        //Pass all the data to its successor, and the successor store it temporarily
        client.connectToServer(getSuccessor().getIp());
        data.forEach((url,ip) -> client.pushMessage(combineUrlAndIp(url,ip)));
        //Ask predescessor to set its successor
        client.connectToServer(getPredecessor().getIp());
        //SNS: set new successor
        message = "SNS," + getSuccessor().getIp();
        //updateothers
        updateOthers();
        //allocate the data
        client.connectToServer(getSuccessor().getIp());
        //ATD: allocate temporary data
        message = "ATD,";
        client.pushMessage(message);

    }
    
    private String combineUrlAndIp(String url, String ip){
        //TSD: temporary store data
        return new String("TSD," + url + ";" + ip);
    }

    public void initFingerTable(String bootstrapNodeIp){
        //This method requires a lot of network code
        //m is the length of the finger table
        //n' is the node represented by bootstrapNodeIp
        client.connectToServer(bootstrapNodeIp);
        //Call Find successor on bootstrap node with start of first finger. FSU = Find Successor
        String message = "FSU," + Integer.toString(idealFingertable[0]);
        client.pushMessage(message);
        String response = client.pullMessage();
        System.out.println(response);
        client.disconnect();
        String[] parts = response.split(",");
        String[] responseNode = parts[1].split(";");
        fingerTable[0] = new Finger(responseNode[0], Integer.parseInt(responseNode[1]), idealFingertable[0]);

        /*
        for i = 0 to m - 1;
            if (finger[i + 1].start is between [n, finger[i].node]):
                finger[i + 1].node = finger[i].node
            else:
                finger[i + 1].node = n'.findSucessor(finger[i + 1].start)'
        */

        for (int i = 1; i < fingerTable.length; i++) {
            client.connectToServer(bootstrapNodeIp);
            message = "FSU," + Integer.toString(idealFingertable[i]);
            client.pushMessage(message);
            response = client.pullMessage();
            parts = response.split(",");
            responseNode = parts[1].split(";");
            fingerTable[i] = new Finger(responseNode[0], Integer.parseInt(responseNode[1]), idealFingertable[i]);
        }

        
        //predecessor = successor.predecessor // this requires the server to have some method to retrieve its node's predecessor
        client.connectToServer(fingerTable[0].getNode().getIp());
        message = "GPD,0";
        client.pushMessage(message);
        response = client.pullMessage();
        parts = response.split(",");
        responseNode = parts[1].split(";");
        predecessor = new FingeredNode(responseNode[0], Integer.parseInt(responseNode[1]));

        //successor.predecessor = n // This requires the server to have some method to set its node's predecessor
        client.connectToServer(fingerTable[0].getNode().getIp());
        message = "SPD," + ip + ";" + Integer.toString(guid); 
        client.pushMessage(message);
        response = client.pullMessage();
    }

    public void updateOthers(){
        //This method requires network code to update other nodes finger tables
        /*
        for i = 1 to m:
            p = findPredecessor(n - 2^(i - 1))
            p.updateFingerTable(n, i)//This requires the server to call its node updateFingerTable method with the given arguments
        */
        
        for (int i = 0; i < fingerTable.length; i++){
            int updateId = guid - (int)Math.pow(2, i);
            FingeredNode p = findPredecessor(updateId);
            client.connectToServer(p.getIp());
            String message = "UFT," + i + ";" + ip + ";" + Integer.toString(guid);
            client.pushMessage(message);
            String response = client.pullMessage();
        }
        
        /*
        for (i=0; i < fingerTable.length; i ++){
            FingeredNode p = findPredecessor(guid - (int)Math.pow(2,i-1));

            p.updateFingerTable(guid, i);
            client.connectToServer(p.getIp());
            client.pushMessage("UFT,"+ "s" + "i" ); //Not sure what S and I are, they should be substituted accordingly
            //Could pull message here to check if the update was successful but we can skip this.
        }
        */
    }

    public void updateFingerTable(FingeredNode s, int i){
        //This method requires network code to step back along the network 
        //!!! Im unsure of the expected types of s and i. Using int for now, as a placeholder.
        /*
        if (s is between [n, finger[i].node]):
            finger[i].node = s;
            p = n.predecessor;
            p.updateFingerTable(s, i)//This requires the server to call its node updateFingerTable method with the given arguments
        */
        int nodeId = s.getId();
        //Deals with Ring Architecture
        if (nodeId < guid)
            nodeId = nodeId + MAXSIZE;
            
       int fingerId = fingerTable[i].getNode().getId();
       if (fingerId < guid)
            fingerId = fingerId + MAXSIZE;
            
        if ((guid < s.getId()) && (s.getId() < fingerId)){
            fingerTable[i].setNode(s);
            String message = "UFT," + i + ";" + s.getIp() + ";" + Integer.toString(s.getId());
            client.connectToServer(predecessor.getIp());
            client.pushMessage(message);
            String response = client.pullMessage();
        }
        
    }

    public FingeredNode findSuccessor(int id){
        //This requires network code to retrieve the sucessor of the target node
        /*
        n' = findPredecessor(id)
        return n'.sucessor()//The contacted server should then retrieve the finger[0].node field of its node
        */
        
        FingeredNode node = findPredecessor(id);
        if (node.getId() == guid) {
            return fingerTable[0].getNode();
        } else {
            client.connectToServer(node.getIp());
            String message = "FSU," + Integer.toString(id);
            client.pushMessage(message);
            String response = client.pullMessage();
            String parts[] = response.split(",");
            String[] responseNode = parts[1].split(";");
            FingeredNode successor = new FingeredNode(responseNode[0], Integer.parseInt(responseNode[1]));
            return successor;
        }
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
        
        //Deals with ring architecture
        if (id < nodeId) {
            id = id + MAXSIZE;
            successorId = successorId + MAXSIZE;
        }
        if (successorId <= nodeId)
            successorId = successorId + MAXSIZE;
        
        //Repeats until it finds a node that should have the ID as its successor
        while (!((nodeId <= id) && (id <= successorId))) {
            System.out.println("nodeId: " + nodeId + " id: " + id + " successorId: " + successorId);
            if (currentNode) {
                node = closestPrecedingFinger(id % MAXSIZE);
                currentNode = false;
            } else if (nodeId == id) {
                node = new FingeredNode(ip ,guid);
            } else {
                client.connectToServer(node.getIp());
                String message = "CPS," + Integer.toString(id % MAXSIZE);     
                client.pushMessage(message);
                String response = client.pullMessage();
                String[] parts = response.split(",");
                String[] responseNode = parts[1].split(";");
                FingeredNode prevNode = node;
                node = new FingeredNode(responseNode[0], Integer.parseInt(responseNode[1]));
                if (node.getId() == prevNode.getId())
                    break;
            }
        }
            
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
            id = id + MAXSIZE;
        
       //iterates backwards through the finger table checking each finger against the requested ID
        for (int i = fingerTable.length - 1; i >= 0; i--) {
            int fingerId = fingerTable[i].getNode().getId();
            if (fingerId < guid)
                fingerId = fingerId + MAXSIZE;
            if ((fingerId > guid) && (fingerId < id)) {
                return fingerTable[i].getNode();
            }
        }
        
        FingeredNode curNode = new FingeredNode(ip, guid);
        return curNode;
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

    private int computeHashingValue(String s){
        /*
         *convert the type: String to byte[]
         *hash the string
         *convert the hash result to int
        */
        int value = 0;
    	byte[] resultOfHashing = new Hashing().hash(s.getBytes());
        for(int i = 0 ; i < resultOfHashing.length ; i++){
            value = value * 32;
            value += resultOfHashing[i] & 0xFF;
        }
        return value;
    }
    private int computeId(){
        return computeHashingValue(ip);
    }
    
    private int computeUrl(String url){
        return computeHashingValue(url);
    }
    
    public void passData(String url, String ip){
        //compute the hashing result of the url
        //connect to that node for storing urp-ip pair
        int id;
        FingeredNode node;
        id = Math.abs(this.computeUrl(url) % MAXSIZE);
        //node = this.findSuccessor(id); 
        node = this.findSuccessor(id-1);
        client.connectToServer(node.getIp());
        String message = "AUD," + url + ";" + ip;
         client.pushMessage(message);
    }
    
    public void deleteData(String url){
        int id;
        FingeredNode node;
        id = Math.abs(this.computeUrl(url) % MAXSIZE);
        node = this.findSuccessor(id-1);
        client.connectToServer(node.getIp());
        String message = "DUD," + url;
        client.pushMessage(message);
    }
    
    public String fetchData(String url){
        //compute the hashing result of the url
        //connect to that node for the ip address of the url
        int id;
        FingeredNode node;
        id = Math.abs(this.computeUrl(url) % MAXSIZE);
        //node = this.findSuccessor(id);
        node = this.findSuccessor(id-1);
        client.connectToServer(node.getIp());
        String message = "GUD," + url;     
        client.pushMessage(message);
        String response = client.pullMessage();
        String[] parts = response.split(",");
        return parts[1];
    }
    
    public void addData(String url, String ip){
        // Add data to URL-> IP hashmap
        data.put(url,ip);
    }
    
    public void removeData(String url){
        data.remove(url);
    }
    
    public String getData(String url){
        // Return the ip referred by the given url 
        // Null would be returned if the url does not exist
        if(data.remove(url)==null)
            return new String("null");
        return data.get(url);
    }
}
