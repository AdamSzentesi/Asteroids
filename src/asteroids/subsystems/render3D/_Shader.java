package asteroids.subsystems.render3D;

import asteroids.Util;
import asteroids.components.CameraComponent;
import asteroids.components.Geometry3D.Render3DMesh.Material;
import asteroids.components.Geometry3D.Transform3DComponent;
import asteroids.math.Matrix4f;
import asteroids.math.Vector3f;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.GL_INTERLEAVED_ATTRIBS;
import static org.lwjgl.opengl.GL30.glTransformFeedbackVaryings;

public class _Shader
{
	private int id;
	private HashMap<String, Integer> uniforms;
	
	public _Shader()
	{
		this.id = glCreateProgram();
		this.uniforms = new HashMap<>();
	}
	
	//gets shader handle
	public int getId()
	{
		return this.id;
	}
	
	//adds shader to program
	public void addShader(String shaderName, int type)
	{
		int shader = glCreateShader(type);
		String shaderData = loadShader(shaderName);
		glShaderSource(shader, shaderData);
		glCompileShader(shader);
		glAttachShader(this.id, shader);
	}
	
	//loads shader text file
	private static String loadShader(String fileName)
	{
		StringBuilder shaderSource = new StringBuilder();
		BufferedReader shaderReader = null;
		
		try
		{
			//reads the file in buffered mode
			shaderReader = new BufferedReader(new FileReader("./res/shaders/" + fileName));
			
			//add every line in the source
			String line;
			while((line = shaderReader.readLine()) != null)
			{
				shaderSource.append(line).append("\n");
			}
			
			shaderReader.close();
		}
		catch (Exception ex)
		{
			System.out.println("GLShader loadShader error!");
			System.exit(1);
		}
		
		return shaderSource.toString();
	}
	
	//adds attribute
	public void addAttribute(int location, String attributeName)
	{
		glBindAttribLocation(this.id, location, attributeName);
	}
	
	//adds uniform
	public void addUniform(String uniform)
	{
		int uniformLocation = glGetUniformLocation(this.id, uniform);
		
		if(uniformLocation == 0xFFFFFFFF)
		{
			System.err.println("Error: could not find glUniform: " + uniform);
			System.exit(1);
		}
		//System.out.println(uniform + ":" + uniformLocation);
		uniforms.put(uniform, uniformLocation);
	}
	
	//sets uniforms
	public void setUniform(String uniform, Vector3f vector3f)
	{
		glUniform3f(uniforms.get(uniform), vector3f.getX(), vector3f.getY(), vector3f.getZ());
	}
	public void setUniform(String uniform, Matrix4f matrix)
	{
		glUniformMatrix4(uniforms.get(uniform), true, Util.createFlippedBuffer(matrix));
	}
	public void setUniform(String uniform, float number)
	{
		glUniform1f(uniforms.get(uniform), number);
	}
	
	//link before rendering
	public void link()
	{
		glLinkProgram(this.id);
	}
	
	//enables data feedback on given output
	public void enableFeedback(CharSequence[] outputs)
	{
		//define which varyings we get back from shader
		glTransformFeedbackVaryings(this.id, outputs, GL_INTERLEAVED_ATTRIBS);
	}
	
	public void updateUniforms(Matrix4f modelMatrix, Matrix4f viewMatrix, CameraComponent cameraCamera, Material material, Vector3f viewPosition){}
	public void updateUniforms(Matrix4f modelTransformMatrix, Matrix4f cameraTransformMatrix, CameraComponent cameraCamera){}
	public void updateUniforms(Matrix4f cameraTransformMatrix, CameraComponent cameraCamera){}
	public void updateUniforms(Matrix4f cameraTransformMatrix){}
	public void updateUniforms(Transform3DComponent cameraTransform, CameraComponent cameraCamera){}
	//public void updateUniforms(Matrix4f modelTransform, Transform3DComponent cameraTransform, CameraComponent cameraCamera){}
	//public void updateUniforms(Transform3DComponent cameraTransform){}
	
}
