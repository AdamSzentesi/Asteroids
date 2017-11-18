package asteroids.subsystems;

import asteroids.Message;
import asteroids.World;
import asteroids.components.AsteroidComponent;
import asteroids.components.HealthComponent;
import java.util.List;

public class HealthSubsystem extends Subsystem
{
	@Override
	public void process(World world, float delta)
	{
		for(int entityId : this.getPrimaryList())
		{
			
			List<Message> messages = getMessages(entityId);
			int messageCount = messages.size();
			
			for(int i = 0; i < messageCount; i++)
			{
				Message message = messages.get(i);
				switch (message.parameter)
				{
					case "HIT":
					{
						if(world.hasEntityComponent((int)message.value, AsteroidComponent.class))
						{
							HealthComponent healthComponent = world.getComponent(entityId, HealthComponent.class);
							healthComponent.health--;
							this.sendMessage(new Message(entityId, "DISPERSE", null));
							System.out.println("health: " + healthComponent.health);
						}
						break;
					}
				}
			}
			
		}
	}
	
}