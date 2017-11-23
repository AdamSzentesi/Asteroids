package asteroids.subsystems;

import asteroids.Message;
import asteroids.World;
import asteroids.components.Geometry2D.Transform2DComponent;
import asteroids.components.ThrustComponent;
import asteroids.math.Vector2f;
import static asteroids.messenger.Messages.*;
import java.util.List;

public class ThrustSubsystem extends Subsystem
{	
	@Override
	public void process(World world, float delta)
	{
		for(int entityId : this.getPrimaryList())
		{
			ThrustComponent thrustComponent = world.getComponent(entityId, ThrustComponent.class);
			Transform2DComponent transform2DComponent = world.getComponent(entityId, Transform2DComponent.class);
			
			List<Message> messages = getMessages(entityId);
			int messageCount = messages.size();

			for(int i = 0; i < messageCount; i++)
			{
				Message message = messages.get(i);
				switch (message.parameter)
				{
					case ECS_THRUST:
						Vector2f force = new Vector2f(0, 1).rotate(transform2DComponent.transform.rotation);
						force = force.multiply(thrustComponent.force);
						this.sendMessage(new Message(entityId, ECS_APPLY_FORCE, force));
						break;	
				}
			}
		}
	}
	
}
