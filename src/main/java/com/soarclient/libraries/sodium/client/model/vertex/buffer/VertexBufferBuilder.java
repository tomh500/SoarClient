package com.soarclient.libraries.sodium.client.model.vertex.buffer;

import java.nio.ByteBuffer;

import com.soarclient.libraries.sodium.client.gl.attribute.BufferVertexFormat;

import net.minecraft.client.renderer.GLAllocation;

public class VertexBufferBuilder implements VertexBufferView {
	private final BufferVertexFormat vertexFormat;
	private ByteBuffer buffer;
	private int writerOffset;
	private int capacity;

	public VertexBufferBuilder(BufferVertexFormat vertexFormat, int initialCapacity) {
		this.vertexFormat = vertexFormat;
		this.buffer = GLAllocation.createDirectByteBuffer(initialCapacity);
		this.capacity = initialCapacity;
		this.writerOffset = 0;
	}

	private void grow(int len) {
		int cap = Math.max(this.capacity * 2, this.capacity + len);
		ByteBuffer buffer = GLAllocation.createDirectByteBuffer(cap);
		buffer.put(this.buffer);
		buffer.position(0);
		this.buffer = buffer;
		this.capacity = cap;
	}

	@Override
	public boolean oldium$ensureBufferCapacity(int bytes) {
		if (this.writerOffset + bytes <= this.capacity) {
			return false;
		} else {
			this.grow(bytes);
			return true;
		}
	}

	@Override
	public ByteBuffer oldium$getDirectBuffer() {
		return this.buffer;
	}

	@Override
	public int oldium$getWriterPosition() {
		return this.writerOffset;
	}

	@Override
	public void oldium$flush(int vertexCount, BufferVertexFormat format) {
		if (this.vertexFormat != format) {
			throw new IllegalStateException("Mis-matched vertex format (expected: [" + format + "], currently using: ["
					+ this.vertexFormat + "])");
		} else {
			this.writerOffset = this.writerOffset + vertexCount * format.getStride();
		}
	}

	@Override
	public BufferVertexFormat oldium$getVertexFormat() {
		return this.vertexFormat;
	}

	public boolean isEmpty() {
		return this.writerOffset == 0;
	}

	public int getSize() {
		return this.writerOffset;
	}

	public void copyInto(ByteBuffer dst) {
		this.buffer.position(0);
		this.buffer.limit(this.writerOffset);
		dst.put(this.buffer.slice());
		this.buffer.clear();
		this.writerOffset = 0;
	}
}
