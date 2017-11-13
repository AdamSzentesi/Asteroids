#version 150

in Data
{
	vec3 color;
} inData;

//out vec4 fragmentColor;
out vec4 outDiffuse;

void main()
{
  outDiffuse = vec4(inData.color, 1);// * (sin(gl_FragCoord.y*2) + 3) / 4;

}