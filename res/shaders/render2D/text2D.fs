#version 150

in Data
{
	vec2 tex;
	vec3 color;
} inData;

//buffer output
out vec4 outDiffuse;

//texture resources
uniform sampler2D diffuseSampler;

void main()
{
	vec4 diffuse = vec4(inData.color, 1.0);

	//diffuse
	vec4 diffuseTexture = texture2D(diffuseSampler, inData.tex.xy);
	if(diffuseTexture != vec4(0, 0, 0, 0))
	{
		diffuse = diffuseTexture;
	}

  outDiffuse = diffuse;// * (sin(gl_FragCoord.y*20) + 3) / 4;

}