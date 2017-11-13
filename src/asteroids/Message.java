package asteroids;

public class Message
{
	public int entityId;
	public String parameter;
	public Object value;
	
	public Message(int entityId, String parameter, Object value)
	{
		this.entityId = entityId;
		this.parameter = parameter;
		this.value = value;
	}
	
	public <K extends Number> K getComponent(Class<K> c)
	{
		return c.cast(this.value);
	}
	
	public <F extends Object> F getValue(Class<F> c)
	{
		return c.cast(this.value);
	}
}
