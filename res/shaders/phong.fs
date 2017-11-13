#version 150

in Data
{
	vec3 color;
	vec3 normal;
	vec3 worldPosition;
} inData;

vec3 lightPosition = vec3(2, -2, 2);

//calculates the lighten color
vec4 calcLight(vec3 normal, vec3 lightPosition, vec3 worldPosition)
{
	vec4 baseColor = vec4(1.0, 1.0, 1.0, 1.0);
	vec4 diffuseColor = vec4(0, 0, 0, 0);
	
	vec3 lightDirection = worldPosition - lightPosition;

	float diffuseFactor = max(dot(normalize(normal), normalize(lightDirection)), 0.0);
	
	diffuseColor = baseColor * 1.0 * diffuseFactor;

	return (diffuseColor);
}

void main (void)  
{
	gl_FragColor = calcLight(inData.normal, lightPosition, inData.worldPosition);
}   