package com.soarclient.libraries.sodium.client.model.vertex.buffer;

import java.nio.ByteBuffer;

import com.soarclient.libraries.sodium.client.gl.attribute.BufferVertexFormat;

public interface VertexBufferView {
	boolean oldium$ensureBufferCapacity(int integer);

	ByteBuffer oldium$getDirectBuffer();

	int oldium$getWriterPosition();

	void oldium$flush(int integer, BufferVertexFormat bufferVertexFormat);

	BufferVertexFormat oldium$getVertexFormat();
}
