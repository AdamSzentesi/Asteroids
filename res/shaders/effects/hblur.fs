#version 150

in Data
{
	vec2 tex;
	vec2 blurCoordinates[11];
} inData;

uniform sampler2D diffuseSampler;

void main (void)  
{
	//0.0093	0.028002	0.065984	0.121703	0.175713	0.198596	0.175713	0.121703	0.065984	0.028002	0.0093

	vec4 textureColor = texture2D(diffuseSampler, inData.blurCoordinates[0]) * 0.0093;
	textureColor += texture2D(diffuseSampler, inData.blurCoordinates[1]) * 0.028002;
	textureColor += texture2D(diffuseSampler, inData.blurCoordinates[2]) * 0.065984;
	textureColor += texture2D(diffuseSampler, inData.blurCoordinates[3]) * 0.121703;
	textureColor += texture2D(diffuseSampler, inData.blurCoordinates[4]) * 0.175713;
	textureColor += texture2D(diffuseSampler, inData.blurCoordinates[5]) * 0.198596;
	textureColor += texture2D(diffuseSampler, inData.blurCoordinates[6]) * 0.175713;
	textureColor += texture2D(diffuseSampler, inData.blurCoordinates[7]) * 0.121703;
	textureColor += texture2D(diffuseSampler, inData.blurCoordinates[8]) * 0.065984;
	textureColor += texture2D(diffuseSampler, inData.blurCoordinates[9]) * 0.028002;
	textureColor += texture2D(diffuseSampler, inData.blurCoordinates[10]) * 0.0093;

	gl_FragColor = textureColor;
}   