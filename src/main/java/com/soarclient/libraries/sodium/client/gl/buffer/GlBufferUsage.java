package com.soarclient.libraries.sodium.client.gl.buffer;

public enum GlBufferUsage {
	GL_STREAM_DRAW(35040),
	GL_STREAM_READ(35041),
	GL_STREAM_COPY(35042),
	GL_STATIC_DRAW(35044),
	GL_STATIC_READ(35045),
	GL_STATIC_COPY(35046),
	GL_DYNAMIC_DRAW(35048),
	GL_DYNAMIC_READ(35049),
	GL_DYNAMIC_COPY(35050);

	private final int id;

	private GlBufferUsage(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}
}
