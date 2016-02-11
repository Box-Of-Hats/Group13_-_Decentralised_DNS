import java.util.Hashtable;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;

public class Server implements Runnable
{
	private final int PORT = 3333;
	private static HashSet<ObjectOutputStream> writers = new HashSet<ObjectOutputStream>();

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
		private BufferedReader in;
		private ObjectOutputStream out;

		public Handler(Socket socket)
		{
			this.socket = socket;
		}

		public void run()
		{
			System.out.println("User connected!");
		}
	}
}
