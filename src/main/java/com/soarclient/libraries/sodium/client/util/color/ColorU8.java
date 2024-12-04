package com.soarclient.libraries.sodium.client.util.color;

public interface ColorU8 {
	float COMPONENT_RANGE = 255.0F;
	float NORM = 0.003921569F;

	static float normalize(float v) {
		return v * 0.003921569F;
	}
}
