#version 150

layout(triangles) in;
layout(lines, max_vertices=1) out;

in Data
{
	vec3 color;
} inData[];

out Data
{
	vec3 color;
} outData;

bool isIntersectingZero(float a, float b)
{
	
	bool result = true;
	return(result);
}

void main()
{
	if(isIntersectingZero(gl_in[0].gl_Position.z, gl_in[1].gl_Position.z))
	{
		gl_Position = gl_in[0].gl_Position;
		outData.color = inData[0].color;
		EmitVertex();

		gl_Position = gl_in[1].gl_Position;
		outData.color = inData[1].color;
		EmitVertex();	
	}
EndPrimitive();
	if(isIntersectingZero(gl_in[1].gl_Position.z, gl_in[2].gl_Position.z))
	{
		gl_Position = gl_in[1].gl_Position;
		outData.color = inData[1].color;
		EmitVertex();

		gl_Position = gl_in[2].gl_Position;
		outData.color = inData[2].color;
		EmitVertex();	
	}
EndPrimitive();
	if(isIntersectingZero(gl_in[2].gl_Position.z, gl_in[0].gl_Position.z))
	{
		gl_Position = gl_in[2].gl_Position;
		outData.color = inData[2].color;
		EmitVertex();

		gl_Position = gl_in[0].gl_Position;
		outData.color = inData[0].color;
		EmitVertex();	
	}

	EndPrimitive();
}