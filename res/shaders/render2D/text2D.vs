#version 150

in vec2 position;
in vec2 tex;
in vec3 color;

out Data
{
	vec2 tex;
	vec3 color;
} outData;

uniform mat4 M;
uniform mat4 V;
uniform mat4 P;

void main()
{
	gl_Position = P * V * M * vec4(position, 0.0, 1.0);
	outData.color = color;
	outData.tex = tex;
}