package asteroids.subsystems;

import asteroids.subsystems.render3D.Shader;
import asteroids.World;
import asteroids.components.CameraComponent;
import asteroids.components.Collider.Collider2DComponent;
import asteroids.components.Collider.Shapes.*;
import asteroids.components.Geometry2D.*;
import asteroids.components.Geometry3D.Render3DMesh.Texture;
import asteroids.components.Geometry3D.Transform3DComponent;
import asteroids.math.Matrix4f;
import asteroids.math.Pair;
import asteroids.math.Vector2f;
import asteroids.math.Vector3f;
import asteroids.subsystems.render2D.primitives.Debug2DPrimitive;
import asteroids.subsystems.render2D.primitives.Debug2DPrimitiveRectangle;
import asteroids.subsystems.render3D.Framebuffer;
import asteroids.subsystems.render3D.Postprocessing.*;
import static asteroids.subsystems.ThrustSubsystem.*;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
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
	
	//texte rendering
	private Shader textShader;
	private Texture fontTexture;
	private Map<Character, Pair<Integer, Integer>> alphabet;
	
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
		
		//shader setup
		textShader = new Shader();
		textShader.addShader("render2D/text2D.vs", GL_VERTEX_SHADER);
		//textShader.addShader("render2D/line2D.gs", GL_GEOMETRY_SHADER);
		textShader.addShader("render2D/text2D.fs", GL_FRAGMENT_SHADER);
		textShader.addAttribute(0, "position");
		textShader.addAttribute(1, "tex");
		textShader.addAttribute(4, "color");
		textShader.addOutput(0, "outDiffuse");
		textShader.link();
		textShader.addUniform("displayPosition");
		textShader.addUniform("displaySize");
		textShader.addUniform("diffuseSampler");
		textShader.addUniform("character");
		
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
		
		//font texture setup
		this.fontTexture = new Texture("font.png", Texture.NEAREST_FILTERING);
		
		//alphabet setup
		this.alphabet = new HashMap();
		this.alphabet.put(' ', new Pair(0, 2));
		
		this.alphabet.put('0', new Pair(15, 2));
		
		this.alphabet.put('1', new Pair(0, 3));
		this.alphabet.put('2', new Pair(1, 3));
		this.alphabet.put('3', new Pair(2, 3));
		this.alphabet.put('4', new Pair(3, 3));
		this.alphabet.put('5', new Pair(4, 3));
		this.alphabet.put('6', new Pair(5, 3));
		this.alphabet.put('7', new Pair(6, 3));
		this.alphabet.put('8', new Pair(7, 3));
		this.alphabet.put('9', new Pair(8, 3));
		this.alphabet.put(':', new Pair(9, 3));
		this.alphabet.put(';', new Pair(10, 3));
		this.alphabet.put('<', new Pair(11, 3));
		this.alphabet.put('=', new Pair(12, 3));
		this.alphabet.put('>', new Pair(13, 3));
		this.alphabet.put('?', new Pair(14, 3));
		this.alphabet.put('@', new Pair(15, 3));
		
		this.alphabet.put('A', new Pair(0, 4));
		this.alphabet.put('B', new Pair(1, 4));
		this.alphabet.put('C', new Pair(2, 4));
		this.alphabet.put('D', new Pair(3, 4));
		this.alphabet.put('E', new Pair(4, 4));
		this.alphabet.put('F', new Pair(5, 4));
		this.alphabet.put('G', new Pair(6, 4));
		this.alphabet.put('H', new Pair(7, 4));
		this.alphabet.put('I', new Pair(8, 4));
		this.alphabet.put('J', new Pair(9, 4));
		this.alphabet.put('K', new Pair(10, 4));
		this.alphabet.put('L', new Pair(11, 4));
		this.alphabet.put('M', new Pair(12, 4));
		this.alphabet.put('N', new Pair(13, 4));
		this.alphabet.put('O', new Pair(14, 4));
		this.alphabet.put('P', new Pair(15, 4));
		
		this.alphabet.put('Q', new Pair(0, 5));
		this.alphabet.put('R', new Pair(1, 5));
		this.alphabet.put('S', new Pair(2, 5));
		this.alphabet.put('T', new Pair(3, 5));
		this.alphabet.put('U', new Pair(4, 5));
		this.alphabet.put('V', new Pair(5, 5));
		this.alphabet.put('W', new Pair(6, 5));
		this.alphabet.put('X', new Pair(7, 5));
		this.alphabet.put('Y', new Pair(8, 5));
		this.alphabet.put('Z', new Pair(9, 5));
		this.alphabet.put('[', new Pair(10, 5));
		this.alphabet.put('\\', new Pair(11, 5));
		this.alphabet.put(']', new Pair(12, 5));
		this.alphabet.put('^', new Pair(13, 5));
		this.alphabet.put('_', new Pair(14, 5));
		this.alphabet.put('`', new Pair(15, 5));
		
		this.alphabet.put('a', new Pair(0, 4));
		this.alphabet.put('b', new Pair(1, 4));
		this.alphabet.put('c', new Pair(2, 4));
		this.alphabet.put('d', new Pair(3, 4));
		this.alphabet.put('e', new Pair(4, 4));
		this.alphabet.put('f', new Pair(5, 4));
		this.alphabet.put('g', new Pair(6, 4));
		this.alphabet.put('h', new Pair(7, 4));
		this.alphabet.put('i', new Pair(8, 4));
		this.alphabet.put('j', new Pair(9, 4));
		this.alphabet.put('k', new Pair(10, 4));
		this.alphabet.put('l', new Pair(11, 4));
		this.alphabet.put('m', new Pair(12, 4));
		this.alphabet.put('n', new Pair(13, 4));
		this.alphabet.put('o', new Pair(14, 4));
		this.alphabet.put('p', new Pair(15, 4));
		
		this.alphabet.put('q', new Pair(0, 5));
		this.alphabet.put('r', new Pair(1, 5));
		this.alphabet.put('s', new Pair(2, 5));
		this.alphabet.put('t', new Pair(3, 5));
		this.alphabet.put('u', new Pair(4, 5));
		this.alphabet.put('v', new Pair(5, 5));
		this.alphabet.put('w', new Pair(6, 5));
		this.alphabet.put('x', new Pair(7, 5));
		this.alphabet.put('y', new Pair(8, 5));
		this.alphabet.put('z', new Pair(9, 5));
		this.alphabet.put('{', new Pair(10, 7));
		this.alphabet.put('|', new Pair(11, 7));
		this.alphabet.put('}', new Pair(12, 7));
		this.alphabet.put('~', new Pair(13, 7));
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
		glLineWidth(2);
		//get current camera view matrix
		this.viewTransformMatrix = world.getComponent(this.cameraEntityId, CameraComponent.class).viewMatrix;
		
//RENDERING TO BUFFEROBJECT
		this.multisampleFramebuffer.bind();
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		glDisable(GL_DEPTH_TEST);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		//iterate through geometry
		for(int entityId : this.getPrimaryList())
		{
			//System.out.println(entityId);
			Render2DLineComponent render2DLineComponent = world.getComponent(entityId, Render2DLineComponent.class);
			Transform2DComponent transform2DComponent = world.getComponent(entityId, Transform2DComponent.class);
			Matrix4f modelTransformMatrix = transform2DComponent.getWorldMatrix();
			renderVBO(modelTransformMatrix, render2DLineComponent.vbo, render2DLineComponent.ibo, render2DLineComponent.iboCount, render2DLineComponent.color, GL_LINE_STRIP);
		}
		
		//iterate text
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		for(int entityId : this.getList("text"))
		{
			Render2DTextComponent render2DTextComponent = world.getComponent(entityId, Render2DTextComponent.class);
			for(int i = 0; i < render2DTextComponent.get().length(); i++)
			{
				Pair<Integer, Integer> character = this.alphabet.get(render2DTextComponent.string.charAt(i));
				Vector2f characterPosition = new Vector2f(character.a, character.b);
				renderVBO(render2DTextComponent.x + i * render2DTextComponent.width, render2DTextComponent.y, render2DTextComponent.width, render2DTextComponent.height, render2DTextComponent.vbo, render2DTextComponent.ibo, render2DTextComponent.iboCount, render2DTextComponent.color, GL_TRIANGLES, characterPosition);
			}
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
		finalOutput = this.effectManager.getEffect("hblur", HorizontalBlurEffect.class).apply(finalOutput);
		finalOutput = this.effectManager.getEffect("vblur", VerticalBlurEffect.class).apply(finalOutput);
		finalOutput = this.effectManager.getEffect("combine", CombineEffect.class).apply(this.singlesampleFramebuffer.getTexture(0), 1f, finalOutput, 1.5f);
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
	
	//render scene using VBO
	private void renderVBO(int x, int y, int width, int height, int vbo, int ibo, int iboCount, Vector3f color, int primitiveType, Vector2f characterPosition)
	{
		//activate texture in slot 0: diffuse
		glActiveTexture(GL_TEXTURE0);
			this.fontTexture.bind();
		
		//use shader
		glUseProgram(this.textShader.getId());
		
		float halfWidth = (float)Display.getWidth() / 2;
		float halfHeight = (float)Display.getHeight()/ 2;
		Vector2f displayPosition = new Vector2f((1f / halfWidth) * x, (1f / halfHeight) * y);
		Vector2f displaySize = new Vector2f((1f / halfWidth) * width, (1f / halfHeight) * height);
		//update uniforms
		this.textShader.setUniform("displayPosition", displayPosition);
		this.textShader.setUniform("displaySize", displaySize);
		this.textShader.setUniform("diffuseSampler", GL_TEXTURE0);
		this.textShader.setUniform("character", characterPosition);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);	
			glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * 4, 0);
			glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * 4, 8);
			glVertexAttrib3f(4, color.x, color.y, color.z);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
			glDrawElements(primitiveType, iboCount, GL_UNSIGNED_INT, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
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
