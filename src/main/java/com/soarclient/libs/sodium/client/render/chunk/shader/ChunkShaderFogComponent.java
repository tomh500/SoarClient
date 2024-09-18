package com.soarclient.libs.sodium.client.render.chunk.shader;

import org.lwjgl.opengl.GL20;

import com.soarclient.libs.sodium.client.gl.compat.FogHelper;

public abstract class ChunkShaderFogComponent {
	public abstract void setup();

	public static class Exp2 extends ChunkShaderFogComponent {
		private final int uFogColor;
		private final int uFogDensity;

		public Exp2(ChunkProgram program) {
			this.uFogColor = program.getUniformLocation("u_FogColor");
			this.uFogDensity = program.getUniformLocation("u_FogDensity");
		}

		@Override
		public void setup() {
			float[] fc = FogHelper.getFogColor();
			GL20.glUniform4f(this.uFogColor, fc[0], fc[1], fc[2], fc[3]);
			GL20.glUniform1f(this.uFogDensity, FogHelper.getFogDensity());
		}
	}

	public static class Linear extends ChunkShaderFogComponent {
		private final int uFogColor;
		private final int uFogLength;
		private final int uFogEnd;

		public Linear(ChunkProgram program) {
			this.uFogColor = program.getUniformLocation("u_FogColor");
			this.uFogLength = program.getUniformLocation("u_FogLength");
			this.uFogEnd = program.getUniformLocation("u_FogEnd");
		}

		@Override
		public void setup() {
			float end = FogHelper.getFogEnd();
			float start = FogHelper.getFogStart();
			float[] color = FogHelper.getFogColor();
			GL20.glUniform4f(this.uFogColor, color[0], color[1], color[2], color[3]);
			GL20.glUniform1f(this.uFogLength, end - start);
			GL20.glUniform1f(this.uFogEnd, end);
		}
	}

	public static class None extends ChunkShaderFogComponent {
		public None(ChunkProgram program) {
		}

		@Override
		public void setup() {
		}
	}
}
