#version 150

in vec2 position;
in vec2 tex;

out Data
{
	vec2 tex;
	vec2 blurCoordinates[11];
} outData;

uniform float targetWidth;

void main()
{
	outData.tex = tex;
	gl_Position = vec4(position, 0.0, 1.0);

	vec2 centerCoordinates = position * 0.5 + 0.5;
	float pixelSize = 1.0 / targetWidth;

	for(int i = -5; i < 5; i++)
	{
		outData.blurCoordinates[i + 5] = centerCoordinates + vec2(pixelSize * i, 0.0);
	}
}