#version 150

//INPUTS------------------------------------------------

layout(triangles) in;
layout(line_strip, max_vertices = 4) out;

//input data from vertex shader
in Data
{
	vec4 position;
} inData[];

//output feedback data: section coordinates
out vec2 transformedPosition;

//METHODS------------------------------------------------

//is 0 in numbers between (a, b)?
bool isIntersectingZero(float a, float b)
{
	bool result = false;
	if(sign(a) != sign(b))
	{
		result = true;
	}
	return(result);
}

//returns intersection coordinates between segment and z=0 plane;
vec4 getIntersection(vec4 a, vec4 b)
{
	float x = a.x;
	float y = 0;

	if(a.z != 0 || b.z != 0)
	{
		x = a.x - ((b.x - a.x) / (b.z - a.z)) * a.z;
		y = a.y - ((b.y - a.y) / (b.z - a.z)) * a.z;
	}

	vec4 result = vec4(x, y, 3, 1);

	return(result);
}
//emit a 2D vertex
void emit(vec4 t1, vec4 t2, vec2 n)
{
	transformedPosition = vec2(t1.x, t1.y);
	EmitVertex();
	transformedPosition = n;
	EmitVertex();
	EndPrimitive();

	transformedPosition = vec2(t2.x, t2.y);
	EmitVertex();
	transformedPosition = n;
	EmitVertex();
	EndPrimitive();
	
}

uniform mat4 MV;
uniform mat4 P;

//MAIN------------------------------------------------

void main()
{
	//triangle segment sets
	int setA[3] = int[](0, 1, 2);
	int setB[3] = int[](1, 2, 0);

	//binary counter (result is between 0 and 7 depending on variation of events)
	int count = 0;

	//list through all of the triangle segments
	for(int i = 0; i < 3; i++)
	{
		if(isIntersectingZero(inData[setA[i]].position.z, inData[setB[i]].position.z))
		{
			count = count + int(exp2(i));
		}
	}

	vec4 t1;
	vec4 t2;

	vec4 s0 = inData[1].position - inData[0].position;
	vec4 s1 = inData[2].position - inData[1].position;
	vec3 faceNormal = cross(vec3(s0), vec3(s1));
	vec4 faceNormal4 = vec4(faceNormal, 1);
	vec2 n = normalize(vec2(faceNormal4));

	//count is 3 = 1 + 2 = 2^0 + 2^1 -> set 0 and 1 are intersectiong with z=0
	if(count == 3)
	{
		if(inData[1].position.z != 0)
		{
			t1 = getIntersection(inData[setA[0]].position, inData[setB[0]].position);
			t2 = getIntersection(inData[setA[1]].position, inData[setB[1]].position);
			emit(t1, t2, n);
		}
	}

	//count is 5 = 1 + 4 = 2^0 + 2^2 -> set 0 and 2 are intersectiong with z=0
	if(count == 5)
	{
		if(inData[0].position.z != 0)
		{
			t1 = getIntersection(inData[setA[0]].position, inData[setB[0]].position);
			t2 = getIntersection(inData[setA[2]].position, inData[setB[2]].position);
			emit(t1, t2, n);
		}
	}

	//count is 6 = 2 + 4 = 2^1 + 2^2 -> set 1 and 2 are intersectiong with z=0
	if(count == 6)
	{
		if(inData[2].position.z != 0)
		{
			t1 = getIntersection(inData[setA[1]].position, inData[setB[1]].position);	
			t2 = getIntersection(inData[setA[2]].position, inData[setB[2]].position);
			emit(t1, t2, n);
		}
	}

	//count is 7 = 1 + 2 + 4 = 2^0 + 2^1 + 2^2 -> set 0 and 1 and 2 are intersectiong with z=0

	if(count == 7)
	{
		if(inData[0].position.z == 0)
		{
			t1 = vec4 (inData[0].position.x, inData[0].position.y, 3, 1);
			t2 = getIntersection(inData[setA[1]].position, inData[setB[1]].position);
			emit(t1, t2, n);
		}
		if(inData[1].position.z == 0)
		{
			t1 = vec4 (inData[1].position.x, inData[1].position.y, 3, 1);
			t2 = getIntersection(inData[setA[2]].position, inData[setB[2]].position);
			emit(t1, t2, n);
		}
		if(inData[2].position.z == 0)
		{
			t1 = vec4 (inData[2].position.x, inData[2].position.y, 3, 1);
			t2 = getIntersection(inData[setA[0]].position, inData[setB[0]].position);
			emit(t1, t2, n);
		}
	}

}