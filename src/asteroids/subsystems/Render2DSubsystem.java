package asteroids.subsystems;

import asteroids.subsystems.render3D.Shader;
import asteroids.World;
import asteroids.components.CameraComponent;
import asteroids.components.Collider.Collider2DComponent;
import asteroids.components.Collider.Shapes.*;
import asteroids.components.Geometry2D.*;
import asteroids.components.Geometry3D.Transform3DComponent;
import asteroids.math.Matrix4f;
import asteroids.math.Vector2f;
import asteroids.math.Vector3f;
import asteroids.subsystems.render2D.primitives.Debug2DPrimitive;
import asteroids.subsystems.render2D.primitives.Debug2DPrimitiveRectangle;
import asteroids.subsystems.render3D.Framebuffer;
import asteroids.subsystems.render3D.Postprocessing.*;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Render2DSubsystem extends Subsystem
{
	private Shader shader;
	private CameraComponent cameraComponent;
	private Matrix4f viewTransformMatrix;
	private int cameraEntityId;
	
	private Framebuffer multisampleFramebuffer;
	private Framebuffer singlesampleFramebuffer;
	private EffectManager effectManager;
	
	private Debug2DPrimitiveRectangle debug2DPrimitiveRectangle = new Debug2DPrimitiveRectangle();
	
	public Render2DSubsystem()
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
			Render2DLineComponent render2DLineComponent = world.getComponent(entityId, Render2DLineComponent.class);
			Transform2DComponent transform2DComponent = world.getComponent(entityId, Transform2DComponent.class);
			Matrix4f modelTransformMatrix = transform2DComponent.getWorldMatrix();
			renderVBO(modelTransformMatrix, render2DLineComponent.vbo, render2DLineComponent.ibo, render2DLineComponent.iboCount, render2DLineComponent.color, GL_LINE_STRIP);
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
	
//	
//	private void renderColliders(World world)
//	{
//		//iterate through colliders
//		for(int entityId : this.getList("colliders"))
//		{
//			Collider2DComponent collider2DComponent = world.getComponent(entityId, Collider2DComponent.class);
//			Transform2DComponent transform2DComponent = world.getComponent(entityId, Transform2DComponent.class);
//
//			//get collider position
//			Vector2f colliderPosition = collider2DComponent.position;
//			Matrix4f colliderPositionMatrix = new Matrix4f().initTranslation(colliderPosition.x, colliderPosition.y, 0);
//			//get collider scale
//			Vector2f colliderScale = collider2DComponent.getColliderSize();
//			Matrix4f colliderScaleMatrix = new Matrix4f().initScale(colliderScale.x, colliderScale.y, 1);
//			//final matrix
//			Matrix4f colliderMatrix = colliderPositionMatrix;
//							
//			//render collider
//			Matrix4f worldMatrix = transform2DComponent.getWorldMatrix();
//			String colliderShapeClass = collider2DComponent.collider2DShape.getClass().getSimpleName();
//			Debug2DPrimitive primitive = new Debug2DPrimitive();
//			int	primitiveType = GL_LINE_STRIP;
//			switch (colliderShapeClass)
//			{
//				case "Collider2DShapeRectangle":
//					colliderMatrix = colliderPositionMatrix.multiply(colliderScaleMatrix);
//					primitive = collider2DComponent.getShape(Collider2DShapeRectangle.class).debug2DPrimitive;
//					break;
//				case "Collider2DShapeCircle":
//					colliderMatrix = colliderPositionMatrix.multiply(colliderScaleMatrix);
//					primitive = collider2DComponent.getShape(Collider2DShapeCircle.class).debug2DPrimitive;
//					break;
//				case "Collider2DShapeLine":
//					colliderMatrix = colliderPositionMatrix.multiply(colliderScaleMatrix);
//					primitive = collider2DComponent.getShape(Collider2DShapeLine.class).debug2DPrimitive;
//					break;
//				case "Collider2DShapeMultiline":
//					colliderMatrix = colliderPositionMatrix.multiply(colliderScaleMatrix);
//					primitive = collider2DComponent.getShape(Collider2DShapeMultiline.class).debug2DPrimitive;
//					primitiveType = GL_LINES;
//					break;
//				case "Collider2DShapePoint":
//					colliderMatrix = colliderPositionMatrix.multiply(colliderScaleMatrix);
//					primitive = collider2DComponent.getShape(Collider2DShapePoint.class).debug2DPrimitive;
//					break;
//			}
//			//push to world coords
//			colliderMatrix = worldMatrix.multiply(colliderMatrix);
//			renderVBO(colliderMatrix, primitive.vbo, primitive.ibo, primitive.iboCount, new Vector3f(1.0f, 1.0f, 0.0f), primitiveType);
//
//			//debug
//			//renderAABB(worldMatrix, colliderPosition, transform2DComponent, collider2DComponent);
//		}
//	}
//
//	//render AABB	TEMPORARY!!!!
//	private void renderAABB(Matrix4f worldMatrix, Vector2f colliderPosition, Transform2DComponent transform2DComponent, Collider2DComponent collider2DComponent)
//	{
//		//world rotation
//		float rotation = transform2DComponent.getWorldRotation();
//		Matrix4f colliderAABBRotationMatrix = new Matrix4f().initRotation(0, 0, rotation);
//		//collider scale
//		Vector2f[] colliderAABBSize = collider2DComponent.getAABBSize(colliderAABBRotationMatrix);
//		Vector2f colliderAABBMidpoint = colliderAABBSize[1].add(colliderAABBSize[0]).divide(2f);
//		Vector2f colliderAABBScale = colliderAABBSize[1].subtract(colliderAABBSize[0]);
//		Matrix4f colliderAABBScaleMatrix = new Matrix4f().initScale(colliderAABBScale.x, colliderAABBScale.y, 1);
//		//world position
//		Vector2f colliderAABBPosition = worldMatrix.transform(colliderPosition).add(colliderAABBMidpoint);
//		Matrix4f colliderAABBPositionMatrix = new Matrix4f().initTranslation(colliderAABBPosition.x, colliderAABBPosition.y, 0);
//
//		Matrix4f colliderAABBMatrix = colliderAABBPositionMatrix.multiply(colliderAABBScaleMatrix);
//		renderVBO(colliderAABBMatrix, debug2DPrimitiveRectangle.vbo, debug2DPrimitiveRectangle.ibo, debug2DPrimitiveRectangle.iboCount, new Vector3f(0.0f, 1.0f, 0.0f), GL_LINE_STRIP);
//	}
//	

	@Override	
	public void cleanUp()
	{
		System.out.println(this.getClass().getSimpleName() + " CLEAN!!!!!!!!");
		this.multisampleFramebuffer.cleanUp();
	}
}
