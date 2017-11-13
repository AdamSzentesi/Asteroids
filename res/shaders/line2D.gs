#version 150

layout(lines) in;
layout(line_strip, max_vertices = 6) out;

in Data
{
	vec4 position;
	vec3 color;
} inData[];

out Data
{
	vec3 color;
} outData;

uniform mat4 P;

void main()
{
	mat4 proj = P;

	for(int i = 0; i < 2; i++)
	{
		gl_Position = P * inData[i].position;
		outData.color = inData[i].color;
		EmitVertex();
	}
EndPrimitive();

}