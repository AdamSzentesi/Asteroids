package asteroids.subsystems;

import asteroids.subsystems.render3D.Shader;
import asteroids.World;
import asteroids.components.CameraComponent;
import asteroids.components.Geometry2D.*;
import asteroids.math.Matrix4f;
import asteroids.math.Vector2f;
import asteroids.math.Vector3f;
import asteroids.subsystems.render3D.Framebuffer;
import asteroids.subsystems.render3D.Postprocessing.*;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Render2DTextSubsystem extends Subsystem
{
	private Shader shader;
	private CameraComponent cameraComponent;
	private Matrix4f viewTransformMatrix;
	private int cameraEntityId;
	
	private Framebuffer multisampleFramebuffer;
	private Framebuffer singlesampleFramebuffer;
	private EffectManager effectManager;
	
	public Render2DTextSubsystem()
	{
		super();
		
		//shader setup
		shader = new Shader();
		shader.addShader("render2D/line2D.vs", GL_VERTEX_SHADER);
		//shader.addShader("render2D/line2D.gs", GL_GEOMETRY_SHADER);
		shader.addShader("render2D/line2D.fs", GL_FRAGMENT_SHADER);
		shader.addAttribute(0, "position");
		shader.addAttribute(4, "color");
		shader.addOutput(0, "outDiffuse");
		shader.link();
		shader.addUniform("M");
		shader.addUniform("V");
		shader.addUniform("P");
		
		//default camera setup
		this.cameraComponent = new CameraComponent();
		this.cameraComponent.projection.initPerspective(45f, (float)Display.getWidth()/Display.getHeight(), 0.001f, 100.0f);
		
		this.multisampleFramebuffer = new Framebuffer(Display.getWidth(), Display.getHeight(), 4);
		this.multisampleFramebuffer.createRenderbuffer(GL_RGBA8, GL_COLOR_ATTACHMENT0, true);
		this.multisampleFramebuffer.updateDrawbuffers();
		
		this.singlesampleFramebuffer = new Framebuffer(Display.getWidth(), Display.getHeight());
		this.singlesampleFramebuffer.createTexturebuffer(GL_COLOR_ATTACHMENT0, GL_RGBA8, GL_UNSIGNED_BYTE, true);
		this.singlesampleFramebuffer.updateDrawbuffers();
		
		this.effectManager = new EffectManager();
		this.effectManager.addEffect("hblur", new HorizontalBlurEffect(multisampleFramebuffer.getWidth()/2, multisampleFramebuffer.getHeight()/2));
		this.effectManager.addEffect("vblur", new VerticalBlurEffect(multisampleFramebuffer.getWidth()/2, multisampleFramebuffer.getHeight()/2));
		this.effectManager.addEffect("draw", new DrawEffect(multisampleFramebuffer.getWidth(), multisampleFramebuffer.getHeight()));
		this.effectManager.addEffect("combine", new CombineEffect(multisampleFramebuffer.getWidth(), multisampleFramebuffer.getHeight()));
	}
	
	//set the active camera for this renderingEngine, MOVE TO OWN SYSTEM!!!
	public void setActiveCamera(World world, int cameraEntityId)
	{
		this.cameraComponent = world.getComponent(cameraEntityId, CameraComponent.class);
		this.cameraEntityId = cameraEntityId;
	}
	
	@Override
	public void process(World world, float delta)
	{
		glLineWidth(1);
		//get current camera view matrix
		this.viewTransformMatrix = world.getComponent(this.cameraEntityId, CameraComponent.class).viewMatrix;
		
//RENDERING TO BUFFEROBJECT
		this.multisampleFramebuffer.bind();
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		glDisable(GL_DEPTH_TEST);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		//iterate through geometry
		for(int entityId : this.getList("primary"))
		{
			//System.out.println(entityId);
			Render2DTextComponent render2DTextComponent = world.getComponent(entityId, Render2DTextComponent.class);
			Transform2DComponent transform2DComponent = world.getComponent(entityId, Transform2DComponent.class);
			Matrix4f modelTransformMatrix = transform2DComponent.getWorldMatrix();
			renderVBO(modelTransformMatrix, render2DTextComponent.vbo, render2DTextComponent.ibo, render2DTextComponent.iboCount, render2DTextComponent.color, GL_LINE_STRIP);
		}
		//debug
//		renderColliders(world);
		this.multisampleFramebuffer.unbind();
		
		this.multisampleFramebuffer.resolveToFramebuffer(this.singlesampleFramebuffer);

		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glDisable(GL_DEPTH_TEST);
		glCullFace(GL_BACK);
		
		//POSTITERATE (EFFECTS, RENDER TO SCREEN)
		int finalOutput = this.singlesampleFramebuffer.getTexture(0);
		finalOutput = this.effectManager.getEffect("hblur", HorizontalBlurEffect.class).apply(this.singlesampleFramebuffer.getTexture(0));
		finalOutput = this.effectManager.getEffect("vblur", VerticalBlurEffect.class).apply(finalOutput);
		finalOutput = this.effectManager.getEffect("combine", CombineEffect.class).apply(this.singlesampleFramebuffer.getTexture(0), 1f, finalOutput, 3f);
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		this.effectManager.getEffect("draw", DrawEffect.class).apply(finalOutput);
		
		//try to free buffers from heap
		System.gc();
	}
	
	//render scene using VBO
	private void renderVBO(Matrix4f modelTransformMatrix, int vbo, int ibo, int iboCount, Vector3f color, int primitiveType)
	{
		glUseProgram(this.shader.getId());
		//this.shader.updateUniforms(modelTransformMatrix, this.viewTransformMatrix, this.cameraComponent);

		Matrix4f M = modelTransformMatrix;
		Matrix4f P = this.cameraComponent.projection;
		this.shader.setUniform("M", M);
		this.shader.setUniform("V", this.viewTransformMatrix);
		this.shader.setUniform("P", P);
		
		glVertexAttrib3f(4, color.x, color.y, color.z);
		glEnableVertexAttribArray(0);
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
				glVertexAttribPointer(0, 2, GL_FLOAT, false, 2 * 4, 0);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
				glDrawElements(primitiveType, iboCount, GL_UNSIGNED_INT, 0);
			glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDisableVertexAttribArray(0);
	}

	@Override	
	public void cleanUp()
	{
		System.out.println(this.getClass().getSimpleName() + " CLEAN!!!!!!!!");
		this.multisampleFramebuffer.cleanUp();
	}
}
