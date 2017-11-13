package asteroids.math;

public class Matrix4f
{
	private float[][] m;
	
	public Matrix4f()
	{
		m = new float[4][4];
	}
	
	public float[][] getM()
	{
		float[][] res = new float[4][4];
		
		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 4; j++)
				res[i][j] = m[i][j];
		
		return res;
	}
	
	//set the whole matrix
	public void setM(float[][] m)
	{
		this.m = m;
	}
	
	//get a single value from matrix
	public float getValue(int x, int y)
	{
		return m[x][y];
	}
	
	//set a single value in matrix
	public void setValue(int x, int y, float value)
	{
		this.m[x][y] = value;
	}
	
	//initiate as identity matrix
	public Matrix4f initIdentity()
	{
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				if(i == j)
				{
					this.m[i][j] = 1;
				}
				else
				{
					this.m[i][j] = 0;
				}
			}
		}
		return this;
	}
	
	//initiate as translation matrix
	public Matrix4f initTranslation(float x, float y, float z)
	{
		//cast idetity to matrix
		this.initIdentity();
		
		//set translation to matrix
		this.m[0][3] = x;
		this.m[1][3] = y;
		this.m[2][3] = z;
		this.m[3][3] = 1;
		
		return this;
	}
	public Matrix4f initTranslation(Vector3f translation)
	{
		this.initTranslation(translation.x, translation.y, translation.z);
		return this;
	}
	
	//initiate as rotation matrix
	public Matrix4f initRotation(float x, float y, float z)
	{
		//rotation matrices for each axis
		Matrix4f rotX = new Matrix4f();
		Matrix4f rotY = new Matrix4f();
		Matrix4f rotZ = new Matrix4f();
		
		//rotation angles for each axis
		x = (float)Math.toRadians(x);
		y = (float)Math.toRadians(y);
		z = (float)Math.toRadians(z);
		
		//cast idetity to matrices
		rotX.initIdentity();
		rotY.initIdentity();
		rotZ.initIdentity();
		
		//set rotation matrix for x component
		rotX.m[1][1] = (float)Math.cos(x);
		rotX.m[1][2] = -(float)Math.sin(x);
		rotX.m[2][1] = (float)Math.sin(x);
		rotX.m[2][2] = (float)Math.cos(x);
		
		//set rotation matrix for y component
		rotY.m[0][0] = (float)Math.cos(y);
		rotY.m[0][2] = -(float)Math.sin(y);
		rotY.m[2][0] = (float)Math.sin(y);
		rotY.m[2][2] = (float)Math.cos(y);
		
		//set rotation matrix for z component
		rotZ.m[0][0] = (float)Math.cos(z); 
		rotZ.m[0][1] = -(float)Math.sin(z);
		rotZ.m[1][0] = (float)Math.sin(z);
		rotZ.m[1][1] = (float)Math.cos(z);

		//multiply all the rotation matrices
		m = rotZ.multiply(rotY.multiply(rotX)).getM();
		
		return this;
	}
	
	//initiate as scale matrix
	public Matrix4f initScale(float x, float y, float z)
	{
		//cast idetity to matrix
		this.initIdentity();
		
		//set translation to matrix
		this.m[0][0] = x;
		this.m[1][1] = y;
		this.m[2][2] = z;
		
		return this;
	}
	public Matrix4f initScale(Vector3f scale)
	{
		this.initScale(scale.x, scale.y, scale.z);
		return this;
	}
	
	//initiate as projection matrix
	public Matrix4f initPerspective(float fov, float aspectRatio, float near, float far)
	{
		float range = near - far;
		float tanHalfFov = (float)Math.tan(Math.toRadians(fov) / 2);
		//cast idetity to matrix
		this.initIdentity();
		
		this.m[0][0] = 1.0f / (tanHalfFov * aspectRatio);
		this.m[1][1] = 1.0f / (tanHalfFov);
		this.m[2][2] = (-near - far) / range;
		this.m[2][3] = 2 * far * near / range;
		this.m[3][2] = 1.0f;
		this.m[3][3] = 0.0f;
		
		return this;
	}
	
	//initiate as ortho projection matrix
	public Matrix4f initOrthographic(float left, float right, float top, float bottom, float near, float far)
	{
		float width = right - left;
		float height = top - bottom;
		float depth = far - near;

		//cast idetity to matrix
		this.initIdentity();
		
		this.m[0][0] = 2.0f / width;
		this.m[1][1] = 2.0f / height;
		this.m[2][2] = -2.0f / depth;

		this.m[0][3] = -(left + right) / width;
		this.m[1][3] = -(top + bottom) / height;
		this.m[2][3] = -(far + near) / depth;
		
		return this;
	}
	
	//initiate as rotation matrix
	public Matrix4f initRotation(Vector3f forward, Vector3f up)
	{
		Vector3f f = forward.normalize();
		Vector3f r = up.normalize();
		r = r.cross(f);		
		Vector3f u = f.cross(r);
		
		return initRotation(f, u, r);
	}

	//initiate as rotation matrix
	public Matrix4f initRotation(Vector3f forward, Vector3f up, Vector3f right)
	{
		Vector3f f = forward;
		Vector3f u = up;
		Vector3f r = right;
		
		//cast idetity to matrix
		this.initIdentity();
		
		//set translation to matrix
		this.m[0][0] = r.getX();
		this.m[0][1] = r.getY();
		this.m[0][2] = r.getZ();
		
		this.m[1][0] = u.getX();
		this.m[1][1] = u.getY();
		this.m[1][2] = u.getZ();
		
		this.m[2][0] = f.getX();
		this.m[2][1] = f.getY();
		this.m[2][2] = f.getZ();
		
		return this;
	}
	
	public Vector3f transform(Vector3f r)
	{
		return new Vector3f
		(
			m[0][0] * r.getX() + m[0][1] * r.getY() + m[0][2] * r.getZ() + m[0][3],
			m[1][0] * r.getX() + m[1][1] * r.getY() + m[1][2] * r.getZ() + m[1][3],
			m[2][0] * r.getX() + m[2][1] * r.getY() + m[2][2] * r.getZ() + m[2][3]
		);
	}
	public Vector2f transform(Vector2f r)
	{
		return new Vector2f
		(
			m[0][0] * r.getX() + m[0][1] * r.getY() + m[0][3],
			m[1][0] * r.getX() + m[1][1] * r.getY() + m[1][3]
		);
	}
	
	//multiply matrix with matrix
	public Matrix4f multiply(Matrix4f r)
	{
		Matrix4f result = new Matrix4f();
		for(int x = 0; x < 4; x++)
		{
			for(int y = 0; y < 4; y++)
			{
				float value = 0;
				for(int i = 0; i < 4; i++)
				{
					value += this.m[x][i] * r.getValue(i, y);
				}
				result.setValue(x, y, value);
			}
		}
		return result;
	}

}
