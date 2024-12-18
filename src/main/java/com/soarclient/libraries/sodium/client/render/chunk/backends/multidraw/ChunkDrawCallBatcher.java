package com.soarclient.libraries.sodium.client.render.chunk.backends.multidraw;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import org.lwjgl.system.MemoryUtil;

import com.soarclient.libraries.sodium.SodiumClientMod;
import com.soarclient.libraries.sodium.client.util.CompatMemoryUtil;

import net.minecraft.util.MathHelper;

public abstract class ChunkDrawCallBatcher extends StructBuffer {
	protected final int capacity;
	protected boolean isBuilding;
	protected int count;
	protected int arrayLength;

	protected ChunkDrawCallBatcher(int capacity) {
		super(MathHelper.roundUpToPowerOfTwo(capacity), 16);
		this.capacity = capacity;
	}

	public static ChunkDrawCallBatcher create(int capacity) {
		return (ChunkDrawCallBatcher) (SodiumClientMod.isDirectMemoryAccessEnabled()
				? new ChunkDrawCallBatcher.UnsafeChunkDrawCallBatcher(capacity)
				: new ChunkDrawCallBatcher.NioChunkDrawCallBatcher(capacity));
	}

	public void begin() {
		this.isBuilding = true;
		this.count = 0;
		this.arrayLength = 0;
		this.buffer.limit(this.buffer.capacity());
	}

	public void end() {
		this.isBuilding = false;
		this.arrayLength = this.count * this.stride;
		this.buffer.limit(this.arrayLength);
		this.buffer.position(0);
	}

	public boolean isBuilding() {
		return this.isBuilding;
	}

	public abstract void addIndirectDrawCall(int integer1, int integer2, int integer3, int integer4);

	public int getCount() {
		return this.count;
	}

	public boolean isEmpty() {
		return this.count <= 0;
	}

	public int getArrayLength() {
		return this.arrayLength;
	}

	public static class NioChunkDrawCallBatcher extends ChunkDrawCallBatcher {
		private int writeOffset;

		public NioChunkDrawCallBatcher(int capacity) {
			super(capacity);
		}

		@Override
		public void begin() {
			super.begin();
			this.writeOffset = 0;
		}

		@Override
		public void addIndirectDrawCall(int first, int count, int baseInstance, int instanceCount) {
			ByteBuffer buf = this.buffer;
			buf.putInt(this.writeOffset, count);
			buf.putInt(this.writeOffset + 4, instanceCount);
			buf.putInt(this.writeOffset + 8, first);
			buf.putInt(this.writeOffset + 12, baseInstance);
			this.writeOffset = this.writeOffset + this.stride;
			this.count++;
		}
	}

	public static class UnsafeChunkDrawCallBatcher extends ChunkDrawCallBatcher {
		private final long basePointer = MemoryUtil.memAddress(this.buffer);
		private long writePointer;

		public UnsafeChunkDrawCallBatcher(int capacity) {
			super(capacity);
		}

		@Override
		public void begin() {
			super.begin();
			this.writePointer = this.basePointer;
		}

		@Override
		public void addIndirectDrawCall(int first, int count, int baseInstance, int instanceCount) {
			if (this.count++ >= this.capacity) {
				throw new BufferUnderflowException();
			} else {
				CompatMemoryUtil.memPutInt(this.writePointer, count);
				CompatMemoryUtil.memPutInt(this.writePointer + 4L, instanceCount);
				CompatMemoryUtil.memPutInt(this.writePointer + 8L, first);
				CompatMemoryUtil.memPutInt(this.writePointer + 12L, baseInstance);
				this.writePointer = this.writePointer + (long) this.stride;
			}
		}
	}
}
