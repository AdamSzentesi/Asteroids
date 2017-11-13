package asteroids.subsystems;

import asteroids.Message;
import asteroids.World;
import asteroids.components.Projectile2DComponent;
import java.util.List;

public class Projectile2DSubsystem extends Subsystem
{
	private int ignored;
	
	@Override
	public void iterate(World world, float delta)
	{	
		for(int entityId : this.getList("primary"))
		{
			Projectile2DComponent projectile2DComponent = world.getComponent(entityId, Projectile2DComponent.class);
			
			//MESSAGES
			List<Message> messages = getMessages(entityId);
			for(Message message : messages)
			{
				switch (message.parameter)
				{
					case "HIT":
						if((int)message.value != this.ignored)
						{
							//System.out.println("HIT bullet: " + entityId);
							world.destroyEntity(entityId);						
						}
					break;
				}
			}

			if(projectile2DComponent.age < projectile2DComponent.lifespan)
			{
				projectile2DComponent.age = projectile2DComponent.age + delta;
			}
			else
			{
				world.destroyEntity(entityId);
			}
		}
		
	}
	
	public void setIgnored(int ignored)
	{
		this.ignored = ignored;
	}

}
