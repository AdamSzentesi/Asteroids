package asteroids.subsystems;

import asteroids.Message;
import asteroids.Util;
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
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Random;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;

public class AsteroidSubsystem extends Subsystem
{
	private Random random = new Random();
	private float timer = 0;
	
	//TMP
	private int vbo;
	private int vboCount;
	private int ibo;
	private int iboCount;
	
	public AsteroidSubsystem()
	{
		super();
		
		//TMP: make some line manager!!!
		float[] vertexArray =
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
		};
		int[] indexArray =
		{
			0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12, 12, 13, 0
		};
		//turn vertex array to buffer
		FloatBuffer vertexBuffer = Util.makeFlippedBuffer(vertexArray);
		this.vbo = makeVBO(vertexBuffer);
		this.vboCount = vertexArray.length; 
		//turn index array to buffer
		IntBuffer indexBuffer = Util.makeFlippedBuffer(indexArray);
		this.ibo = makeIBO(indexBuffer);
		this.iboCount = indexArray.length;
	}
	
	@Override
	public void process(World world, float delta)
	{
		for(int entityId : this.getList("primary"))
		{
			Transform2DComponent transform2DComponent = world.getComponent(entityId, Transform2DComponent.class);
			transform2DComponent.transform.rotation += world.getComponent(entityId, RotateComponent.class).rate * delta;
			
			List<Message> messages = getMessages(entityId);
			int messageCount = messages.size();

			for(int i = 0; i < messageCount; i++)
			{
				Message message = messages.get(i);
				switch (message.parameter)
				{
					case "HIT":
					{
						//System.out.println("HIT by: " + world.getEntityKey((int)message.value));
						if(world.hasEntityComponent((int)message.value, Projectile2DComponent.class))
						{
							AsteroidComponent asteroidComponent = world.getComponent(entityId, AsteroidComponent.class);
							asteroidComponent.level -= 1;
							//wrong!!!
							if(asteroidComponent.level > 0)
							{
								//update old
								world.getComponent(entityId, Collider2DComponent.class).setShape(new Collider2DShapeCircle(asteroidComponent.level * 0.1f));
								world.getComponent(entityId, Rigidbody2DComponent.class).velocity.set(getRandomVector().multiply((float)1/asteroidComponent.level));
								world.getComponent(entityId, Transform2DComponent.class).transform.scale.set(asteroidComponent.level, asteroidComponent.level);
								
								//create new
								summon(world, transform2DComponent.transform.position, asteroidComponent.level);
							}
							else
							{
								world.destroyEntity(entityId);
							}
						}
						break;
					}
					case "DISPERSE":
					{
						disperse();
						break;
					}
				}
			}
		}
		
		if(this.timer <= 0)
		{
			summon(world, new Vector2f(-1.0f, -1.0f), (byte)3);
			this.timer = 5.0f;
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
			world.getComponent(entityId, Render2DLineComponent.class).set(this.vbo, this.vboCount, this.ibo, this.iboCount);
			world.getComponent(entityId, Render2DLineComponent.class).color.set(1.0f, 0.8f, 0.2f);
		world.addComponent(entityId, Collider2DComponent.class);
			world.getComponent(entityId, Collider2DComponent.class).setShape(new Collider2DShapeCircle(level * 0.1f));
		world.addComponent(entityId, Rigidbody2DComponent.class);
			world.getComponent(entityId, Rigidbody2DComponent.class).mass = 3000.0f;
			world.getComponent(entityId, Rigidbody2DComponent.class).maxVelocity = 5.0f;
			world.getComponent(entityId, Rigidbody2DComponent.class).velocity.set(getRandomVector().multiply((float)1/level));
		world.addComponent(entityId, AsteroidComponent.class);
			world.getComponent(entityId, AsteroidComponent.class).level = level;
		world.addComponent(entityId, RotateComponent.class);
			world.getComponent(entityId, RotateComponent.class).rate = this.random.nextFloat() * 50 - 25;
	}
	
	private void disperse()
	{
		for(int entityId : getPrimaryList())
		{
			
		}
	}
	
	private Vector2f getRandomVector()
	{
		int randomAngle = this.random.nextInt(360);
		Vector2f result = new Vector2f(0, 1).rotate(randomAngle);
		return result;
	}
	
	//return VBO from FloatBuffer
	private int makeVBO(FloatBuffer inputData)
	{
		//create VBO
		int vertexBuffer = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
			glBufferData(GL_ARRAY_BUFFER, inputData, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		return vertexBuffer;
	}
	
	//return IBO from FloatBuffer
	private int makeIBO(IntBuffer inputData)
	{
		//create IBO
		int indexBuffer = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, inputData, GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		
		return indexBuffer;
	}
}
