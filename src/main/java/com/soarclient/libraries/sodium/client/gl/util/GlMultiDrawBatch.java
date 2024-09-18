package com.soarclient.libraries.sodium.client.gl.util;

import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;

public class GlMultiDrawBatch {
	private final IntBuffer bufIndices;
	private final IntBuffer bufLen;
	private int count;
	private boolean isBuilding;

	public GlMultiDrawBatch(int capacity) {
		this.bufIndices = BufferUtils.createIntBuffer(capacity);
		this.bufLen = BufferUtils.createIntBuffer(capacity);
	}

	public IntBuffer getIndicesBuffer() {
		return this.bufIndices;
	}

	public IntBuffer getLengthBuffer() {
		return this.bufLen;
	}

	public void begin() {
		this.bufIndices.clear();
		this.bufLen.clear();
		this.count = 0;
		this.isBuilding = true;
	}

	public void end() {
		this.bufIndices.limit(this.count);
		this.bufLen.limit(this.count);
		this.isBuilding = false;
	}

	public boolean isEmpty() {
		return this.count <= 0;
	}

	public void addChunkRender(int first, int count) {
		int i = this.count++;
		this.bufIndices.put(i, first);
		this.bufLen.put(i, count);
	}

	public boolean isBuilding() {
		return this.isBuilding;
	}

	public void delete() {
	}
}
