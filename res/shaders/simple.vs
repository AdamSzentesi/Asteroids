#version 150

in vec3 position;
in vec3 normal;
in vec3 color;

out Data
{
	vec3 color;
} outData;

uniform mat4 MV;
uniform mat4 P;

void main()
{
	mat4 proj = P;
	vec3 norm = normal;
	gl_Position = proj * MV * vec4(position, 1.0);
	outData.color = color;
}