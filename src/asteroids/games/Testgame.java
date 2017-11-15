package asteroids.games;

import asteroids.Game;
import asteroids.World;
import asteroids.components.*;
import asteroids.components.Collider.*;
import asteroids.components.Collider.Shapes.*;
import asteroids.components.Geometry2D.*;
import asteroids.components.Geometry3D.Transform3DComponent;
import asteroids.math.Quadtree;
import asteroids.math.Vector2f;
import asteroids.subsystems.*;
import asteroids.subsystems.physics2D.Physics2DAABB;
import static org.lwjgl.opengl.GL11.glClearColor;
import sun.awt.windows.awtLocalization_fr;

public class Testgame extends Game
{
	private World world;
	
	public Testgame(int width, int height)
	{
		super(width, height);
		this.world = new World(1000);
	}

	@Override
	public void initialize()
	{
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		//COMPONENTS + KEYS
		long inputComponentKey = world.registerComponent(InputComponent.class);
		long shootComponentKey = world.registerComponent(ShootComponent.class);
		long projectile2DComponentKey = world.registerComponent(Projectile2DComponent.class);
		long thrustComponentKey = world.registerComponent(ThrustComponent.class);
		long turnComponentKey = world.registerComponent(TurnComponent.class);
		long asteroidComponentKey = world.registerComponent(AsteroidComponent.class);
		long rotateComponentKey = world.registerComponent(RotateComponent.class);
		
		long transform2DComponentKey = world.registerComponent(Transform2DComponent.class);
		long rigidbody2DComponentKey = world.registerComponent(Rigidbody2DComponent.class);
		long collider2DComponentKey = world.registerComponent(Collider2DComponent.class);
		long render2DLineComponentKey = world.registerComponent(Render2DLineComponent.class);
		
		long transform3DComponentKey = world.registerComponent(Transform3DComponent.class);
		
		long cameraComponentKey = world.registerComponent(CameraComponent.class);
		System.out.println("...");
		
		//SUBSYSTEMS + LOCKS
		world.addSubsystem(InputSubsystem.class, inputComponentKey);
		world.addSubsystem(Turn2DSubsystem.class, turnComponentKey | transform2DComponentKey);
		world.addSubsystem(ThrustSubsystem.class, thrustComponentKey | transform2DComponentKey);
		world.addSubsystem(ShootSubsystem.class, shootComponentKey | transform2DComponentKey);
			world.getSubsystem(ShootSubsystem.class).addLock("rigidbodies", shootComponentKey | transform2DComponentKey | rigidbody2DComponentKey);
//		world.addSubsystem(SimplePhysics2DSubsystem.class, transform2DComponentKey | rigidbody2DComponentKey);
//			world.getSubsystem(SimplePhysics2DSubsystem.class).addLock("colliders", collider2DComponentKey | transform2DComponentKey);
		world.addSubsystem(Physics2DMoveSubsystem.class, transform2DComponentKey | rigidbody2DComponentKey);
		world.addSubsystem(Physics2DCollisionSubsystem.class, transform2DComponentKey | collider2DComponentKey);
		world.addSubsystem(FieldSubsystem.class, transform2DComponentKey);
		world.addSubsystem(Projectile2DSubsystem.class, transform2DComponentKey | projectile2DComponentKey);
		world.addSubsystem(AsteroidSubsystem.class, asteroidComponentKey | transform2DComponentKey | collider2DComponentKey | rotateComponentKey);
		world.addSubsystem(Render2DSubsystem.class, render2DLineComponentKey | transform2DComponentKey);
			world.getSubsystem(Render2DSubsystem.class).addLock("colliders", collider2DComponentKey | transform2DComponentKey);
		System.out.println("...");
		
		//ENTITIES + COMPONENTS + VALUES
		int camera = world.createEntity();
		world.addComponent(camera, Transform3DComponent.class);
			world.getComponent(camera, Transform3DComponent.class).transform.position.set(0.0f, 0.0f, 0.0f);
		world.addComponent(camera, CameraComponent.class);
			world.getComponent(camera, CameraComponent.class).projection.initPerspective(70f, (float)getWidth()/getHeight(), 0.001f, 10f);
		
		int player = this.world.createEntity();
		world.addComponent(player, Transform2DComponent.class);
			world.getComponent(player, Transform2DComponent.class).transform.position.set(0.0f, 0.0f);
		world.addComponent(player, Render2DLineComponent.class);
			world.getComponent(player, Render2DLineComponent.class).loadLine
			(
				new float[]
				{
					0.00f, 0.10f,
					0.03f, 0.02f,
					0.08f, -0.04f,
					0.08f, -0.05f,
					0.03f, -0.08f,
					0.03f, -0.09f,
					0.01f, -0.08f,
					-0.01f, -0.08f,
					-0.03f, -0.09f,
					-0.03f, -0.08f,
					-0.08f, -0.05f,
					-0.08f, -0.04f,
					-0.03f, 0.02f,
				},
				new int[]
				{
					0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12, 12, 0	
				}
			);
			world.getComponent(player, Render2DLineComponent.class).color.set(0.0f, 1.0f, 1.0f);
		world.addComponent(player, Collider2DComponent.class);
			world.getComponent(player, Collider2DComponent.class).collider2DShape = new Collider2DShapeCircle(0.10f);
			world.getComponent(player, Collider2DComponent.class).position.set(0.0f, 0.0f);
		world.addComponent(player, Rigidbody2DComponent.class);
			world.getComponent(player, Rigidbody2DComponent.class).mass = 3.0f;
			world.getComponent(player, Rigidbody2DComponent.class).maxVelocity = 1.5f;
			world.getComponent(player, Rigidbody2DComponent.class).velocity.set(0.0f, 0.0f);
		world.addComponent(player, InputComponent.class);
			world.getComponent(player, InputComponent.class).commands.put("KEY_SPACE", "THRUST");
			world.getComponent(player, InputComponent.class).commands.put("KEY_E", "FIRE");
			world.getComponent(player, InputComponent.class).commands.put("KEY_LEFT", "TURN_LEFT");
			world.getComponent(player, InputComponent.class).commands.put("KEY_RIGHT", "TURN_RIGHT");
		world.addComponent(player, ShootComponent.class);
			world.getComponent(player, ShootComponent.class).reloadTime = 0.1f;
		world.addComponent(player, ThrustComponent.class);
			world.getComponent(player, ThrustComponent.class).force = 10.0f;
		world.addComponent(player, TurnComponent.class);
			world.getComponent(player, TurnComponent.class).turnRate = 5.0f;


		int field = world.createEntity();
		world.addComponent(field, Transform2DComponent.class);
		world.addComponent(field, Render2DLineComponent.class);
			world.getComponent(field, Render2DLineComponent.class).loadLine
			(
				new float[]
				{
					-2.0f, -2.0f, -2.0f, 2.0f, 2.0f, 2.0f, 2.0f, -2.0f
				},
				new int[]
				{
					0, 1, 1, 2, 2, 3, 3, 0
				}
			);
			world.getComponent(field, Render2DLineComponent.class).color.set(1.0f, 1.0f, 1.0f);
		System.out.println("...");
			
		//SUBSYSTEM ADDITIONS
		world.getSubsystem(Render2DSubsystem.class).setActiveCamera(world, camera);
		world.getSubsystem(Projectile2DSubsystem.class).setIgnored(player);
		//world.getComponent(camera, Transform3DComponent.class).setParent(player);
	}

	@Override
	public void update(float delta)
	{
		this.world.update(delta);
	}

	@Override
	public void cleanUp()
	{
		this.world.cleanUp();
	}
	
}
