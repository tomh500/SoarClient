package com.soarclient.libs.sodium.client.gl.shader;

import org.lwjgl.opengl.GL20;

class ShaderWorkarounds {
	static void safeShaderSource(int glId, CharSequence source) {
		GL20.glShaderSource(glId, source);
	}
}
