import java.net.*;

public class Finger {
    /*
    Represents a single finger of a Node's hashtable. (a one-way link between 2 nodes)
    Not 100% sure whats going on here.
    I think the class is supposed to extend HashMap but I could be completely wrong. Please check!
    */

    private FingeredNode node;
    private int start;

    public Finger(String ip, int id, int inStart){
        node = new FingeredNode(ip, id);
        start = inStart;
    }

    public FingeredNode getNode(){
        return node;
    }
}
