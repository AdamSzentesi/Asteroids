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
uniform vec2 character;

void main()
{
	vec2 char = character;
	vec4 diffuse = vec4(inData.color, 1.0);

	//diffuse
	vec4 diffuseTexture = texture2D(diffuseSampler, inData.tex.xy + (character * 0.0625));
	if(diffuseTexture != vec4(0, 0, 0, 0))
	{
		diffuse = diffuseTexture;
	}

	if(diffuse == vec4(1, 1, 1, 1))
	{
		discard;
	}

  outDiffuse = vec4(inData.color, 1.0);// * (sin(gl_FragCoord.y*20) + 3) / 4;

}