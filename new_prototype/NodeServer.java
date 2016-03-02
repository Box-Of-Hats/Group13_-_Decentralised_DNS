public abstract class NodeServer{
    
    private ServerSocket serverSocket;
	private Socket clientSocket;
	    
    public Socket acceptConnectionRequest();
    
    public void closeIOStream();

}
