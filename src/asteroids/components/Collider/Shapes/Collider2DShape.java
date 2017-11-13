package asteroids.components.Collider.Shapes;

import asteroids.math.Matrix4f;
import asteroids.math.Vector2f;
import asteroids.subsystems.render2D.primitives.Debug2DPrimitive;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;

public class Collider2DShape
{
	public Debug2DPrimitive debug2DPrimitive;
	public int shapeKey = 1 << 0;
	
	public Vector2f[] getAABBSize (Matrix4f rotationMatrix)
	{
		return null;
	}
	
	public Vector2f getAABBMin (Matrix4f rotationMatrix)
	{
		return null;
	}
	
	public Vector2f getAABBMax (Matrix4f rotationMatrix)
	{
		return null;
	}
	
	public Vector2f getColliderSize()
	{
		return new Vector2f(1, 1);
	}
	
	@Override
	public void finalize()
	{
		glDeleteBuffers(this.debug2DPrimitive.ibo);
		glDeleteBuffers(this.debug2DPrimitive.vbo);
	}
}
