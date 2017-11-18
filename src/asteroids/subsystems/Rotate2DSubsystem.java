package asteroids.subsystems;

import asteroids.World;
import asteroids.components.Geometry2D.Transform2DComponent;
import asteroids.components.RotateComponent;

public class Rotate2DSubsystem extends Subsystem
{
	
	@Override
	public void process(World world, float delta)
	{
		for(int entityId : this.getPrimaryList())
		{
			RotateComponent rotateComponent = world.getComponent(entityId, RotateComponent.class);
			Transform2DComponent transform2DComponent = world.getComponent(entityId, Transform2DComponent.class);
			
			transform2DComponent.rotate(rotateComponent.rate * delta);
		}
	}
}
