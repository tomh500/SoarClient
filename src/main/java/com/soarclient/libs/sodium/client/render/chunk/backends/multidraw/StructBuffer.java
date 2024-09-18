package com.soarclient.libs.sodium.client.render.chunk.backends.multidraw;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

public abstract class StructBuffer {
	protected ByteBuffer buffer;
	protected final int stride;

	protected StructBuffer(int bytes, int stride) {
		this.buffer = BufferUtils.createByteBuffer(bytes * stride);
		this.stride = stride;
	}

	public ByteBuffer getBuffer() {
		return this.buffer;
	}

	public void delete() {
	}

	public long getBufferAddress() {
		return MemoryUtil.memAddress(this.buffer);
	}
}
