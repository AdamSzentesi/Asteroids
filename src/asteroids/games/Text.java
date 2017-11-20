package asteroids.games;

import asteroids.Game;
import asteroids.World;
import asteroids.components.*;
import asteroids.components.Geometry2D.*;
import asteroids.subsystems.*;
import static org.lwjgl.opengl.GL11.glClearColor;

public class Text extends Game
{
	private World world;
	
	public Text(int width, int height)
	{
		super(width, height);
		this.world = new World(2000);
	}

	@Override
	public void initialize()
	{
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		//COMPONENTS + KEYS
		long rotateComponentKey = world.registerComponent(RotateComponent.class);
		long transform2DComponentKey = world.registerComponent(Transform2DComponent.class);
		long render2DTextComponent = world.registerComponent(Render2DTextComponent.class);
		long cameraComponentKey = world.registerComponent(CameraComponent.class);
		System.out.println("...");
		
		//SUBSYSTEMS + LOCKS
		world.addSubsystem(Rotate2DSubsystem.class, transform2DComponentKey | rotateComponentKey);
		world.addSubsystem(Update2DCameraSubsystem.class, cameraComponentKey | transform2DComponentKey);
		world.addRenderSubsystem(Render2DTextSubsystem.class, render2DTextComponent | transform2DComponentKey);
		System.out.println("...");
		
		//ENTITIES + COMPONENTS + VALUES
		int camera = world.createEntity();
		world.addComponent(camera, Transform2DComponent.class);
			world.getComponent(camera, Transform2DComponent.class).transform.position.set(0.0f, 0.0f);
		world.addComponent(camera, CameraComponent.class);
			world.getComponent(camera, CameraComponent.class).projection.initPerspective(70f, (float)getWidth()/getHeight(), 0.001f, 10f);
			
		int player = this.world.createEntity();
		world.addComponent(player, Transform2DComponent.class);
			world.getComponent(player, Transform2DComponent.class).transform.position.set(0.0f, 0.0f);
		world.addComponent(player, Render2DTextComponent.class);
			world.getComponent(player, Render2DTextComponent.class).loadLine
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
			world.getComponent(player, Render2DTextComponent.class).color.set(0.0f, 1.0f, 1.0f);
			
		System.out.println("...");
		
		//HIERARCHY
		
		//SUBSYSTEM ADDITIONS
		world.getRenderSubsystem(Render2DTextSubsystem.class).setActiveCamera(world, camera);

	}

	@Override
	public void update(float delta)
	{
		this.world.update(delta);
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
