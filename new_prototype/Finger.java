import java.net.*;

public class Finger {
    //Represents a single finger of a Node's hashtable. (a one-way link between 2 nodes)

    private FingeredNode node;
    private int start;

    public Finger(String ip, int id, int inStart){
        node = new FingeredNode(ip, id);
        start = inStart;
    }

    public FingeredNode getNode(){
        return node;
    }
    
    public void setNode(FingeredNode newNode){
        node = newNode;
    }
}
