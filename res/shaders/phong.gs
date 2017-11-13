#version 150

layout(triangles) in;
layout(triangle_strip, max_vertices=3) out;

in Data
{
	vec3 color;
	vec3 normal;
	vec3 worldPosition;
} inData[];

out Data
{
	vec3 color;
	vec3 normal;
	vec3 worldPosition;
} outData;

void main()
{
	for(int i = 0; i < 3; i++)
	{
		gl_Position = gl_in[i].gl_Position;
		outData.color = inData[i].color;
		outData.normal = inData[i].normal;
		outData.worldPosition = inData[i].worldPosition;
		EmitVertex();
	}
	EndPrimitive();
}