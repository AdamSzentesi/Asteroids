package asteroids.math;

public class Matrix3f
{
	private float[][] m;
	
	public Matrix3f()
	{
		m = new float[3][3];
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
	
	public float[][] getM()
	{
		float[][] res = new float[3][3];
		
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
				res[i][j] = m[i][j];
		
		return res;
	}
	
	//initiate as identity matrix
	public Matrix3f initIdentity()
	{
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
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
	public Matrix3f initTranslation(float x, float y)
	{
		//cast idetity to matrix
		this.initIdentity();
		
		//set translation to matrix
		this.m[0][2] = x;
		this.m[1][2] = y;
		this.m[2][2] = 1;
		
		return this;
	}
	public Matrix3f initTranslation(Vector2f translation)
	{
		this.initTranslation(translation.x, translation.y);
		return this;
	}
	
	//initiate as rotation matrix
	public Matrix3f initRotation(float z)
	{
		//rotation matrix
		Matrix3f rotZ = new Matrix3f();
		rotZ.initIdentity();
		//rotation angle
		z = (float)Math.toRadians(z);
		//set rotation matrix
		rotZ.m[0][0] = (float)Math.cos(z); 
		rotZ.m[0][1] = -(float)Math.sin(z);
		rotZ.m[1][0] = (float)Math.sin(z);
		rotZ.m[1][1] = (float)Math.cos(z);
		//multiply all the rotation matrices
		m = rotZ.getM();
		return this;
	}
	
	//initiate as scale matrix
	public Matrix3f initScale(float x, float y)
	{
		//cast idetity to matrix
		this.initIdentity();
		
		//set translation to matrix
		this.m[0][0] = x;
		this.m[1][1] = y;
		
		return this;
	}
	public Matrix3f initScale(Vector2f scale)
	{
		this.initScale(scale.x, scale.y);
		return this;
	}
	
	public Vector2f transform(Vector2f r)
	{
		return new Vector2f
		(
			m[0][0] * r.getX() + m[0][1] * r.getY() + m[0][2],
			m[1][0] * r.getX() + m[1][1] * r.getY() + m[1][2]
		);
	}
	
	//multiply matrix with matrix
	public Matrix3f multiply(Matrix3f r)
	{
		Matrix3f result = new Matrix3f();
		for(int x = 0; x < 3; x++)
		{
			for(int y = 0; y < 3; y++)
			{
				float value = 0;
				for(int i = 0; i < 3; i++)
				{
					value += this.m[x][i] * r.getValue(i, y);
				}
				result.setValue(x, y, value);
			}
		}
		return result;
	}
}
