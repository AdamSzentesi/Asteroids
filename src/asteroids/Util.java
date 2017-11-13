package asteroids;

import asteroids.math.Matrix3f;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import asteroids.math.Matrix4f;

public class Util
{
	public Util()
	{
	
	}
	//return FloatBuffer from float Array
	public static FloatBuffer makeFlippedBuffer(float[] input)
	{
		FloatBuffer outBuffer = BufferUtils.createFloatBuffer(input.length);
		outBuffer.put(input);
		outBuffer.flip();
		return outBuffer;
	}
	public static ByteBuffer makeFlippedByteBuffer(int capacity)
	{
		ByteBuffer outBuffer = BufferUtils.createByteBuffer(capacity);
		outBuffer.flip();
		return outBuffer;
	}
	
	public static ByteBuffer createByteBuffer(int capacity)
	{
		ByteBuffer outBuffer = BufferUtils.createByteBuffer(capacity);
		return outBuffer;
	}
		
	//return IntBuffer from int Array
	public static IntBuffer makeFlippedBuffer(int[] input)
	{
		IntBuffer outBuffer = BufferUtils.createIntBuffer(input.length);
		outBuffer.put(input);
		outBuffer.flip();
		return outBuffer;
	}
	
	//return IntBuffer from matrix
	public static FloatBuffer createFlippedBuffer(Matrix4f matrix)
	{
		FloatBuffer outBuffer = BufferUtils.createFloatBuffer(4 * 4);
		
		for(int i = 0; i < 4; i++)
		{	
			for(int j = 0; j < 4; j++)
			{
				outBuffer.put(matrix.getValue(i, j));
			}
		}
		outBuffer.flip();
		return outBuffer;
	}
	
	//return IntBuffer from matrix
	public static FloatBuffer createFlippedBuffer(Matrix3f matrix)
	{
		FloatBuffer outBuffer = BufferUtils.createFloatBuffer(3 * 3);
		
		for(int i = 0; i < 3; i++)
		{	
			for(int j = 0; j < 3; j++)
			{
				outBuffer.put(matrix.getValue(i, j));
			}
		}
		outBuffer.flip();
		return outBuffer;
	}
	
	public static String[] removeEmptyStrings(String[] data)
	{
		ArrayList<String> result = new ArrayList<String>();
		
		for(int i = 0; i < data.length; i++)
			if(!data[i].equals(""))
				result.add(data[i]);
		
		String[] res = new String[result.size()];
		result.toArray(res);
		
		return res;
	}
	
	public static int[] toIntArray(Integer[] data)
	{
		int[] result = new int[data.length];
		for(int i = 0; i < data.length; i++)
		{
			result[i] = data[i].intValue();
		}
		return(result);
	}
}
