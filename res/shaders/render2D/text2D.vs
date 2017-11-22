#version 150

in vec2 position;
in vec2 tex;
in vec3 color;

out Data
{
	vec2 tex;
	vec3 color;
} outData;

uniform vec2 displayPosition;
uniform vec2 displaySize;

void main()
{
	vec2 size = displaySize;
	vec2 model = displayPosition;
	//gl_Position = P * V * M * vec4(position, 0.0, 1.0);
	gl_Position =  displayPosition + vec4(position * displaySize, 0.0, 1.0);
	outData.color = color;
	outData.tex = tex;
}