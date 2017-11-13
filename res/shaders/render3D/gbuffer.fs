#version 150

in Data
{
	vec2 tex;
	vec3 color;
	vec3 normal;
	mat3 tbn;
	vec3 worldPosition;
	float emission;
} inData;

//in vec4 gl_FragCoord;

out vec4 outDiffuse;
out vec4 outNormal;
out vec4 outSpecular;
out vec4 outEmission;
out vec4 outPosition;

uniform float specularIntensity;
uniform float specularExponent;

uniform sampler2D diffuseSampler;
uniform sampler2D normalSampler;
uniform sampler2D specularSampler;
uniform sampler2D emissionSampler;

float average(vec3 vector)
{
	return(vector.x + vector.y + vector.z) / 3;
}

void main (void)  
{
	vec4 diffuse = vec4(inData.color, 1.0);
	vec4 normal = vec4(inData.normal, 0.0);
	vec4 specular = vec4(0, 0, 0, 1);
	vec4 emission = vec4(1, 1, 1, 1);;

	if(inData.emission != 1)
	{
		float si = specularIntensity;
		float se = specularExponent;

		//diffuse
		vec4 diffuseTexture = texture2D(diffuseSampler, inData.tex.xy);
		if(diffuseTexture != vec4(0, 0, 0, 0))
		{
			diffuse = diffuseTexture;
		}

		//normal
		vec4 normalTexture = texture2D(normalSampler, inData.tex.xy);
		if(normalTexture != vec4(0, 0, 0, 0))
		{
			//normalTexture = vec4(inData.tbn * (normalTexture.xyz), 1.0);
			normalTexture = normalize(vec4(inData.tbn * (255.0/128.0 * normalTexture - 1.0).xyz, 1.0));
			normal = mix(normal, normalTexture, 0.3);
		}

		//specular
		vec4 specularTexture = texture2D(specularSampler, inData.tex.xy);
		if(specularTexture != vec4(0, 0, 0, 0))
		{
			float a = average(specularTexture.xyz);
			specular = vec4(a, a, a, 1);
		}

		//emission
		emission = texture2D(emissionSampler, inData.tex.xy);
	}

	//gl_FragColor = vec4(finalColor, 1.0);
	outDiffuse = diffuse;
	outNormal = normal;
	outSpecular = specular;
	outEmission = emission * diffuse;
	outPosition = vec4(inData.worldPosition, 1);
}   