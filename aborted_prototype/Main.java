import java.net.Inet4Address;
import java.net.UnknownHostException;

public class Main
{
	public static void main(String[] args)
	{
		Server server = new Server();
		Client client = new Client();

		new Thread(server).start();
		new Thread(client).start();


		try
		{
			System.out.println(Inet4Address.getLocalHost().getHostAddress());
		}
		catch (UnknownHostException e)
		{
			System.out.println(e);
		}
	}
}
