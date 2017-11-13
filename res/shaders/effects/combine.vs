#version 150

in vec2 position;
in vec2 tex;

out Data
{
	vec2 tex;
} outData;

void main()
{
	outData.tex = tex;
	gl_Position = vec4(position, 0.0, 1.0);
}