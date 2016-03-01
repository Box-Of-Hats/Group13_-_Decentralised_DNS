import java.util.*;

public class Finger extends HashMap{
    /*
    Represents a single finger of a Node's hashtable.
    Not 100% sure whats going on here.
    I think the class is supposed to extend Hashtable but I could be completely wrong. Please check!
    */

    private Node node;

    public Finger(){

    }

    public void setNode(Node newNode){
        node = newNode;
    }

}