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
<<<<<<< HEAD

		try
		{
			System.out.println(Inet4Address.getLocalHost().getHostAddress());
		}
		catch (UnknownHostException e)
		{
			System.out.println(e);
		}
=======
>>>>>>> 92a6a5ad27b636eb30fa4801e24d5d08ff7237ae
	}
}
