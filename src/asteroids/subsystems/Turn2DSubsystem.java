package asteroids.subsystems;

import asteroids.components.TurnComponent;
import asteroids.Message;
import asteroids.World;
import asteroids.components.Geometry2D.Transform2DComponent;
import static asteroids.messenger.Messages.*;
import java.util.List;

public class Turn2DSubsystem extends Subsystem
{
	
	@Override
	public void process(World world, float delta)
	{
		for(int entityId : this.getPrimaryList())
		{
			TurnComponent turnComponent = world.getComponent(entityId, TurnComponent.class);
			Transform2DComponent transform2DComponent = world.getComponent(entityId, Transform2DComponent.class);
			
			List<Message> messages = getMessages(entityId);
			int messageCount = messages.size();

			for(int i = 0; i < messageCount; i++)
			{
				Message message = messages.get(i);
				switch (message.parameter)
				{
					case ECS_TURN_LEFT:
						transform2DComponent.transform.rotation += turnComponent.turnRate * delta;
						break;
					case ECS_TURN_RIGHT:
						transform2DComponent.transform.rotation -= turnComponent.turnRate * delta;
						break;
				}
			}
		}
	}
	
}
