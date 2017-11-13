#version 150

layout(triangles) in;
layout(line_strip, max_vertices=6) out;

//input vertex data
in Data
{
	vec3 color;
} inData[];

//output vertex data
out Data
{
	vec3 color;
} outData;

//projection matrix uniform
uniform mat4 P;

//is 0 in numbers between (a, b)?
bool isIntersectingZero(float a, float b)
{
	bool result = false;
	if((a <= 0 && b >= 0) || (b <= 0 && a >= 0))
	{
		result = true;
	}
	return(result);
}

//emit vertex at specified position
void emit(vec4 position)
{
	//adjust position by projection matrix
	gl_Position = P * position;
	outData.color = inData[0].color;
	EmitVertex();
}

//returns intersection coordinates between segment and z=0 plane;
vec4 getIntersection(vec4 a, vec4 b)
{
	float x = a.x - ((b.x - a.x) / (b.z - a.z)) * a.z;
	float y = a.y - ((b.y - a.y) / (b.z - a.z)) * a.z;
	vec4 result = vec4(x, y, 1, 1);
	return(result);
}

void main()
{
	mat4 proj = P;

	//triangle segment sets
	int setA[3] = int[](0, 1, 2);
	int setB[3] = int[](1, 2, 0);
	
	//binary counter (result is between 0 and 7 depending on variation of events)
	int count = 0;

	//copy vertex positions to writable vectors (gl_Position is readonly)
	vec4 vertices[3];
	vertices[0] = gl_in[0].gl_Position;
	vertices[1] = gl_in[1].gl_Position;
	vertices[2] = gl_in[2].gl_Position;

	//list through all of the triangle segments
	for(int i = 0; i < 3; i++)
	{
		if(isIntersectingZero(vertices[setA[i]].z, vertices[setB[i]].z))
		{
			count = count + int(exp2(i)); 
		}
	}

	//count is 3 = 1 + 2 = 2^0 + 2^1 -> set 0 and 1 are intersectiong with z=0
	if(count == 3)
	{
		vec4 a = getIntersection(vertices[setA[0]], vertices[setB[0]]);
		vec4 b = getIntersection(vertices[setA[1]], vertices[setB[1]]);
		emit(a);
		emit(b);
	}

	//count is 5 = 1 + 4 = 2^0 + 2^2 -> set 0 and 2 are intersectiong with z=0
	if(count == 5)
	{
		vec4 a = getIntersection(vertices[setA[0]], vertices[setB[0]]);
		vec4 b = getIntersection(vertices[setA[2]], vertices[setB[2]]);
		emit(a);
		emit(b);
	}

	//count is 6 = 2 + 4 = 2^1 + 2^2 -> set 1 and 2 are intersectiong with z=0
	if(count == 6)
	{
		vec4 a = getIntersection(vertices[setA[1]], vertices[setB[1]]);
		vec4 b = getIntersection(vertices[setA[2]], vertices[setB[2]]);
		emit(a);
		emit(b);
	}

EndPrimitive();
}