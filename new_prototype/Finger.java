import java.util.*;

public class Finger extends HashMap{
    /*
    Represents a single finger of a Node's hashtable. (a one-way link between 2 nodes)
    Not 100% sure whats going on here.
    I think the class is supposed to extend HashMap but I could be completely wrong. Please check!
    */

    private Node node;

    public Finger(){

    }

    public void setNode(Node newNode){
        node = newNode;
    }

}