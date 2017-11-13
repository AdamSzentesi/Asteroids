#version 150

in vec3 position;
in vec3 normal;
in vec3 color;

out Data
{
	vec3 color;
	vec3 normal;
	vec3 worldPosition;
} outData;

uniform mat4 MV;
uniform mat4 P;

void main()
{
	mat4 proj = P;
	gl_Position = proj * MV * vec4(position, 1.0);
	outData.color = color;
	outData.normal = normal;
	outData.worldPosition = position;
}