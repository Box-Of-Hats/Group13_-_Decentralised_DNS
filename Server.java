import java.util.HashSet;
import java.util.HashMap;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

// Node class should have a Server and a Client and should be initalized like this:
//
// Client client = new Client();
// Server server = new Server(this, client);
//
// new Thread(server).start();
// new Thread(client).start();
//
// 

public class Server implements Runnable
{
	private final int PORT = 3333;
	// private Node node;
	// private Client client;

	/*
	public Server(Node node, Client client)
	{
		this.node = node;
		this.client = client;
	}
	*/

	public void run()
	{
		ServerSocket listener;

		try
		{
			listener = new ServerSocket(PORT);
		}
		catch (IOException e)
		{
			System.out.println(e);
			return;
		}

		try
		{
			while (true)
			{
				new Thread(new Handler(listener.accept())).start();
			}
		}
		catch (IOException e)
		{
			System.out.println(e);
		}
		finally
		{
			try
			{
				listener.close();
			}
			catch (IOException e)
			{
				System.out.println(e);
			}
		}
	}

	private static class Handler implements Runnable
	{
		private Socket socket;
		private ObjectInputStream in;
		private ObjectOutputStream out;

		public Handler(Socket socket)
		{
			this.socket = socket;

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

		public void run()
		{
			while (true)
			{
				Message message;

				try
				{
					// Reads the message from the input stream
					message = (Message)in.readObject();
				}
				catch (IOException | ClassNotFoundException e)
				{
					System.out.print(e);
					return;
				}

				switch (message.getType())
				{
				case Plain:
					System.out.println(message.getData());
					break;
				case RequestFingerTable:
					//sendFingerTable(message.getSenderIp(), node.getFingerTable());
					// above is commented out because there is no node member yet because the node class is unfinished
					break;
				case ReceiveFingerTable:
					HashMap table = (HashMap)message.getData(); // Here is where the finger table will be stored so that you can do what you need with it
					break;
				case RequestNodeIp:
					//String ip = node.getIPAddress();
					//sendNodeIp(message.getSenderIp(), ip);
					break;
				case ReceiveNodeIp:
					String ip = message.getData();
					break;
				}
			}
		}
	}
}
