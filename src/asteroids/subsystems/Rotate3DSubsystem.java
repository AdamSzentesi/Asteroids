package asteroids.subsystems;

import asteroids.World;
import asteroids.components.Geometry3D.Transform3DComponent;
import asteroids.components.RotateComponent;
import asteroids.math.Vector3f;

public class Rotate3DSubsystem extends Subsystem
{
	
	@Override
	public void process(World world, float delta)
	{
		for(int entityId : this.getPrimaryList())
		{
			RotateComponent rotateComponent = world.getComponent(entityId, RotateComponent.class);
			Transform3DComponent transform3DComponent = world.getComponent(entityId, Transform3DComponent.class);
			
			transform3DComponent.rotate(new Vector3f(0, 0, 1), rotateComponent.rate * delta);
		}
	}
}
