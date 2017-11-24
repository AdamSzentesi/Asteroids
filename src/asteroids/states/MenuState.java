package asteroids.states;

import asteroids.State;
import asteroids.World;
import asteroids.components.*;
import asteroids.components.Collider.*;
import asteroids.components.Geometry2D.*;
import asteroids.subsystems.*;
import static org.lwjgl.opengl.GL11.glClearColor;

/**
 *
 * @author Adam Szentesi
 */
public class MenuState extends State
{
	private World world;
	private float timer = 5;
	
	public MenuState(int width, int height)
	{
		super(width, height);
		this.world = new World(100);
	}
	
	@Override
	public void initialize()
	{
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		//COMPONENTS + KEYS
		long inputComponentKey = world.registerComponent(InputComponent.class);
		long asteroidComponentKey = world.registerComponent(AsteroidComponent.class);
		long rotateComponentKey = world.registerComponent(RotateComponent.class);
		long transform2DComponentKey = world.registerComponent(Transform2DComponent.class);
		long rigidbody2DComponentKey = world.registerComponent(Rigidbody2DComponent.class);
		long collider2DComponentKey = world.registerComponent(Collider2DComponent.class);
		long render2DLineComponentKey = world.registerComponent(Render2DLineComponent.class);
		long render2DTextComponent = world.registerComponent(Render2DTextComponent.class);
		long cameraComponentKey = world.registerComponent(CameraComponent.class);
		System.out.println("...");
		
		//SUBSYSTEMS + LOCKS
		world.addSubsystem(InputSubsystem.class, inputComponentKey);
		world.addSubsystem(Physics2DMoveSubsystem.class, transform2DComponentKey | rigidbody2DComponentKey);
		world.addSubsystem(FieldSubsystem.class, transform2DComponentKey);
		world.addSubsystem(AsteroidSubsystem.class, asteroidComponentKey | transform2DComponentKey | collider2DComponentKey | rotateComponentKey);
		world.addSubsystem(Rotate2DSubsystem.class, transform2DComponentKey | rotateComponentKey);
		world.addSubsystem(Update2DCameraSubsystem.class, cameraComponentKey | transform2DComponentKey);
		world.addSubsystem(UpdateHUDSubsystem.class, render2DTextComponent);
		world.addRenderSubsystem(Render2DSubsystem.class, render2DLineComponentKey | transform2DComponentKey);
			world.getRenderSubsystem(Render2DSubsystem.class).addLock("colliders", collider2DComponentKey | transform2DComponentKey);
			world.getRenderSubsystem(Render2DSubsystem.class).addLock("text", render2DTextComponent);
			world.getRenderSubsystem(Render2DSubsystem.class).active = true;
		System.out.println("...");
		
		//ENTITIES + COMPONENTS + VALUES
		int camera = world.createEntity();
		world.addComponent(camera, Transform2DComponent.class);
			world.getComponent(camera, Transform2DComponent.class).transform.position.set(0.0f, 0.0f);
		world.addComponent(camera, CameraComponent.class);
			world.getComponent(camera, CameraComponent.class).projection.initPerspective(70f, (float)getWidth()/getHeight(), 0.001f, 10f);
		world.addComponent(camera, RotateComponent.class);
			world.getComponent(camera, RotateComponent.class).rate = 0.0f;

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
		
		int logoLabel = this.world.createEntity();
		world.addComponent(logoLabel, Render2DTextComponent.class);
			world.getComponent(logoLabel, Render2DTextComponent.class).setString("CLONOIDS");
			world.getComponent(logoLabel, Render2DTextComponent.class).color.set(1.0f, 0.8f, 0.2f);
			world.getComponent(logoLabel, Render2DTextComponent.class).setPosition(-175, 0);
			world.getComponent(logoLabel, Render2DTextComponent.class).setSize(50, 50);
		
		int companyLabel = this.world.createEntity();
		world.addComponent(companyLabel, Render2DTextComponent.class);
			world.getComponent(companyLabel, Render2DTextComponent.class).setString("[c] READY GAMES 2017");
			world.getComponent(companyLabel, Render2DTextComponent.class).color.set(1.0f, 1.0f, 1.0f);
			world.getComponent(companyLabel, Render2DTextComponent.class).setPosition(-150, -50);
			world.getComponent(companyLabel, Render2DTextComponent.class).setSize(16, 16);
		
			
			
			
		int menuItem01 = this.world.createEntity();
		world.addComponent(menuItem01, Render2DTextComponent.class);
			world.getComponent(menuItem01, Render2DTextComponent.class).setString("> NEW GAME");
			world.getComponent(menuItem01, Render2DTextComponent.class).color.set(1.0f, 0.0f, 0.0f);
			world.getComponent(menuItem01, Render2DTextComponent.class).setPosition(-150, -100);
			world.getComponent(menuItem01, Render2DTextComponent.class).setSize(16, 16);
			
		int menuItem02 = this.world.createEntity();
		world.addComponent(menuItem02, Render2DTextComponent.class);
			world.getComponent(menuItem02, Render2DTextComponent.class).setString("  OPTIONS");
			world.getComponent(menuItem02, Render2DTextComponent.class).color.set(1.0f, 1.0f, 1.0f);
			world.getComponent(menuItem02, Render2DTextComponent.class).setPosition(-150, -116);
			world.getComponent(menuItem02, Render2DTextComponent.class).setSize(16, 16);
			
		int menuItem03 = this.world.createEntity();
		world.addComponent(menuItem03, Render2DTextComponent.class);
			world.getComponent(menuItem03, Render2DTextComponent.class).setString("  EXIT");
			world.getComponent(menuItem03, Render2DTextComponent.class).color.set(1.0f, 1.0f, 1.0f);
			world.getComponent(menuItem03, Render2DTextComponent.class).setPosition(-150, -132);
			world.getComponent(menuItem03, Render2DTextComponent.class).setSize(16, 16);
		System.out.println("...");
		
		//HIERARCHY
		
		//SUBSYSTEM ADDITIONS
		world.getRenderSubsystem(Render2DSubsystem.class).setActiveCamera(world, camera);
	}
	
	@Override
	public void update(float delta)
	{
		this.world.update(delta);
		if(this.timer < 0)
		{
			pop();
		}
		this.timer -= delta;
	}

	@Override
	public void render()
	{
		this.world.render(0);
	}

	@Override
	public void cleanUp()
	{
		this.world.cleanUp();
	}
}
