package asteroids.components;

public class Component
{
	public boolean active = true;
	public String message = "";
	
	public void receiveMessage(String message)
	{
		this.message = message;
		//System.out.println("Component recieved message: " + message);
	}
}
