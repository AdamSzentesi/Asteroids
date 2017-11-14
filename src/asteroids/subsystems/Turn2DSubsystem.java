package asteroids.subsystems;

import asteroids.EntityManager;
import asteroids.components.TurnComponent;
import asteroids.Message;
import asteroids.World;
import asteroids.components.Geometry2D.Transform2DComponent;
import asteroids.math.Vector2f;
import java.util.List;

public class Turn2DSubsystem extends Subsystem
{
	
	@Override
	public void process(World world, float delta)
	{
		for(int entityId : this.getList("primary"))
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
					case "TURN_LEFT":
						transform2DComponent.transform.rotation += turnComponent.turnRate;
						break;
					case "TURN_RIGHT":
						transform2DComponent.transform.rotation -= turnComponent.turnRate;
						break;
				}
				
			}
			
		}
		
	}
	
}
