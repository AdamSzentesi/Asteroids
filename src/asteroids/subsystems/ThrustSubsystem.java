package asteroids.subsystems;

import asteroids.EntityManager;
import asteroids.Message;
import asteroids.World;
import asteroids.components.Geometry2D.Transform2DComponent;
import asteroids.components.ThrustComponent;
import asteroids.math.Vector2f;
import java.util.List;

public class ThrustSubsystem extends Subsystem
{
	@Override
	public void process(World world, float delta)
	{
		for(int entityId : this.getList("primary"))
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
					case "THRUST":
						Vector2f force = new Vector2f(0, 1).rotate(transform2DComponent.transform.rotation);
						force = force.multiply(thrustComponent.force);
						this.sendMessage(new Message(entityId, "APPLY_FORCE", force));
						break;	
				}
				
			}
			
		}
		
	}
	
}
