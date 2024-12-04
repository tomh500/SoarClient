package com.soarclient.libraries.sodium.client.model.vertex.buffer;

import java.nio.ByteBuffer;

import com.soarclient.libraries.sodium.client.model.vertex.type.BufferVertexType;

public abstract class VertexBufferWriterNio extends VertexBufferWriter {
	protected ByteBuffer byteBuffer;
	protected int writeOffset;

	protected VertexBufferWriterNio(VertexBufferView backingBuffer, BufferVertexType<?> vertexType) {
		super(backingBuffer, vertexType);
	}

	@Override
	protected void onBufferStorageChanged() {
		this.byteBuffer = this.backingBuffer.oldium$getDirectBuffer();
		this.writeOffset = this.backingBuffer.oldium$getWriterPosition();
	}

	@Override
	protected void advance() {
		this.writeOffset = this.writeOffset + this.vertexStride;
		super.advance();
	}
}
