package asteroids.subsystems;

import asteroids.World;
import asteroids.components.CameraComponent;
import asteroids.components.Geometry2D.Transform2DComponent;
import asteroids.math.Matrix4f;
import asteroids.math.Quaternion;
import asteroids.math.Vector2f;
import asteroids.math.Vector3f;

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
	
	//update all the world transforms
	private void updateWorldTransform(CameraComponent cameraComponent, Transform2DComponent transform2DComponent)
	{
		//get view position
		Vector2f position2D = transform2DComponent.transform.position;
		Vector3f position3D = new Vector3f(position2D.x, position2D.y, -3.0f);
		Vector3f position = position3D.multiply(-1f);
		Matrix4f translationMatrix = new Matrix4f().initTranslation(position);
		
		//get view rotation
		Quaternion rotation3D = new Quaternion(new Vector3f(0, 0, 1), (float)Math.toRadians(transform2DComponent.transform.rotation));
		Quaternion rotation = rotation3D.conjugate();
		Matrix4f rotationMatrix = rotation.toRotationMatrix();
		
		cameraComponent.viewMatrix = rotationMatrix.multiply(translationMatrix);
		//cameraComponent.viewMatrix = translationMatrix;
	}

}