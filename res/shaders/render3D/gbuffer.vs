#version 150

in vec3 position;
in vec2 tex;
in vec3 normal;
in vec3 tangent;
in vec3 ridge;

in vec3 color;

out Data
{
	vec2 tex;
	vec3 color;
	vec3 normal;
	mat3 tbn;
	vec3 worldPosition;
	vec3 ridge;
} outData;

uniform mat4 M;

void main()
{
	outData.tex = tex;
	outData.color = color;

	vec3 n = normalize(M * vec4(normal, 0.0)).xyz;
	vec3 t = normalize(M * vec4(tangent, 0.0)).xyz;
	t = normalize(t - dot(t, n) * n);	
	vec3 b = normalize(cross(t, n));

	outData.tbn = mat3(t, b, n);
	outData.normal = n;
	outData.ridge = normalize(M * vec4(ridge, 0.0)).xyz;
	outData.worldPosition = (M * vec4(position, 1.0)).xyz;
}