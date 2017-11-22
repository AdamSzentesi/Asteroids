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
		world.addRenderSubsystem(Render2DTextSubsystem.class, render2DTextComponent);
		System.out.println("...");
		
		//ENTITIES + COMPONENTS + VALUES
		int camera = world.createEntity();
		world.addComponent(camera, Transform2DComponent.class);
			world.getComponent(camera, Transform2DComponent.class).transform.position.set(0.0f, 0.0f);
		world.addComponent(camera, CameraComponent.class);
			world.getComponent(camera, CameraComponent.class).projection.initPerspective(70f, (float)getWidth()/getHeight(), 0.001f, 10f);
			
		int string = this.world.createEntity();
		world.addComponent(string, Transform2DComponent.class);
			world.getComponent(string, Transform2DComponent.class).transform.position.set(0.0f, 0.0f);
		world.addComponent(string, Render2DTextComponent.class);
			world.getComponent(string, Render2DTextComponent.class).setString("ahoj");
			world.getComponent(string, Render2DTextComponent.class).color.set(0.9f, 0.2f, 0.0f);
		
		int string2 = this.world.createEntity();
		world.addComponent(string2, Transform2DComponent.class);
			world.getComponent(string2, Transform2DComponent.class).transform.position.set(1.1f, 0.0f);
			world.getComponent(string2, Transform2DComponent.class).transform.scale.set(1.0f, 1.0f);
		world.addComponent(string2, Render2DTextComponent.class);
			world.getComponent(string2, Render2DTextComponent.class).setString("prdel");	
			world.getComponent(string2, Render2DTextComponent.class).setPosition(-200, 0);
			world.getComponent(string2, Render2DTextComponent.class).setSize(8, 8);
			world.getComponent(string2, Render2DTextComponent.class).color.set(0.0f, 0.5f, 0.8f);
			
		System.out.println("...");
		
		//HIERARCHY
		
		//SUBSYSTEM ADDITIONS
		

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
