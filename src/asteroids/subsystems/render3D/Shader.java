package asteroids.subsystems.render3D;

import asteroids.Util;
import asteroids.components.CameraComponent;
import asteroids.math.Matrix4f;
import asteroids.math.Vector3f;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import asteroids.components.Geometry3D.PointLightComponent;
import asteroids.components.Geometry3D.Render3DMesh.Material;
import asteroids.components.Geometry3D.Transform3DComponent;
import asteroids.math.Matrix3f;
import asteroids.math.Pair;
import asteroids.math.Vector2f;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

public class Shader
{
	private int id;
	private HashMap<String, Integer> uniforms;
	
	public Shader()
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
			System.out.println("GLShader loadShader error! " + fileName);
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
	public void setUniform(String uniform, Vector2f vector)
	{
		glUniform2f(uniforms.get(uniform), vector.getX(), vector.getY());
	}
	public void setUniform(String uniform, Matrix4f matrix)
	{
		glUniformMatrix4(uniforms.get(uniform), true, Util.createFlippedBuffer(matrix));
	}
	public void setUniform(String uniform, Matrix3f matrix)
	{
		glUniformMatrix3(uniforms.get(uniform), true, Util.createFlippedBuffer(matrix));
	}
	public void setUniform(String uniform, float number)
	{
		glUniform1f(uniforms.get(uniform), number);
	}
	public void setUniform(String uniform, int number)
	{
		glUniform1i(uniforms.get(uniform), number);
	}
	public void setUniform(String uniform, Pair<Integer, Integer> pair)
	{
		glUniform2i(uniforms.get(uniform), pair.a, pair.b);
	}
	public void setUniform(String uniform, PointLightComponent pointLight, Transform3DComponent transformLight, int id)
	{
		int uniformLocation;
		uniformLocation = glGetUniformLocation(this.id, uniform + "[" + id + "].base.color");
		glUniform3f(uniformLocation, pointLight.lightComponent.color.x, pointLight.lightComponent.color.y, pointLight.lightComponent.color.z);
		uniformLocation = glGetUniformLocation(this.id, uniform + "[" + id + "].base.intensity");
		glUniform1f(uniformLocation, pointLight.lightComponent.intensity);
		
		uniformLocation = glGetUniformLocation(this.id, uniform + "[" + id + "].attenuation.constant");
		glUniform1f(uniformLocation, pointLight.attenuation.x);
		uniformLocation = glGetUniformLocation(this.id, uniform + "[" + id + "].attenuation.linear");
		glUniform1f(uniformLocation, pointLight.attenuation.y);
		uniformLocation = glGetUniformLocation(this.id, uniform + "[" + id + "].attenuation.exponential");
		glUniform1f(uniformLocation, pointLight.attenuation.z);
		
		uniformLocation = glGetUniformLocation(this.id, uniform + "[" + id + "].position");
		glUniform3f(uniformLocation, transformLight.worldTransform.position.x, transformLight.worldTransform.position.y, transformLight.worldTransform.position.z);
		uniformLocation = glGetUniformLocation(this.id, uniform + "[" + id + "].range");
		glUniform1f(uniformLocation, pointLight.range);
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

	public void updateUniforms(){}	
	public void updateUniforms(Matrix4f modelTransformMatrix, Matrix4f cameraTransformMatrix, CameraComponent cameraCamera){}
	public void updateUniforms(Matrix3f modelTransformMatrix, Matrix4f cameraTransformMatrix, CameraComponent cameraCamera){}
	public void updateUniforms(Matrix4f modelTransformMatrix, Matrix4f cameraTransformMatrix, CameraComponent cameraCamera, Material material, Vector3f viewPosition){}

	public void addOutput(int i, String outColor)
	{
		glBindFragDataLocation(this.id, i, outColor);
	}

	
}
