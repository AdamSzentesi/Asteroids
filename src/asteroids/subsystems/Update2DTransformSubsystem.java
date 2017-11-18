package asteroids.subsystems;

import asteroids.World;

public class Update2DTransformSubsystem extends Subsystem
{
	@Override
	public void process(World world, float delta)
	{
		for(int entityId : this.getPrimaryList())
		{
			
		}
	}
//	
//	
//	//OPTIMIZE: NO DOUBLE UPDATES, UPDATES ONLY IF CHANGED, SINGLE CYCLE UPDATE
//	@Override
//	public void actOnEntity(EntityManager entityManager, float delta, int entityId)
//	{
//		//System.out.println(entityId + ": ");
//		Transform3DComponent transform3DComponent = entityManager.getComponent(entityId, Transform3DComponent.class);
//
//		updateWorldMatrix(entityManager, transform3DComponent);
//		updateWorldTransform(entityManager, transform3DComponent);
//		
//		//System.out.println(transform3DComponent.transform.position.z);
//	}
//	
//	//update all the world matrices
//	private Matrix4f updateWorldMatrix(EntityManager entityManager, Transform3DComponent transform3DComponent)
//	{
//		transform3DComponent.worldMatrix = transform3DComponent.getMatrix(transform3DComponent.transform);
//		if(transform3DComponent.child)
//		{
//			Transform3DComponent parent = entityManager.getComponent(transform3DComponent.parent, Transform3DComponent.class);
//			Matrix4f parentWorldMatrix = updateWorldMatrix(entityManager, parent);
//			transform3DComponent.worldMatrix = parentWorldMatrix.multiply(transform3DComponent.worldMatrix);
//		}
//		return transform3DComponent.worldMatrix;
//	}
//	
//	//update all the world transforms
//	private Transform3D updateWorldTransform(EntityManager entityManager, Transform3DComponent transform3DComponent)
//	{
//		//update hierarchy of world transforms
//		transform3DComponent.worldTransform = transform3DComponent.transform;
//		if(transform3DComponent.child)
//		{
//			Transform3DComponent parent = entityManager.getComponent(transform3DComponent.parent, Transform3DComponent.class);
//			Transform3D parentWorldTransform = updateWorldTransform(entityManager, parent);
//			Matrix4f parentWorldMatrix = updateWorldMatrix(entityManager, parent);
//			transform3DComponent.worldTransform = transform3DComponent.worldTransform.transform(parentWorldMatrix, parentWorldTransform);
//		}
//
//		//get view position
//		Vector3f position = transform3DComponent.transform.position;
//		if(transform3DComponent.child)
//		{
//			Matrix4f parentTransformMatrix = entityManager.getComponent(transform3DComponent.parent, Transform3DComponent.class).worldMatrix;
//			position = parentTransformMatrix.transform(position);
//		}
//		position = position.multiply(-1f);
//
//		//get view rotation
//		Quaternion rotation = transform3DComponent.transform.rotation;
//		if(transform3DComponent.child)
//		{
//			rotation = transform3DComponent.worldTransform.rotation.multiply(rotation);
//		}
//		rotation = rotation.conjugate();
//		
//		Matrix4f translationMatrix = new Matrix4f().initTranslation(position);
//		Matrix4f rotationMatrix = rotation.toRotationMatrix();
//		transform3DComponent.viewMatrix = rotationMatrix.multiply(translationMatrix);
//		
//		return transform3DComponent.worldTransform;
//	}

}