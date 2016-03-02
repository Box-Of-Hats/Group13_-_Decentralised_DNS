import java.net.*;

public class FingeredNode {
	private InetAddress ip;
	private int id;

	public FingeredNode (InetAddress inIp, int inId) {
		ip = inIp;
		id = inId;
	}

	public InetAddress getIp () {
		return ip;
	}

	public int getId () {
		return id;
	}
}