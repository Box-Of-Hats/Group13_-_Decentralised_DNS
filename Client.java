import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.net.Socket;

public class Client implements Runnable
{
	private final int PORT = 3333;
	Socket socket;
	private ObjectInputStream in;
	private PrintWriter out;

	public void run()
	{

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
			out = new PrintWriter(socket.getOutputStream(), true);
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
	public void sendMessage(String ip, String message)
	{
		connectToServer(ip);
		out.println(message);
		disconnect();
	}
}
