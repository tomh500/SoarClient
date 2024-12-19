package com.soarclient.libraries.sodium.client.gl.func;

import java.nio.ByteBuffer;
import org.lwjgl.opengl.ARBMultiDrawIndirect;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GL43;

public enum GlIndirectMultiDrawFunctions {
	CORE {
		@Override
		public void glMultiDrawArraysIndirect(int mode, ByteBuffer indirect, int primcount, int stride) {
			GL43.glMultiDrawArraysIndirect(mode, indirect, primcount, stride);
		}

		@Override
		public void glMultiDrawArraysIndirect(int mode, long indirect, int primcount, int stride) {
			GL43.glMultiDrawArraysIndirect(mode, indirect, primcount, stride);
		}
	},
	ARB {
		@Override
		public void glMultiDrawArraysIndirect(int mode, ByteBuffer indirect, int primcount, int stride) {
			ARBMultiDrawIndirect.glMultiDrawArraysIndirect(mode, indirect, primcount, stride);
		}

		@Override
		public void glMultiDrawArraysIndirect(int mode, long indirect, int primcount, int stride) {
			ARBMultiDrawIndirect.glMultiDrawArraysIndirect(mode, indirect, primcount, stride);
		}
	},
	UNSUPPORTED {
		@Override
		public void glMultiDrawArraysIndirect(int mode, ByteBuffer indirect, int primcount, int stride) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void glMultiDrawArraysIndirect(int mode, long indirect, int primcount, int stride) {
			throw new UnsupportedOperationException();
		}
	};

	public static GlIndirectMultiDrawFunctions load(ContextCapabilities capabilities) {
		if (capabilities.OpenGL43) {
			return CORE;
		} else {
			return capabilities.GL_ARB_multi_draw_indirect && capabilities.GL_ARB_draw_indirect ? ARB : UNSUPPORTED;
		}
	}

	public abstract void glMultiDrawArraysIndirect(int integer1, ByteBuffer byteBuffer, int integer3, int integer4);

	public abstract void glMultiDrawArraysIndirect(int integer1, long long2, int integer3, int integer4);
}
