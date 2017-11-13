package asteroids.components.Geometry3D;

import asteroids.components.Component;
import asteroids.math.Vector3f;

public class PointLightComponent extends Component
{
	public LightComponent lightComponent;
	public Vector3f attenuation;
	public float range;
	
	public PointLightComponent()
	{
		this.lightComponent = new LightComponent();
		this.attenuation = new Vector3f();
		setAttenuation(new Vector3f(0.0f, 0.0f, 1.0f));
	}

	public void setup(Vector3f color, float intensity, Vector3f attenuation)
	{
		this.lightComponent.setup(color, intensity);
		this.setAttenuation(attenuation);
	}
	
	private void setAttenuation(Vector3f attenuation)
	{
		this.attenuation.set(attenuation);
		calculateRange();
	}
	
	private void calculateRange()
	{
		float minLight = 256 / 5;
		float c = this.attenuation.x - minLight * this.lightComponent.getIntensity() * this.lightComponent.getColor().max();
		float l = this.attenuation.y;
		float q = this.attenuation.z;
		this.range = (float)((-l + Math.sqrt(l * l - 4 * q * c))/(2 * q));
	}

}
