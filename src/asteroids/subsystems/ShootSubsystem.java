package asteroids.subsystems;

import asteroids.Message;
import asteroids.Util;
import asteroids.World;
import asteroids.components.Collider.Collider2DComponent;
import asteroids.components.Collider.Shapes.Collider2DShapePoint;
import asteroids.components.Geometry2D.Render2DLineComponent;
import asteroids.components.Geometry2D.Rigidbody2DComponent;
import asteroids.components.Geometry2D.Transform2DComponent;
import asteroids.components.Projectile2DComponent;
import asteroids.components.ShootComponent;
import asteroids.math.Vector2f;
import static asteroids.messenger.Messages.*;
import asteroids.subsystems.render3D.OpenGLUtils;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

public class ShootSubsystem extends Subsystem
{
	//TMP
	private int vbo;
	private int vboCount;
	private int ibo;
	private int iboCount;
	
	public ShootSubsystem()
	{
		super();
		
		//TODO: make some line manager!!!
		float[] vertexArray =
		{
			0.0f, 0.0f,
			0.01f, -0.01f,
			0.00f, -0.05f,
			-0.01f, -0.01f,
		};
		int[] indexArray =
		{
			0, 1, 1, 2, 2, 3, 3, 0
		};
		//turn vertex array to buffer
		FloatBuffer vertexBuffer = Util.makeFlippedBuffer(vertexArray);
		this.vbo = OpenGLUtils.makeVBO(vertexBuffer);
		this.vboCount = vertexArray.length; 
		//turn index array to buffer
		IntBuffer indexBuffer = Util.makeFlippedBuffer(indexArray);
		this.ibo = OpenGLUtils.makeIBO(indexBuffer);
		this.iboCount = indexArray.length;
	}
	
	@Override
	public void process(World world, float delta)
	{
		for(int entityId : this.getPrimaryList())
		{
			Transform2DComponent transform2DComponent = world.getComponent(entityId, Transform2DComponent.class);
			ShootComponent shootComponent = world.getComponent(entityId, ShootComponent.class);

			if(shootComponent.lastShootTime > 0)
			{
				shootComponent.lastShootTime = shootComponent.lastShootTime - delta;
			}
			
			List<Message> messages = getMessages(entityId);
			int messageCount = messages.size();

			for(int i = 0; i < messageCount; i++)
			{
				Message message = messages.get(i);
				switch (message.parameter)
				{
					case ECS_FIRE:
						if(shootComponent.lastShootTime <= 0)
						{
							Vector2f velocity = new Vector2f(0, 2.5f).rotate(transform2DComponent.transform.rotation);
							if(world.hasEntityComponent(entityId, Rigidbody2DComponent.class))
							{
								velocity = velocity.add(world.getComponent(entityId, Rigidbody2DComponent.class).velocity);
							}
							summon(world, transform2DComponent, velocity);
							shootComponent.lastShootTime = shootComponent.reloadTime;
							
							Vector2f force = new Vector2f(0, 1).rotate(transform2DComponent.transform.rotation);
							force = force.multiply(-20f);
							this.sendMessage(new Message(entityId, ECS_APPLY_FORCE, force));
						}
					break;	
				}
			}
		}
	}
	
	private void summon(World world, Transform2DComponent transform2DComponent, Vector2f velocity)
	{
		int summoned = world.createEntity();
		world.addComponent(summoned, Transform2DComponent.class);
			world.getComponent(summoned, Transform2DComponent.class).transform.position.set(transform2DComponent.transform.position);
			world.getComponent(summoned, Transform2DComponent.class).transform.rotation = transform2DComponent.transform.rotation;
		world.addComponent(summoned, Render2DLineComponent.class);
			world.getComponent(summoned, Render2DLineComponent.class).set(this.vbo, this.vboCount, this.ibo, this.iboCount);
			world.getComponent(summoned, Render2DLineComponent.class).color.set(1.0f, 0.0f, 0.0f);
		world.addComponent(summoned, Rigidbody2DComponent.class);
			world.getComponent(summoned, Rigidbody2DComponent.class).mass = 0.5f;
			world.getComponent(summoned, Rigidbody2DComponent.class).gravity = false;
			world.getComponent(summoned, Rigidbody2DComponent.class).maxVelocity = 50f;
			world.getComponent(summoned, Rigidbody2DComponent.class).velocity.set(velocity);
		world.addComponent(summoned, Projectile2DComponent.class);
			world.getComponent(summoned, Projectile2DComponent.class).lifespan = 1.0f;
		world.addComponent(summoned, Collider2DComponent.class);
			world.getComponent(summoned, Collider2DComponent.class).setShape(new Collider2DShapePoint());
			world.getComponent(summoned, Collider2DComponent.class).position.set(0.0f, 0.0f);
	}

}
