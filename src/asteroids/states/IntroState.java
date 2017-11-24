package asteroids.states;

import asteroids.State;
import asteroids.World;
import asteroids.components.*;
import asteroids.components.Geometry2D.*;
import asteroids.subsystems.*;
import static org.lwjgl.opengl.GL11.glClearColor;

/**
 *
 * @author Adam Szentesi
 */
public class IntroState extends State
{
	private World world;
	private float timer = 5;

	public IntroState(int width, int height)
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
		long transform2DComponentKey = world.registerComponent(Transform2DComponent.class);
		long render2DLineComponentKey = world.registerComponent(Render2DLineComponent.class);
		long render2DTextComponent = world.registerComponent(Render2DTextComponent.class);
		long cameraComponentKey = world.registerComponent(CameraComponent.class);
		System.out.println("...");
		
		//SUBSYSTEMS + LOCKS
		world.addSubsystem(InputSubsystem.class, inputComponentKey);
		world.addSubsystem(Update2DCameraSubsystem.class, cameraComponentKey | transform2DComponentKey);
		world.addSubsystem(UpdateHUDSubsystem.class, render2DTextComponent);
		world.addRenderSubsystem(Render2DSubsystem.class, render2DLineComponentKey | transform2DComponentKey);
			world.getRenderSubsystem(Render2DSubsystem.class).addLock("text", render2DTextComponent);
			world.getRenderSubsystem(Render2DSubsystem.class).active = true;
		System.out.println("...");
		
		//ENTITIES + COMPONENTS + VALUES
		int camera = world.createEntity();
		world.addComponent(camera, Transform2DComponent.class);
			world.getComponent(camera, Transform2DComponent.class).transform.position.set(0.0f, 0.0f);
		world.addComponent(camera, CameraComponent.class);
			world.getComponent(camera, CameraComponent.class).projection.initPerspective(70f, (float)getWidth()/getHeight(), 0.001f, 10f);

		int logoLabel = this.world.createEntity();
		world.addComponent(logoLabel, Render2DTextComponent.class);
			world.getComponent(logoLabel, Render2DTextComponent.class).setString("READY GAMES");
			world.getComponent(logoLabel, Render2DTextComponent.class).color.set
			(
				0.2421875f,
				0.19140625f,
				0.6328125f
			);
			world.getComponent(logoLabel, Render2DTextComponent.class).setPosition(-250, 0);
			world.getComponent(logoLabel, Render2DTextComponent.class).setSize(50, 50);
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
