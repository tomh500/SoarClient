package com.soarclient.libs.sodium.client.render.chunk.backends.multidraw;

import java.nio.ByteBuffer;

import org.lwjgl.system.MemoryUtil;

import com.soarclient.libs.sodium.SodiumClientMod;
import com.soarclient.libs.sodium.client.util.CompatMemoryUtil;

public abstract class ChunkDrawParamsVector extends StructBuffer {
	protected int capacity;
	protected int count;

	protected ChunkDrawParamsVector(int capacity) {
		super(capacity, 16);
		this.capacity = capacity;
	}

	public static ChunkDrawParamsVector create(int capacity) {
		return (ChunkDrawParamsVector)(SodiumClientMod.isDirectMemoryAccessEnabled()
			? new ChunkDrawParamsVector.UnsafeChunkDrawCallVector(capacity)
			: new ChunkDrawParamsVector.NioChunkDrawCallVector(capacity));
	}

	public abstract void pushChunkDrawParams(float float1, float float2, float float3);

	public void reset() {
		this.count = 0;
	}

	protected void growBuffer() {
		this.capacity *= 2;
		this.buffer = CompatMemoryUtil.memReallocDirect(this.buffer, this.capacity * this.stride);
	}

	public static class NioChunkDrawCallVector extends ChunkDrawParamsVector {
		private int writeOffset;

		public NioChunkDrawCallVector(int capacity) {
			super(capacity);
		}

		@Override
		public void pushChunkDrawParams(float x, float y, float z) {
			if (this.count++ >= this.capacity) {
				this.growBuffer();
			}

			ByteBuffer buf = this.buffer;
			buf.putFloat(this.writeOffset, x);
			buf.putFloat(this.writeOffset + 4, y);
			buf.putFloat(this.writeOffset + 8, z);
			this.writeOffset = this.writeOffset + this.stride;
		}

		@Override
		public void reset() {
			super.reset();
			this.writeOffset = 0;
		}
	}

	public static class UnsafeChunkDrawCallVector extends ChunkDrawParamsVector {
		private long basePointer = MemoryUtil.memAddress(this.buffer);
		private long writePointer;

		public UnsafeChunkDrawCallVector(int capacity) {
			super(capacity);
		}

		@Override
		public void pushChunkDrawParams(float x, float y, float z) {
			if (this.count++ >= this.capacity) {
				this.growBuffer();
			}

			CompatMemoryUtil.memPutFloat(this.writePointer, x);
			CompatMemoryUtil.memPutFloat(this.writePointer + 4L, y);
			CompatMemoryUtil.memPutFloat(this.writePointer + 8L, z);
			this.writePointer = this.writePointer + (long)this.stride;
		}

		@Override
		protected void growBuffer() {
			super.growBuffer();
			long offset = this.writePointer - this.basePointer;
			this.basePointer = MemoryUtil.memAddress(this.buffer);
			this.writePointer = this.basePointer + offset;
		}

		@Override
		public void reset() {
			super.reset();
			this.writePointer = this.basePointer;
		}
	}
}
