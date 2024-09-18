package com.soarclient.libs.sodium.client.gl.attribute;

public class GlVertexAttributeFormat {
	public static final GlVertexAttributeFormat FLOAT = new GlVertexAttributeFormat(5126, 4);
	public static final GlVertexAttributeFormat UNSIGNED_SHORT = new GlVertexAttributeFormat(5123, 2);
	public static final GlVertexAttributeFormat UNSIGNED_BYTE = new GlVertexAttributeFormat(5121, 1);
	private final int glId;
	private final int size;

	private GlVertexAttributeFormat(int glId, int size) {
		this.glId = glId;
		this.size = size;
	}

	public int getSize() {
		return this.size;
	}

	public int getGlFormat() {
		return this.glId;
	}
}
