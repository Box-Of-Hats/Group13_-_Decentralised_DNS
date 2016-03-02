import java.net.InetAddress;

public abstract class NodeClient{
    
   public abstract boolean connectToServer(InetAddress ip);
   
   public abstract void disconnect();
   
}
