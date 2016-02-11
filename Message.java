import java.io.Serializable;

public class Message<T> implements Serializable
{
	private MessageType type;
	private T data = null;
	private String senderIp;

	public Message(MessageType type, String senderip)
	{
		this.type = type;
		this.senderIp = senderIp;
	}

	public Message(MessageType type, T data, String senderip)
	{
		this.type = type;
		this.data = data;
		this.senderIp = senderIp;
	}

	public MessageType getType()
	{
		return this.type;
	}

	public T getData()
	{
		return this.data;
	}

	public String getSenderIp()
	{
		return this.senderIp;
	}
}
