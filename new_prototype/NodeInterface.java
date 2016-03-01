public interface NodeInterface{
    /*
    Unsure as to whether this interface is needed but I have included it for now.
    */
    public String getIp();
    public int getGuid();
    public Node predecessor();
    public Finger[] getFingerTable();
    public void join();
    public void join(String bootstrapNodeIp);
    public void initFingerTable(String bootstrapNodeIp);
    public void updateOthers();
    public void updateFingerTable(int s, int i);
    public Node findSuccessor(int id);
    public Node findPredecessor(int id);
    public Finger closestPrecedingFinger(int key);
}