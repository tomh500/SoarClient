package com.soarclient.libraries.sodium.client.render.chunk.shader;

import com.google.common.collect.ImmutableList;
import com.soarclient.libraries.sodium.client.render.chunk.shader.ChunkShaderFogComponent.Exp2;
import com.soarclient.libraries.sodium.client.render.chunk.shader.ChunkShaderFogComponent.Linear;
import com.soarclient.libraries.sodium.client.render.chunk.shader.ChunkShaderFogComponent.None;

import java.util.List;
import java.util.function.Function;

public enum ChunkFogMode {
	NONE(None::new, ImmutableList.of()), LINEAR(Linear::new, ImmutableList.of("USE_FOG", "USE_FOG_LINEAR")),
	EXP2(Exp2::new, ImmutableList.of("USE_FOG", "USE_FOG_EXP2"));

	private final Function<ChunkProgram, ChunkShaderFogComponent> factory;
	private final List<String> defines;

	private ChunkFogMode(Function<ChunkProgram, ChunkShaderFogComponent> factory, List<String> defines) {
		this.factory = factory;
		this.defines = defines;
	}

	public Function<ChunkProgram, ChunkShaderFogComponent> getFactory() {
		return this.factory;
	}

	public List<String> getDefines() {
		return this.defines;
	}
}
