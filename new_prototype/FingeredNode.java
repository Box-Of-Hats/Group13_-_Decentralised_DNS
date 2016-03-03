import java.net.*;

public class FingeredNode {
	private String ip;
	private int id;

	public FingeredNode (String inIp, int inId) {
		ip = inIp;
		id = inId;
	}

	public String getIp () {
		return ip;
	}

	public int getId () {
		return id;
	}
}
