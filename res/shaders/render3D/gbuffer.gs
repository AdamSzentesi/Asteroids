#version 150

layout(triangles) in;
layout(triangle_strip, max_vertices = 39) out;

in Data
{
	vec2 tex;
	vec3 color;
	vec3 normal;
	mat3 tbn;
	vec3 worldPosition;
	vec3 ridge;
} inData[];

out Data
{
	vec2 tex;
	vec3 color;
	vec3 normal;
	mat3 tbn;
	vec3 worldPosition;
	float emission;
} outData;

uniform mat4 V;
uniform mat4 P;

void main()
{
	//basic geometry
	for(int i = 0; i < 3; i++)
	{
		outData.tex = inData[i].tex;
		outData.color = inData[i].color;
		outData.normal = inData[i].normal;
		outData.tbn = inData[i].tbn;
		vec4 wPosition = vec4(inData[i].worldPosition, 1.0);
		outData.worldPosition = wPosition.xyz;
		outData.emission = 0;
		gl_Position = P * V * wPosition;
		EmitVertex();
	}
	EndPrimitive();
}