#version 150

in vec2 position;
in vec3 color;

out Data
{
	vec3 color;
} outData;

uniform mat4 M;
uniform mat4 V;
uniform mat4 P;

void main()
{
	//mat3 modelMatrix = M;
	//mat3 projectionMatrix = P;

	vec4 worldPosition = M * vec4(position, 0.0, 1.0);
	gl_Position = P * V * worldPosition;
	outData.color = color;
}