package asteroids.subsystems;

import asteroids.World;
import asteroids.components.Geometry2D.Transform2DComponent;

public class FieldSubsystem extends Subsystem
{
	@Override
	public void process(World world, float delta)
	{
		for(int entityId : this.getPrimaryList())
		{
			Transform2DComponent transform2DComponent = world.getComponent(entityId, Transform2DComponent.class);
			
			if(transform2DComponent.transform.position.x > 2.0f)
			{
				transform2DComponent.transform.position.x -= 4.0f;
			}
			if(transform2DComponent.transform.position.x < -2.0f)
			{
				transform2DComponent.transform.position.x += 4.0f;
			}
			if(transform2DComponent.transform.position.y > 2.0f)
			{
				transform2DComponent.transform.position.y -= 4.0f;
			}
			if(transform2DComponent.transform.position.y < -2.0f)
			{
				transform2DComponent.transform.position.y += 4.0f;
			}
			
		}
		
	}
	
}
