package asteroids.subsystems;

import asteroids.Message;
import asteroids.World;
import asteroids.components.Geometry2D.Render2DTextComponent;
import static asteroids.messenger.Messages.*;

public class UpdateHUDSubsystem extends Subsystem
{
	@Override
	public void process(World world, float delta)
	{
		for(int entityId : this.getPrimaryList())
		{

			for(Message message : getMessages(entityId))
			{
				switch (message.parameter)
				{
					case ECS_SET_STRING:
					{
						Render2DTextComponent render2DTextComponent = world.getComponent(entityId, Render2DTextComponent.class);
						render2DTextComponent.string = message.getValue(String.class);
						break;
					}
				}
			}
			
			
//			List<Message> messages = getMessages(entityId);
//			int messageCount = messages.size();			
//			for(int i = 0; i < messageCount; i++)
//			{
//				Message message = messages.get(i);
//				
//			}
			
		}
	}
	

	@Override	
	public void cleanUp()
	{

	}
}
