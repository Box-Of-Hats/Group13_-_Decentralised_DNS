import java.io.Serializable;

public class ServerMessage implements Serializable
{
	private MessageType type;
	private String data = null;

	public ServerMessage(MessageType type)
	{
		this.type = type;
	}

	public ServerMessage(MessageType type, String data)
	{
		this.type = type;
		this.data = data;
	}

	public MessageType getType()
	{
		return this.type;
	}

	public String getData()
	{
		return this.data;
	}
}
