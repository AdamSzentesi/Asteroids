package asteroids.components.Geometry3D.Render3DMesh;

import asteroids.math.Vector3f;

public class Material
{
	private static Texture defaultTexture = new Texture(0);
	//diffuse
	public Vector3f color;
	public Texture diffuseTexture;
	//specular
	public float specularIntensity;
	public float specularExponent;
	public Texture specularTexture;
	//normal
	public Texture normalTexture;
	//emission
	public Texture emissionTexture;
	
	public Material()
	{
		this.color = new Vector3f(1.0f, 0.0f, 1.0f);
		this.diffuseTexture = defaultTexture;
		this.specularTexture = defaultTexture;
		this.normalTexture = defaultTexture;
		this.emissionTexture = defaultTexture;
	}
	
	public void loadDiffuseTexture(String fileName)
	{
		this.diffuseTexture = new Texture(fileName);
	}
	
	public void loadSpecularTexture(String fileName)
	{
		this.specularTexture = new Texture(fileName);
	}
	
	public void loadNormalTexture(String fileName)
	{
		this.normalTexture = new Texture(fileName);
	}
	
	public void loadEmissionTexture(String fileName)
	{
		this.emissionTexture = new Texture(fileName);
	}
}
