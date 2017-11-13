#version 150

layout(triangles) in;
layout(triangle_strip, max_vertices=3) out;

in Data
{
	vec3 color;
} inData[];

out Data
{
	vec3 color;
} outData;

void main()
{
	
	for(int i = 0; i < 3; i++)
	{
		gl_Position = gl_in[i].gl_Position;
		outData.color = inData[i].color;
		EmitVertex();
	}
	EndPrimitive();
}