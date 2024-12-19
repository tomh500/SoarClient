package com.soarclient.libraries.sodium.client.model.quad.properties;

public enum ModelQuadOrientation {
	NORMAL(new int[] { 0, 1, 2, 3 }), FLIP(new int[] { 1, 2, 3, 0 });

	private final int[] indices;

	private ModelQuadOrientation(int[] indices) {
		this.indices = indices;
	}

	public int getVertexIndex(int idx) {
		return this.indices[idx];
	}

	public static ModelQuadOrientation orient(float[] brightnesses) {
		return brightnesses[0] + brightnesses[2] > brightnesses[1] + brightnesses[3] ? NORMAL : FLIP;
	}
}
