package com.soarclient.libraries.sodium.client.gl.tessellation;

public enum GlPrimitiveType {
	LINES(1), TRIANGLES(4), QUADS(7);

	private final int id;

	private GlPrimitiveType(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}
}
