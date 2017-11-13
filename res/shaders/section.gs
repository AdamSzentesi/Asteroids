#version 150

layout(lines) in;
layout(line_strip, max_vertices = 6) out;

in Data
{
	vec4 position;
	vec2 normal;
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

	vec4 line1 = P * inData[0].position;
	vec4 line2 = P * inData[1].position;

	float nx = line2.x - line1.x;
	float ny = line2.y - line1.y;
	//vec2 norm = normalize(vec2(ny, -nx));
	vec2 norm = inData[0].normal;

	gl_Position = line1;
	outData.color = inData[0].color;
	EmitVertex();

	gl_Position = line2;
	outData.color = inData[1].color;
	EmitVertex();

	EndPrimitive();

/*
	gl_Position = line2 + vec4(norm.x, norm.y, 0, 0) * -0.02;
	outData.color = inData[0].color * vec3(0.6, 0.6, 0.6);
	EmitVertex();

	gl_Position = line1 + vec4(norm.x, norm.y, 0, 0) * -0.02;
	outData.color = inData[1].color * vec3(0.6, 0.6, 0.6);
	EmitVertex();

	EndPrimitive();

	gl_Position = line2 + vec4(norm.x, norm.y, 0, 0) * -0.04;
	outData.color = inData[0].color * vec3(0.4, 0.4, 0.4);
	EmitVertex();

	gl_Position = line1 + vec4(norm.x, norm.y, 0, 0) * -0.04;
	outData.color = inData[1].color * vec3(0.4, 0.4, 0.4);
	EmitVertex();

	EndPrimitive();
*/
}