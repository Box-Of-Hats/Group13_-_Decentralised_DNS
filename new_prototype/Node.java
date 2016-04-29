import java.net.*;
import java.util.*;

public class Node{
    
    public static final int MAXSIZE = 8; //The maximum number of Nodes in the system
    private int guid; //Globally Unique Identifier
    private String ip;
    //The length of fingerTable and idealFingerTable is the number of bits used to store the GUID variable.
    //We are currently using a tableLenth of 3, meaning that the highest GUID currently is 2**3, which is 8.
    private Finger[] fingerTable = new Finger[3]; 
    private int[] idealFingertable = new int[3];
    private FingeredNode predecessor;
    private Client client;
    private HashMap<String,String> data;

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
        predecessor = new FingeredNode(ip, guid);
        for (int i = 0; i < fingerTable.length; i++) {
            int start = (int)Math.pow(2, i);
            Finger f = new Finger(ip, guid, start);
            fingerTable[i] = f;
        }
        System.out.println("Started new network");
    }

    public void join(String bootstrapNodeIp){
        //When passed a node IP, the target node joins the network of that bootstrap node.
        int id = guid;
        Boolean idInNetwork = true;
        while(idInNetwork) {
            System.out.println("CHECKING ID");
            client.connectToServer(bootstrapNodeIp);
            System.out.println("FINDINGSUCESSOR OF " + (Math.abs(guid) % MAXSIZE) + " Across the network");
            String request = "FSU," + Integer.toString(Math.abs((guid) % MAXSIZE));
            client.pushMessage(request);
            String response = client.pullMessage();
            client.disconnect();
            String[] parts = response.split(",");
            String[] responseNode = parts[1].split(";");
            id = Integer.parseInt(responseNode[1]);
            System.out.println("SUCCESOR FOUND AS NODE " + id);
            if (id != guid)
                idInNetwork = false;
            else
                guid = (guid + 1) % MAXSIZE;
        }
        //Set up Finger Table and announces exsistance to network
        initFingerTable(bootstrapNodeIp);
        updateOthers();
        takeOwnership();
        System.out.println("Joined Specified Network");
    }
    
    public void quit(){
        /*
            QUITTING:
            Assign all url-ip pairs to successor,
            Update Finger Tables of other nodes,
            point predeseccors successor to nodes successor,
            point successors predecessor to nodes predecessor
        */
        //All URLs stored on the current node must be passed to its direct successor, and removed from node's dataStore
        FingeredNode successor = fingerTable[0].getNode();
        client.connectToServer(successor.getIp());

        data.forEach((url,ip) -> subQuit(url,ip));
        data.clear();
        client.disconnect();

        //Nodes pointing to the current node must now point to its successor
        for (int i = 0; i < fingerTable.length; i++){
            int updateId = guid - (int)Math.pow(2, i);
            if (updateId < 0) {
                updateId = updateId + MAXSIZE;
            }
            System.out.println("Node to notify of departure: " + updateId);
            FingeredNode p = closestPrecedingFinger(updateId);
            if (p.getId() == guid){
                updateId = p.getId() - 1;
                if (updateId < 0)
                    updateId = updateId + MAXSIZE;
                p = closestPrecedingFinger(updateId);
            }
            System.out.println("Closest Preceding Node of " + updateId + " is Node " + p.getId());
            System.out.println("Notifying Node " + p.getId());
            client.connectToServer(p.getIp());
            String request = "FUT," + i + ";" + p.getIp() + ";" + Integer.toString(p.getId()) + ";" + Integer.toString(guid);
            client.pushMessage(request);
            String response = client.pullMessage();
            client.disconnect();
        }

        //Set the successor of predecessor and predecessor of successor
        client.connectToServer(predecessor.getIp());
        String request = "FUT,0;" + fingerTable[0].getNode().getIp() + ";" + Integer.toString(fingerTable[0].getNode().getId()) + ";" + Integer.toString(guid) + ";" + Integer.toString(guid);
        client.pushMessage(request);
        String response = client.pullMessage();
        client.disconnect();

        client.connectToServer(fingerTable[0].getNode().getIp());
        request = "SPD," + predecessor.getIp() + ";" + Integer.toString(predecessor.getId());
        client.pushMessage(request);
        response = client.pullMessage();
        client.disconnect();
    }

    private void subQuit(String url, String ip){
        client.pushMessage("AUD," + url + ";" + ip);
        String response = client.pullMessage();
    }
    

    public void initFingerTable(String bootstrapNodeIp){
        //Initialise the finger table of the current node.
        client.connectToServer(bootstrapNodeIp);
        //Call Find successor on bootstrap node with start of first finger. FSU = Find Successor
        int fingerId = idealFingertable[0];
        if (fingerId < 0)
            fingerId = fingerId + MAXSIZE;
        String message = "FSU," + Integer.toString(fingerId);
        client.pushMessage(message);
        String response = client.pullMessage();
        System.out.println(response);
        client.disconnect();
        String[] parts = response.split(",");
        String[] responseNode = parts[1].split(";");
        fingerTable[0] = new Finger(responseNode[0], Integer.parseInt(responseNode[1]), idealFingertable[0]);

        /*
        Pseudocode used:
            m is the length of the finger table
            n' is the node represented by bootstrapNodeIp
            for i = 0 to m - 1;
                if (finger[i + 1].start is between [n, finger[i].node]):
                    finger[i + 1].node = finger[i].node
                else:
                    finger[i + 1].node = n'.findSucessor(finger[i + 1].start)'
        */

        for (int i = 1; i < fingerTable.length; i++) {
            client.connectToServer(bootstrapNodeIp);
            fingerId = idealFingertable[0];
            if (fingerId < 0)
                fingerId = fingerId + MAXSIZE;
            message = "FSU," + Integer.toString(fingerId);
            client.pushMessage(message);
            response = client.pullMessage();
            client.disconnect();
            parts = response.split(",");
            responseNode = parts[1].split(";");
            fingerTable[i] = new Finger(responseNode[0], Integer.parseInt(responseNode[1]), idealFingertable[i]);
        }

        client.connectToServer(fingerTable[0].getNode().getIp());
        message = "GPD,0";
        client.pushMessage(message);
        response = client.pullMessage();
        client.disconnect();
        parts = response.split(",");
        responseNode = parts[1].split(";");
        predecessor = new FingeredNode(responseNode[0], Integer.parseInt(responseNode[1]));

        client.connectToServer(fingerTable[0].getNode().getIp());
        message = "SPD," + ip + ";" + Integer.toString(guid); 
        client.pushMessage(message);
        response = client.pullMessage();
        client.disconnect();
    }

    public void updateOthers(){
        /*
        Pseudocode used:
            for i = 1 to m:
                p = findPredecessor(n - 2^(i - 1))
                p.updateFingerTable(n, i)//This requires the server to call its node updateFingerTable method with the given arguments
        */
        
        for (int i = 0; i < fingerTable.length; i++){
            int updateId = guid - (int)Math.pow(2, i);
            if (updateId < 0) {
                updateId = updateId + MAXSIZE;
            }
            System.out.println("Node to update: " + updateId);
            FingeredNode p = closestPrecedingFinger(updateId);
            System.out.println("Attempting to update finger " + i + " of node " + p.getId());
            client.connectToServer(p.getIp());
            String message = "UFT," + i + ";" + ip + ";" + Integer.toString(guid);
            client.pushMessage(message);
            String response = client.pullMessage();
            client.disconnect();
        }
    }

    public void updateFingerTable(FingeredNode s, int i){
        //Updates the fingertable of the current node
        /*
        Psuedocode used:
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
       if (fingerId <= guid)
            fingerId = fingerId + MAXSIZE;

        if ((guid < nodeId) && (nodeId < fingerId)){
            System.out.println("Updating finger " + i + " with Node of ID " + s.getId());
            fingerTable[i].setNode(s);
            String message = "UFT," + i + ";" + s.getIp() + ";" + Integer.toString(s.getId());
            client.connectToServer(predecessor.getIp());
            client.pushMessage(message);
            String response = client.pullMessage();
            client.disconnect();
        }
    }

    public void forceUpdateFingerTable(FingeredNode s, int fId, int nId){
        System.out.println("Finger " + fId + " currently contains node " + fingerTable[fId].getNode().getId());
        System.out.println("Node to replace is Node " + nId);
        System.out.println("Updating finger " + fId + " With node " + s.getId());
        if(fingerTable[fId].getNode().getId() == nId) {
            fingerTable[fId].setNode(s);
            for (int i = 0; i < fingerTable.length; i++){
                if (fingerTable[i].getNode().getId() == nId){
                    System.out.println("FINGER " + i + " IS BEING UPDATED");
                    fingerTable[i].setNode(s);
                }
            }
            String predecessorIp = predecessor.getIp();
            client.connectToServer(predecessorIp);
            String request = "FUT," + fId + ";" + s.getIp() + ";" + Integer.toString(s.getId()) + ";" + Integer.toString(nId);
            client.pushMessage(request);
            String response = client.pullMessage();
            client.disconnect();
        }
    }

    public FingeredNode findSuccessor(int id){
        //Find the direct successor of the Node with the provided ID. Can be called by any node in the system upon any other target node.
        /*
        Pseudocode used:
            n' = findPredecessor(id)
            return n'.sucessor()//The contacted server should then retrieve the finger[0].node field of its node
        */
        System.out.println("FINDSUCESSOR");
        System.out.println("Finding Sucessor of Node " + id);
        FingeredNode node = findPredecessor(id);
        System.out.println("Predecessor found as Node " + node.getId());
        if (node.getId() == guid) {
            return fingerTable[0].getNode();
        } else {
            client.connectToServer(node.getIp());
            String message = "FSU," + Integer.toString(id);
            client.pushMessage(message);
            String response = client.pullMessage();
            client.disconnect();
            String parts[] = response.split(",");
            String[] responseNode = parts[1].split(";");
            FingeredNode successor = new FingeredNode(responseNode[0], Integer.parseInt(responseNode[1]));
            return successor;
        }
    }

    public FingeredNode findPredecessor(int id){
        //Find the direct predecessor of the Node with the provided ID. Can be called by any node in the system upon any other target node.
        /*
        Pseudocode used:
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
            if (nodeId == id) {
                node = new FingeredNode(ip ,guid);
            } else {
                client.connectToServer(node.getIp());
                String message = "CPS," + Integer.toString(id % MAXSIZE);
                client.pushMessage(message);
                String response = client.pullMessage();
                client.disconnect();
                String[] parts = response.split(",");
                String[] responseNode = parts[1].split(";");
                FingeredNode prevNode = node;
                node = new FingeredNode(responseNode[0], Integer.parseInt(responseNode[1]));
                if (node.getId() == prevNode.getId())
                    break;
            }
        }

        return node;
    }

    public FingeredNode closestPrecedingFinger(int id){
        /*
        Pseudocode used:
            for i = m - 1 down to 1:
                if (finger[i].node is between [n ,id]):
                    return finger[i].node;
            return n;
        */
        //Deals with Ring Architecture
        if (id < guid)
            id = id + MAXSIZE;
        
       //iterates backwards through the finger table checking each finger against the requested ID
        for (int i = fingerTable.length - 1; i >= 0; i--) {
            int fingerId = fingerTable[i].getNode().getId();
            System.out.println("Current FingerId: " + fingerId);
            if (fingerId < guid)
                fingerId = fingerId + MAXSIZE;
            if ((fingerId > guid) && (fingerId <= id)) {
                return fingerTable[i].getNode();
            }
        }
        
        FingeredNode curNode = new FingeredNode(ip, guid);
        return curNode;
    }

    public void takeOwnership(){
        FingeredNode successor = fingerTable[0].getNode();
        client.connectToServer(successor.getIp());
        String request = "OWN," + Integer.toString(guid);
        client.pushMessage(request);
        String response = client.pullMessage();
        client.disconnect();
        if (!response.equals("1,")){
            String[] parts = response.split(",");
            String[] urlIpPairs = parts[1].split(";");
            //i needs to be 1, I know it looks dumb
            for (int i = 1; i < urlIpPairs.length; i++){
                String[] urlIps = urlIpPairs[i].split(" ");
                System.out.println("Adding data for URL: " + urlIps[0]);
                addData(urlIps[0], urlIps[1]);
                String temp = getData(urlIpPairs[0]);
                System.out.println(temp);
            }
        }
    }

    public String transferOwnership(int rId){
        Iterator<Map.Entry<String, String>> it = data.entrySet().iterator();
        String response = "";
        String delUrls = "";
        int nodeId = guid;

        if (rId > nodeId)
            nodeId = nodeId + MAXSIZE;

        while (it.hasNext()) {
            Map.Entry<String,String> pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            int hashedId = Math.abs(computeUrl(pair.getKey()) % MAXSIZE);

            if (hashedId < guid)
                hashedId = hashedId + MAXSIZE;
            System.out.println("RequestId: " + rId + ", hashedId: " + hashedId + ", nodeId: " + nodeId);

            if ((rId <= hashedId) && (hashedId < nodeId)){
                System.out.println("KEY TO TRANSFER FOUND");
                String url = pair.getKey();
                String urlIp = pair.getValue();
                response = response + ";" + url + " " + urlIp;
                delUrls = delUrls + ";" + url;
            }
        }

        String[] urlsToDel = delUrls.split(";");
        //i needs to be 1, I know it looks dumb
        for (int i = 1; i < urlsToDel.length; i++){
            System.out.println("Deleting local record of " + urlsToDel[1]);
            removeData(urlsToDel[i]);
        }
        System.out.println(response);
        return response;
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
    
    public Boolean passData(String url, String ip){
        //compute the hashing result of the url
        //connect to that node for storing urp-ip pair
        int id;
        FingeredNode node;
        id = Math.abs(this.computeUrl(url) % MAXSIZE) - 1;
        if (id < 0)
            id = id + MAXSIZE;
        System.out.println("Closest Preceding Finger To " + id);
        node = this.closestPrecedingFinger(id);
        System.out.println("Node to add URL to is Node " + node.getId());
        if (node.getId() == guid){
            addData(url,ip);
            System.out.println("Url added to local node");
            return true;
        } else {
            System.out.println("Assigning " + url + " to closest preceding node " + node.getId());
            client.connectToServer(node.getIp());
            String message = "AUD," + url + ";" + ip;
            client.pushMessage(message);
            String response = client.pullMessage();
            System.out.println(response);
            if (!response.equals("1")){
                System.out.println("Url already exists in system");
                return false;
            }
            client.disconnect();
            return true;
        }
        
    }
    
    public void deleteData(String url){
        int id;
        FingeredNode node;
        id = Math.abs(this.computeUrl(url) % MAXSIZE) - 1;
        if (id < 0)
            id = id + MAXSIZE;
        node = this.closestPrecedingFinger(id);
        client.connectToServer(node.getIp());
        String message = "DUD," + url;
        client.pushMessage(message);
        String response = client.pullMessage();
        client.disconnect();
    }
    
    public String fetchData(String url){
        //compute the hashing result of the url
        //connect to that node for the ip address of the url
        int id;
        FingeredNode node;
        id = Math.abs(this.computeUrl(url) % MAXSIZE) - 1;
        //node = this.findSuccessor(id);
        if (id < 0)
            id = id + MAXSIZE;
        System.out.println("ID TO CHECK FOR " + id);
        node = this.closestPrecedingFinger(id);
        if (node.getId() == guid){
            return getData(url);
        } else {
            System.out.println("CHECKING NODE " + node.getId() + " FOR URL " + url);
            client.connectToServer(node.getIp());
            String message = "GUD," + url;     
            client.pushMessage(message);
            String response = client.pullMessage();
            client.disconnect();
            String[] parts = response.split(",");
            return parts[1];
        }
    }
    
    public Boolean addData(String url, String ip){
        // Add data to URL-> IP hashmap
        if (data.get(url) == null){
            data.put(url,ip); 
            return true;    
        }
        else{
            return false; 
        }
    }
    
    public void removeData(String url){
        data.remove(url);
    }
    
    public String getData(String url){
        // Return the ip referred by the given url 
        return data.get(url);
    }

    public void printFingerTable(){
        for (int i = 0; i < fingerTable.length; i++){
            System.out.println("FINGER["+i+"]: Node " + fingerTable[i].getNode().getId());
        }
    }

}