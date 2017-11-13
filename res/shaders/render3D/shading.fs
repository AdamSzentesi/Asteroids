#version 150

in Data
{
	vec2 tex;
} inData;

uniform vec3 viewPosition;
uniform sampler2D diffuseSampler;
uniform sampler2D normalSampler;
uniform sampler2D specularSampler;
uniform sampler2D emissionSampler;
uniform sampler2D positionSampler;

struct BaseLight
{
  vec3 color;
  float intensity;
};

struct Attenuation
{
  float constant;
  float linear;
  float exponential;
};

struct PointLight
{
  BaseLight base;
  Attenuation attenuation;
  vec3 position;
  float range;
};

uniform int numberOfLights;
uniform PointLight pointLights[100];

//TMP: should be unifrom
float specularExponent = 32;
//TMP: should be unifrom

void main (void)  
{
	vec3 diffuse = texture2D(diffuseSampler, inData.tex).rgb;
	vec3 normal = normalize(texture2D(normalSampler, inData.tex).rgb);
	vec3 specular = texture2D(specularSampler, inData.tex).rgb;
	vec3 emission = texture2D(emissionSampler, inData.tex).rgb;
	vec3 position = texture2D(positionSampler, inData.tex).rgb;

	vec3 totalLight;

	//phong shading
	for(int i = 0; i < numberOfLights; i++)
	{
		PointLight pointLight = pointLights[i];

		//diffuse light
		vec3 diffuseLight = vec3(0);
		vec3 lightDirection = pointLight.position - position;
		float diffuseFactor = max(dot(normal, normalize(lightDirection)), 0.0);
		if(diffuseFactor > 0.0)
		{
			diffuseLight = pointLight.base.color * diffuseFactor * pointLight.base.intensity;
		}
		//specular light
		vec3 specularLight = vec3(0);
		vec3 viewDirection = normalize(viewPosition - position);
		vec3 reflectionDirection = reflect(-normalize(lightDirection), -normal);
		float specularFactor = pow(max(dot(viewDirection, reflectionDirection), 0.0), specularExponent);
		if(specularFactor > 0.0)
		{
			specularLight = pointLight.base.color * specularFactor * specular * 10;
		}
		//attenuation
		float distanceToLight = length(lightDirection);
		float attenuation = pointLight.attenuation.constant + pointLight.attenuation.linear * distanceToLight + pointLight.attenuation.exponential * distanceToLight * distanceToLight + 0.0001;

		totalLight = totalLight + (diffuseLight + specularLight) / attenuation;
	}

	totalLight = totalLight + emission;

	gl_FragColor = vec4(totalLight * diffuse, 1);
	//gl_FragColor = vec4(position, 1);
}   