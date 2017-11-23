package asteroids.subsystems;

import asteroids.Message;
import asteroids.World;
import asteroids.components.AsteroidComponent;
import asteroids.components.HealthComponent;
import static asteroids.messenger.Messages.*;
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
					case ECS_HIT:
					{
						if(world.hasEntityComponent((int)message.value, AsteroidComponent.class))
						{
							HealthComponent healthComponent = world.getComponent(entityId, HealthComponent.class);
							healthComponent.health--;
							String healthString = ((Byte)healthComponent.health).toString();
							this.sendMessage(new Message(entityId, ECS_DISPERSE, null));
							this.sendMessage(new Message(entityId, ECS_SET_STRING, healthString));
							//System.out.println("health: " + healthComponent.health);
						}
						break;
					}
				}
			}
			
		}
	}
	
}