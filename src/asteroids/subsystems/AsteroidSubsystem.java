package asteroids.subsystems;

import asteroids.Message;
import asteroids.World;
import asteroids.components.AsteroidComponent;
import asteroids.components.Collider.Collider2DComponent;
import asteroids.components.Collider.Shapes.Collider2DShapeCircle;
import asteroids.components.Geometry2D.Render2DLineComponent;
import asteroids.components.Geometry2D.Rigidbody2DComponent;
import asteroids.components.Geometry2D.Transform2DComponent;
import asteroids.components.Projectile2DComponent;
import asteroids.components.RotateComponent;
import asteroids.math.Vector2f;
import java.util.List;
import java.util.Random;

public class AsteroidSubsystem extends Subsystem
{
	private Random random = new Random();
	private float timer = 0;
	
	@Override
	public void process(World world, float delta)
	{
		for(int entityId : this.getList("primary"))
		{
			Transform2DComponent transform2DComponent = world.getComponent(entityId, Transform2DComponent.class);
			transform2DComponent.transform.rotation += world.getComponent(entityId, RotateComponent.class).rate;
			
			List<Message> messages = getMessages(entityId);
			int messageCount = messages.size();

			for(int i = 0; i < messageCount; i++)
			{
				Message message = messages.get(i);
				switch (message.parameter)
				{
					case "HIT":
						//System.out.println("HITTED by: " + world.getEntityKey((int)message.value));
						
						if(world.hasEntityComponent((int)message.value, Projectile2DComponent.class))
						{
							AsteroidComponent asteroidComponent = world.getComponent(entityId, AsteroidComponent.class);
							asteroidComponent.level -= 1;
							//wrong!!!
							if(asteroidComponent.level > 0)
							{
								//update old
								world.getComponent(entityId, Collider2DComponent.class).collider2DShape = new Collider2DShapeCircle(asteroidComponent.level * 0.1f);
								world.getComponent(entityId, Rigidbody2DComponent.class).velocity.set(getRandomVelocity().multiply((float)1/asteroidComponent.level));
								world.getComponent(entityId, Transform2DComponent.class).transform.scale.set(asteroidComponent.level, asteroidComponent.level);
								
								//create new
								summon
								(
									world,
									transform2DComponent.transform.position,
									asteroidComponent.level
								);
							}
							else
							{
								world.destroyEntity(entityId);
							}
						}
						
						break;	
				}
				
			}
			
		}
		
		if(this.timer <= 0)
		{
			summon(world, new Vector2f(-1.0f, -1.0f), (byte)3);
			this.timer = 5f;
		}
		this.timer -= delta;
	}
	
	private void summon(World world, Vector2f position, byte level)
	{
		int entityId = world.createEntity();
		world.addComponent(entityId, Transform2DComponent.class);
			world.getComponent(entityId, Transform2DComponent.class).transform.position.set(position);
			world.getComponent(entityId, Transform2DComponent.class).transform.scale.set(level, level);
		world.addComponent(entityId, Render2DLineComponent.class);
			world.getComponent(entityId, Render2DLineComponent.class).loadLine
			(
				new float[]
				{
					0.0365102627128692f, 0.0904611828055805f,
					0.0f, 0.1f,
					-0.0830524434676359f, 0.0476393970742882f,
					-0.1f, 0.0f,
					-0.0866025403784439f, -0.0115499020234595f,
					-0.0593274809847848f, -0.0805f,
					-0.0223174981037671f, -0.0821688591886346f,
					-0.0044829319636621f, -0.0998994660696902f,
					0.0593274809847848f, -0.0805f,
					0.0636555993560517f, -0.0634293077388686f,
					0.0728037344786311f, -0.0589013415831579f,
					0.0972551864335618f, -0.0127315272348419f,
					0.08f, 0.034168728838749f,
					0.087248548270777f, 0.0488640033628222f
				},
				new int[]
				{
					0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12, 12, 13, 0
				}
			);
			world.getComponent(entityId, Render2DLineComponent.class).color.set(1.0f, 0.8f, 0.2f);
		world.addComponent(entityId, Collider2DComponent.class);
			world.getComponent(entityId, Collider2DComponent.class).collider2DShape = new Collider2DShapeCircle(level * 0.1f);
		world.addComponent(entityId, Rigidbody2DComponent.class);
			world.getComponent(entityId, Rigidbody2DComponent.class).mass = 3000.0f;
			world.getComponent(entityId, Rigidbody2DComponent.class).maxVelocity = 5.0f;
			world.getComponent(entityId, Rigidbody2DComponent.class).velocity.set(getRandomVelocity().multiply((float)1/level));
		world.addComponent(entityId, AsteroidComponent.class);
			world.getComponent(entityId, AsteroidComponent.class).level = level;
		world.addComponent(entityId, RotateComponent.class);
			world.getComponent(entityId, RotateComponent.class).rate = this.random.nextFloat() * 10 - 5;
	}
	
	private Vector2f getRandomVelocity()
	{
		int randomAngle = this.random.nextInt(360);
		Vector2f result = new Vector2f(0, 1).rotate(randomAngle);
		return result;
	}
	

}
