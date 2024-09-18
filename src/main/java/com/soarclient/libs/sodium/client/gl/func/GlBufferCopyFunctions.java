package com.soarclient.libs.sodium.client.gl.func;

import org.lwjgl.opengl.ARBCopyBuffer;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GL31;

public enum GlBufferCopyFunctions {
	CORE {
		@Override
		public void glCopyBufferSubData(int readTarget, int writeTarget, long readOffset, long writeOffset, long size) {
			GL31.glCopyBufferSubData(readTarget, writeTarget, readOffset, writeOffset, size);
		}
	},
	ARB {
		@Override
		public void glCopyBufferSubData(int readTarget, int writeTarget, long readOffset, long writeOffset, long size) {
			ARBCopyBuffer.glCopyBufferSubData(readTarget, writeTarget, readOffset, writeOffset, size);
		}
	},
	UNSUPPORTED {
		@Override
		public void glCopyBufferSubData(int readTarget, int writeTarget, long readOffset, long writeOffset, long size) {
			throw new UnsupportedOperationException();
		}
	};

	static GlBufferCopyFunctions load(ContextCapabilities capabilities) {
		if (capabilities.OpenGL31) {
			return CORE;
		} else {
			return capabilities.GL_ARB_copy_buffer ? ARB : UNSUPPORTED;
		}
	}

	public abstract void glCopyBufferSubData(int integer1, int integer2, long long3, long long4, long long5);
}
