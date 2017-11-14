package asteroids.subsystems;

import asteroids.EntityManager;
import asteroids.World;
import asteroids.components.Geometry2D.Transform2DComponent;
import asteroids.math.Vector2f;

public class FieldSubsystem extends Subsystem
{
	@Override
	public void process(World world, float delta)
	{
		for(int entityId : this.getList("primary"))
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
