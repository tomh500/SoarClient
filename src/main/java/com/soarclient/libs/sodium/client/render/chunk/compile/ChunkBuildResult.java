package com.soarclient.libs.sodium.client.render.chunk.compile;

import com.soarclient.libs.sodium.client.render.chunk.ChunkGraphicsState;
import com.soarclient.libs.sodium.client.render.chunk.ChunkRenderContainer;
import com.soarclient.libs.sodium.client.render.chunk.data.ChunkRenderData;
import com.soarclient.libs.sodium.client.render.chunk.passes.BlockRenderPass;

public class ChunkBuildResult<T extends ChunkGraphicsState> {
	public final ChunkRenderContainer<T> render;
	public final ChunkRenderData data;
	public BlockRenderPass[] passesToUpload;

	public ChunkBuildResult(ChunkRenderContainer<T> render, ChunkRenderData data) {
		this.render = render;
		this.data = data;
		this.passesToUpload = BlockRenderPass.VALUES;
	}
}
