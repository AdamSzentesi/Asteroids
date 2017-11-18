package asteroids.subsystems;

import asteroids.World;
import asteroids.components.CameraComponent;
import asteroids.components.Geometry2D.Transform2DComponent;
import asteroids.math.Matrix4f;

public class Update2DCameraSubsystem extends Subsystem
{
	@Override
	public void process(World world, float delta)
	{
		for(int entityId : this.getPrimaryList())
		{
			Transform2DComponent transform2DComponent = world.getComponent(entityId, Transform2DComponent.class);
			CameraComponent cameraComponent = world.getComponent(entityId, CameraComponent.class);
			
			updateWorldTransform(cameraComponent, transform2DComponent);
		}
	}
	
	private void updateWorldTransform(CameraComponent cameraComponent, Transform2DComponent transform2DComponent)
	{
		Matrix4f shiftMatrix = new Matrix4f().initTranslation(0, 0, 3);
		cameraComponent.viewMatrix = transform2DComponent.getViewMatrix().multiply(shiftMatrix);
	}

}