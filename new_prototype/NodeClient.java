// Interface used for the Client class

import java.net.InetAddress;

public abstract class NodeClient{
    
   public abstract boolean connectToServer(String ip);
   
   public abstract void disconnect();
   
}
