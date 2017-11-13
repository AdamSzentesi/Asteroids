#version 150

in vec2 position;
in vec3 color;

out Data
{
	vec4 position;
	vec3 color;
} outData;

uniform mat3 MV;

void main()
{

	mat3 modelView = MV;
	outData.position = vec4(MV * vec3(position, 3.0), 1.0);
	outData.color = color;
}