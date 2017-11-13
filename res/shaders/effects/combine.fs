#version 150

in Data
{
	vec2 tex;
} inData;

uniform sampler2D samplerA;
uniform float strengthA;
uniform sampler2D samplerB;
uniform float strengthB;

void main (void)  
{
	vec4 colorA = texture2D(samplerA, inData.tex);
	vec4 colorB = texture2D(samplerB, inData.tex);
	gl_FragColor = colorA * strengthA + colorB * strengthB;

}   