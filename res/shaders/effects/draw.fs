#version 150

in Data
{
	vec2 tex;
} inData;

uniform sampler2D diffuseSampler;



void main (void)  
{
	vec3 diffuse = texture2D(diffuseSampler, inData.tex).rgb;
	gl_FragColor = vec4(diffuse, 1);
}   