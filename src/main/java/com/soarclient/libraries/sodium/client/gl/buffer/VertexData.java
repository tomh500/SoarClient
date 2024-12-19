package com.soarclient.libraries.sodium.client.gl.buffer;

import java.nio.ByteBuffer;

import com.soarclient.libraries.sodium.client.gl.attribute.GlVertexFormat;

public class VertexData {
	public final GlVertexFormat<?> format;
	public final ByteBuffer buffer;

	public VertexData(ByteBuffer buffer, GlVertexFormat<?> format) {
		this.format = format;
		this.buffer = buffer;
	}
}
