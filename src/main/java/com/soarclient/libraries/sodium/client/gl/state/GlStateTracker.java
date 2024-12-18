package com.soarclient.libraries.sodium.client.gl.state;

import java.util.Arrays;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import com.soarclient.libraries.sodium.client.gl.array.GlVertexArray;
import com.soarclient.libraries.sodium.client.gl.buffer.GlBuffer;
import com.soarclient.libraries.sodium.client.gl.buffer.GlBufferTarget;

public class GlStateTracker {
	private static final int UNASSIGNED_HANDLE = -1;
	private final int[] bufferState = new int[GlBufferTarget.COUNT];
	private final int[] bufferRestoreState = new int[GlBufferTarget.COUNT];
	private int vertexArrayState;
	private int vertexArrayRestoreState;

	public GlStateTracker() {
		this.clearRestoreState();
	}

	public boolean makeBufferActive(GlBufferTarget target, GlBuffer buffer) {
		return this.makeBufferActive(target, buffer == null ? 0 : buffer.handle());
	}

	private boolean makeBufferActive(GlBufferTarget target, int buffer) {
		int prevBuffer = this.bufferState[target.ordinal()];
		if (prevBuffer == -1) {
			this.bufferRestoreState[target.ordinal()] = GL11.glGetInteger(target.getBindingParameter());
		}

		this.bufferState[target.ordinal()] = buffer;
		return prevBuffer != buffer;
	}

	public boolean makeVertexArrayActive(GlVertexArray array) {
		return this.makeVertexArrayActive(array == null ? 0 : array.handle());
	}

	private boolean makeVertexArrayActive(int array) {
		int prevArray = this.vertexArrayState;
		if (prevArray == -1) {
			this.vertexArrayRestoreState = GL11.glGetInteger(34229);
		}

		this.vertexArrayState = array;
		return prevArray != array;
	}

	public void applyRestoreState() {
		for (int i = 0; i < GlBufferTarget.COUNT; i++) {
			if (this.bufferState[i] != this.bufferRestoreState[i] && this.bufferRestoreState[i] != -1) {
				GL15.glBindBuffer(GlBufferTarget.VALUES[i].getTargetParameter(), this.bufferRestoreState[i]);
			}
		}

		if (this.vertexArrayState != this.vertexArrayRestoreState && this.vertexArrayRestoreState != -1) {
			GL30.glBindVertexArray(this.vertexArrayRestoreState);
		}
	}

	public void clearRestoreState() {
		Arrays.fill(this.bufferState, -1);
		Arrays.fill(this.bufferRestoreState, -1);
		this.vertexArrayState = -1;
		this.vertexArrayRestoreState = -1;
	}
}
