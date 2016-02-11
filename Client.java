import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.HashMap;

public class Client implements Runnable
{
	private final int PORT = 3333;
	Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	public String ip;

	public void run()
	{

	}

	public Client()
	{
		try
		{
			ip = Inet4Address.getLocalHost().getHostAddress();
		}
		catch (UnknownHostException e)
		{
			System.out.println(e);
		}
	}

	private void connectToServer(String ip)
	{
		try
		{
			socket = new Socket(ip, PORT);
		}
		catch (IOException e)
		{
			System.out.println(e);
			return;
		}

		try
		{
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
		}
		catch (IOException e)
		{
			System.out.println(e);
		}
	}

	private void disconnect()
	{
		try
		{
			socket.close();
		}
		catch (IOException e)
		{
			System.out.println(e);
		}
	}

	// Connects to computer with corresponding IP, sends a message to them and then closes the connection
	public <T> void sendMessage(String ip, T message)
	{
		connectToServer(ip);

		try
		{
			// Writes the message to the output stream
			out.writeObject(new Message<T>(MessageType.Plain, message, this.ip));
		}
		catch (IOException e)
		{
			System.out.println(e);
		}

		disconnect();
	}

	public void sendMessage(String ip, MessageType type)
	{
		connectToServer(ip);

		try
		{
			// Writes the message to the output stream
			out.writeObject(new Message(type, this.ip));
		}
		catch (IOException e)
		{
			System.out.println(e);
		}

		disconnect();
	}

	public <T> void sendMessage(String ip, MessageType type, T message)
	{
		connectToServer(ip);

		try
		{
			// Writes the message to the output stream
			out.writeObject(new Message<T>(type, message, this.ip));
		}
		catch (IOException e)
		{
			System.out.println(e);
		}

		disconnect();
	}

	public void requestFingerTable(String receiverIp)
	{
		// Asks destination node for complete finger table and passes it back.
		sendMessage(receiverIp, MessageType.RequestFingerTable);
	}

	public void sendFingerTable(String receiverIp, HashMap table)
	{
		sendMessage(receiverIp, MessageType.ReceiveFingerTable, table);
	}

	public void requestNodeIp(String receiverIp, int nodeId)
	{
		sendMessage(receiverIp, MessageType.RequestNodeIp);
	}

	public void sendNodeIp(String receiverIp, String ip);
	{
		sendMessage(receiverIp, MessageType.ReceiveNodeIp, ip);
	}
}
