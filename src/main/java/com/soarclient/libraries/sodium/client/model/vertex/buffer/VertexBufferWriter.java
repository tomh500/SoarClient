package com.soarclient.libraries.sodium.client.model.vertex.buffer;

import com.soarclient.libraries.sodium.client.gl.attribute.BufferVertexFormat;
import com.soarclient.libraries.sodium.client.model.vertex.VertexSink;
import com.soarclient.libraries.sodium.client.model.vertex.type.BufferVertexType;

public abstract class VertexBufferWriter implements VertexSink {
	protected final VertexBufferView backingBuffer;
	protected final BufferVertexFormat vertexFormat;
	protected final int vertexStride;
	private int vertexCount;

	protected VertexBufferWriter(VertexBufferView backingBuffer, BufferVertexType<?> vertexType) {
		this.backingBuffer = backingBuffer;
		this.vertexFormat = vertexType.getBufferVertexFormat();
		this.vertexStride = this.vertexFormat.getStride();
		this.onBufferStorageChanged();
	}

	@Override
	public void ensureCapacity(int count) {
		if (this.backingBuffer.oldium$ensureBufferCapacity((this.vertexCount + count) * this.vertexStride)) {
			this.onBufferStorageChanged();
		}
	}

	@Override
	public void flush() {
		this.backingBuffer.oldium$flush(this.vertexCount, this.vertexFormat);
		this.vertexCount = 0;
	}

	protected void advance() {
		this.vertexCount++;
	}

	protected abstract void onBufferStorageChanged();
}
