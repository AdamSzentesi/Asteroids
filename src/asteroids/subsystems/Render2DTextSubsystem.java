package asteroids.subsystems;

import asteroids.subsystems.render3D.Shader;
import asteroids.World;
import asteroids.components.CameraComponent;
import asteroids.components.Geometry2D.*;
import asteroids.components.Geometry3D.Render3DMesh.Texture;
import asteroids.math.Matrix4f;
import asteroids.math.Pair;
import asteroids.math.Vector2f;
import asteroids.math.Vector3f;
import asteroids.subsystems.render3D.Framebuffer;
import asteroids.subsystems.render3D.Postprocessing.*;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Render2DTextSubsystem extends Subsystem
{
	private Shader shader;
	private Texture fontTexture;
	private Map<Character, Pair<Integer, Integer>> alphabet;
	
	private Framebuffer multisampleFramebuffer;
	private Framebuffer singlesampleFramebuffer;
	private EffectManager effectManager;
	
	public Render2DTextSubsystem()
	{
		//shader setup
		shader = new Shader();
		shader.addShader("render2D/text2D.vs", GL_VERTEX_SHADER);
		//shader.addShader("render2D/line2D.gs", GL_GEOMETRY_SHADER);
		shader.addShader("render2D/text2D.fs", GL_FRAGMENT_SHADER);
		shader.addAttribute(0, "position");
		shader.addAttribute(1, "tex");
		shader.addAttribute(4, "color");
		shader.addOutput(0, "outDiffuse");
		shader.link();
		shader.addUniform("displayPosition");
		shader.addUniform("displaySize");
		shader.addUniform("diffuseSampler");
		shader.addUniform("character");
		
		//font texture setup
		this.fontTexture = new Texture("font.png", Texture.NEAREST_FILTERING);
		
		//alphabet setup
		this.alphabet = new HashMap();
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
		this.alphabet.put('[', new Pair(10, 5));
		this.alphabet.put('\\', new Pair(11, 5));
		this.alphabet.put(']', new Pair(12, 5));
		this.alphabet.put('^', new Pair(13, 5));
		this.alphabet.put('_', new Pair(14, 5));
		this.alphabet.put('`', new Pair(15, 5));
		
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
	
	@Override
	public void process(World world, float delta)
	{
		glLineWidth(1);
		
//RENDERING TO BUFFEROBJECT
		this.multisampleFramebuffer.bind();
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glDisable(GL_DEPTH_TEST);
		glFrontFace(GL_CW);
		glEnable(GL_CULL_FACE);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		//iterate through geometry
		for(int entityId : this.getList("primary"))
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
//		finalOutput = this.effectManager.getEffect("hblur", HorizontalBlurEffect.class).apply(this.singlesampleFramebuffer.getTexture(0));
//		finalOutput = this.effectManager.getEffect("vblur", VerticalBlurEffect.class).apply(finalOutput);
//		finalOutput = this.effectManager.getEffect("combine", CombineEffect.class).apply(this.singlesampleFramebuffer.getTexture(0), 1f, finalOutput, 3f);
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		this.effectManager.getEffect("draw", DrawEffect.class).apply(finalOutput);
		
		//try to free buffers from heap
		System.gc();
	}
	
	//render scene using VBO
	private void renderVBO(int x, int y, int width, int height, int vbo, int ibo, int iboCount, Vector3f color, int primitiveType, Vector2f characterPosition)
	{
		//activate texture in slot 0: diffuse
		glActiveTexture(GL_TEXTURE0);
			this.fontTexture.bind();
		
		//use shader
		glUseProgram(this.shader.getId());
		
		float halfWidth = (float)Display.getWidth() / 2;
		float halfHeight = (float)Display.getHeight()/ 2;
		Vector2f displayPosition = new Vector2f((1f / halfWidth) * x, (1f / halfHeight) * y);
		Vector2f displaySize = new Vector2f((1f / halfWidth) * width, (1f / halfHeight) * height);
		//update uniforms
		this.shader.setUniform("displayPosition", displayPosition);
		this.shader.setUniform("displaySize", displaySize);
		this.shader.setUniform("diffuseSampler", GL_TEXTURE0);
		this.shader.setUniform("character", characterPosition);
		
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

	@Override	
	public void cleanUp()
	{
		System.out.println(this.getClass().getSimpleName() + " CLEAN!!!!!!!!");
		this.multisampleFramebuffer.cleanUp();
	}
}
