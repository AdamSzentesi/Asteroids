package asteroids.components.Geometry3D.Render3DMesh;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import static org.lwjgl.opengl.GL11.*;
import asteroids.Util;

public class Texture
{
	private int id;
	
	public Texture(String fileName)
	{
		this.id = loadTexture(fileName);
		System.out.println("Texture loaded at buffer: " + id);
	}
	
	public Texture(int code)
	{
		//create new full white 1 pixel size texture image
		float[] pixelArray = new float[]{0.0f, 0.0f, 0.0f, 0.0f};
		
		int textureId = glGenTextures();

		glBindTexture(GL_TEXTURE_2D, textureId);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, 1, 1, 0, GL_RGBA, GL_UNSIGNED_BYTE, Util.makeFlippedBuffer(pixelArray));	

		glBindTexture(GL_TEXTURE_2D, 0);
		
		this.id = textureId;
	}
	
	public void bind()
	{
		glBindTexture(GL_TEXTURE_2D, this.id);
	}
	
	public int getId()
	{
		return this.id;
	}
	
	private int loadTexture(String fileName)
	{
		try
		{
			BufferedImage image = ImageIO.read(new File("./res/textures/" + fileName));
			int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
			ByteBuffer buffer = Util.createByteBuffer(image.getHeight() * image.getWidth() * 4);
			
			boolean hasAlpha = image.getColorModel().hasAlpha();
			for(int y = 0; y < image.getHeight(); y++)
			{
				for(int x = 0; x < image.getWidth(); x++)
				{
					int pixel = pixels[y * image.getWidth() + x];

					buffer.put((byte)((pixel >> 16) & 0xFF));
					buffer.put((byte)((pixel >> 8) & 0xFF));
					buffer.put((byte)((pixel) & 0xFF));
					if(hasAlpha)
						buffer.put((byte)((pixel >> 24) & 0xFF));
					else
						buffer.put((byte)(0xFF));
				}
			}
			buffer.flip();

			int textureId = glGenTextures();

			glBindTexture(GL_TEXTURE_2D, textureId);

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
			
			glBindTexture(GL_TEXTURE_2D, 0);
			
			return textureId;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		return -1;
	}
}
