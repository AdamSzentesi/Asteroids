package asteroids.subsystems;

import asteroids.Message;
import asteroids.World;
import asteroids.components.Geometry2D.Rigidbody2DComponent;
import asteroids.components.Geometry2D.Transform2DComponent;
import asteroids.math.Vector2f;
import static asteroids.messenger.Messages.*;
import java.util.List;

public class Physics2DMoveSubsystem extends Subsystem
{
	@Override
	public void process(World world, float delta)
	{
		Vector2f netAcceleration = new Vector2f();
		
		//SIMULATIONS
		for(int entityId : this.getPrimaryList())
		{
			Rigidbody2DComponent rigidbody2DComponent = world.getComponent(entityId, Rigidbody2DComponent.class);
			//System.out.print(entityId + "." + rigidbody2DComponent);
			rigidbody2DComponent.lastVelocity.set(rigidbody2DComponent.velocity);
			Transform2DComponent transform2DComponent = world.getComponent(entityId, Transform2DComponent.class);
			transform2DComponent.lastTransform.set(transform2DComponent.transform);
			
			//ACCELERATIONS
			netAcceleration.set(0, 0);
				
			//FORCES
			if(rigidbody2DComponent.mass != 0)
			{
				//VELOCITIES	
				rigidbody2DComponent.velocity = rigidbody2DComponent.velocity.add(netAcceleration.multiply(delta));
				
				//MESSAGES
				List<Message> messages = getMessages(entityId);
				for(Message message : messages)
				{
					switch (message.parameter)
					{
						case ECS_APPLY_FORCE:
						{
							Vector2f addAcceleration = message.getValue(Vector2f.class).divide(rigidbody2DComponent.mass);
							Vector2f addVelocity = addAcceleration.multiply(delta);
							rigidbody2DComponent.velocity = rigidbody2DComponent.velocity.add(addVelocity);
							netAcceleration = netAcceleration.add(addAcceleration);
						}
						break;
					}
				}
				
				//VELOCITY CAP
				if(rigidbody2DComponent.velocity.lengthSquared() > (rigidbody2DComponent.maxVelocity * rigidbody2DComponent.maxVelocity))
				{
					rigidbody2DComponent.velocity.set(rigidbody2DComponent.velocity.normalize().multiply(rigidbody2DComponent.maxVelocity));
				}

				//POSITION
				transform2DComponent.transform.position.set(transform2DComponent.transform.position.add(rigidbody2DComponent.velocity.multiply(delta)));
				rigidbody2DComponent.acceleration.set(netAcceleration);
			}
		}

	}
	
}
