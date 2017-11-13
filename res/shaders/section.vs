#version 150

in vec2 position;
in vec2 normal;
in vec3 color;

out Data
{
	vec4 position;
	vec2 normal;
	vec3 color;
} outData;

uniform mat4 M;

void main()
{
	mat4 modelView = M;
	outData.position = M * vec4(position, 3.0, 1.0);
	outData.normal = normal;
	outData.color = color;
}