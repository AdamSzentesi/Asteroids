package asteroids.subsystems.render3D;

import java.util.ArrayList;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.*;
import asteroids.Util;
import java.util.HashSet;
import static org.lwjgl.opengl.GL32.*;

public class Framebuffer
{
	private int framebufferId;
	private int width;
	private int height;
	private int samples = 1;
	//TODO: fill up
	private ArrayList<Integer> texturebuffers;
	private ArrayList<Integer> renderbuffers;
	private HashSet<Integer> drawbuffers;
	
	public Framebuffer(int width, int height, int samples)
	{
		System.out.println("framebuffer: " + width + "/" + height);
		this.framebufferId = glGenFramebuffers();
		this.width = width;
		this.height = height;
		this.samples = samples;
		this.texturebuffers = new ArrayList<>();
		this.renderbuffers = new ArrayList<>();
		this.drawbuffers = new HashSet<>();//TODO should be a number from autosetup: GL_MAX_COLOR_ATTACHMENTS / GL_MAX_DRAW_BUFFERS
		glBindFramebuffer(GL_FRAMEBUFFER, this.framebufferId);
		glGetError();
		glCheckFramebufferStatus(GL_FRAMEBUFFER);
		System.out.println("Framebuffer check " + glGetError());
	}
	
	public Framebuffer(int width, int height)
	{
		this(width, height, 1);
	}
	
	public int getId()
	{
		return this.framebufferId;
	}
	
	public int getTexture(int textureId)
	{
		return this.texturebuffers.get(textureId);
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getHeight()
	{
		return this.height;
	}
	
	public void bind()
	{
		glBindFramebuffer(GL_FRAMEBUFFER, this.framebufferId);
		glViewport(0, 0, this.width, this.height);
	}
	
	public void unbind()
	{
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}
	
	public void cleanUp()
	{
		glDeleteFramebuffers(this.framebufferId);
		for(int texturebufferId : this.texturebuffers)
		{
			glDeleteTextures(texturebufferId);
		}
		for(int renderbufferId : this.renderbuffers)
		{
			glDeleteRenderbuffers(renderbufferId);
		}
	}
	
	public void updateDrawbuffers()
	{
		int[] drawbuffersArray = Util.toIntArray(this.drawbuffers.toArray(new Integer[this.drawbuffers.size()]));
		glDrawBuffers(Util.makeFlippedBuffer(drawbuffersArray));
	}
	
	public void resolveToFramebuffer(Framebuffer drawFramebuffer)
	{
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, drawFramebuffer.framebufferId);
		glBindFramebuffer(GL_READ_FRAMEBUFFER, this.framebufferId);
		glBlitFramebuffer
		(
			0, 0, this.width, this.height,
			0, 0,	drawFramebuffer.width, drawFramebuffer.height,
			GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT, GL_NEAREST
		);
		this.unbind();
	}

	public void resolveToScreen()
	{
		glBindFramebuffer(GL_READ_FRAMEBUFFER, this.framebufferId);
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
		glDrawBuffer(GL_BACK);
		glBlitFramebuffer
		(
			0, 0, this.width, this.height,
			0, 0,	Display.getWidth(), Display.getHeight(),
			GL_COLOR_BUFFER_BIT, GL_NEAREST
		);
		this.unbind();
	}	
	
	//create new texturebuffer
	public int createTexturebuffer(int attachment, int format, int type, boolean draw)
	{
		glBindFramebuffer(GL_FRAMEBUFFER, this.framebufferId);
		int result = glGenTextures();
		System.out.println(" texture" + this.texturebuffers.size() + ": " + this.width + "/" + this.height + " - " + result);
		if(this.samples > 1)
		{
			System.out.println(" MS");
			glBindTexture(GL_TEXTURE_2D_MULTISAMPLE, result);
			glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE, this.samples, format, this.width, this.height, true);
		}
		else
		{
			glBindTexture(GL_TEXTURE_2D, result);
			glTexImage2D(GL_TEXTURE_2D, 0, format, this.width, this.height, 0, GL_RGB, type, (java.nio.ByteBuffer) null);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		}
		glFramebufferTexture2D(GL_FRAMEBUFFER, attachment, GL_TEXTURE_2D, result, 0);

		this.texturebuffers.add(result);
		if(draw)
		{
			this.drawbuffers.add(attachment);
		}
		return result;
	}
	public int createTexturebuffer(int attachment, int format, int type)
	{
		int result = createTexturebuffer(attachment, format, type, false);
		return result;
	}
	
	//create new renderbuffer
	public int createRenderbuffer(int depth, int attachment, boolean draw)
	{
		glBindFramebuffer(GL_FRAMEBUFFER, this.framebufferId);
		int result = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, result);
		if(this.samples > 1)
		{
			glRenderbufferStorageMultisample(GL_RENDERBUFFER, this.samples, depth, this.width, this.height);
		}
		else
		{
			glRenderbufferStorage(GL_RENDERBUFFER, depth, this.width, this.height);
		}
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, attachment ,GL_RENDERBUFFER, result);
		this.renderbuffers.add(result);
		if(draw)
		{
			this.drawbuffers.add(attachment);
		}
		return result;
	}
	public int createRenderbuffer(int depth, int attachment)
	{
		int result = createRenderbuffer(depth, attachment, false);
		return result;
	}
	
	public void enableBlending()
	{
		 glEnablei(GL_BLEND, this.framebufferId);
	}

}
