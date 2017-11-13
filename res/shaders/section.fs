#version 150

in Data
{
	vec3 color;
} inData;

out vec4 fragmentColor;

void main()
{
  fragmentColor = vec4(inData.color, 1);
}