package com.soarclient.libs.sodium.client.model.vertex.buffer;

import org.lwjgl.system.MemoryUtil;

import com.soarclient.libs.sodium.client.model.vertex.type.BufferVertexType;

public abstract class VertexBufferWriterUnsafe extends VertexBufferWriter {
	protected long writePointer;

	protected VertexBufferWriterUnsafe(VertexBufferView backingBuffer, BufferVertexType<?> vertexType) {
		super(backingBuffer, vertexType);
	}

	@Override
	protected void onBufferStorageChanged() {
		this.writePointer = MemoryUtil.memAddress(this.backingBuffer.oldium$getDirectBuffer(), this.backingBuffer.oldium$getWriterPosition());
	}

	@Override
	protected void advance() {
		this.writePointer = this.writePointer + (long)this.vertexStride;
		super.advance();
	}
}
