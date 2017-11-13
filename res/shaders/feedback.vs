#version 150

//input each vertex position
in vec3 position;
in vec3 normal;

//input constant ModelView matrix
uniform mat4 MV;
uniform mat4 P;

//output transformed coordinates
out Data
{
	vec4 position;
} outData;

void main()
{
	mat4 projectionView = P;
	mat4 modelView = MV;
	outData.position = MV * vec4(position, 1.0);
}