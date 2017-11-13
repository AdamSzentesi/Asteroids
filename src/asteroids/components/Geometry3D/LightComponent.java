package asteroids.components.Geometry3D;

import asteroids.math.Vector3f;

public class LightComponent
{
	public Vector3f color;
	public float intensity;
	
	public LightComponent()
	{
		this.color = new Vector3f(1.0f, 1.0f, 1.0f);
		this.intensity = 1.0f;
	}
	
	public void setup(Vector3f color, float intensity)
	{
		this.color.set(color);
		this.intensity = intensity;
	}
	
	public Vector3f getColor()
	{
		return this.color;
	}
	
	public float getIntensity()
	{
		return this.intensity;
	}
}
