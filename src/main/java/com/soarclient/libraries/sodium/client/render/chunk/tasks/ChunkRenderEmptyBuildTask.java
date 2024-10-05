package com.soarclient.libraries.sodium.client.render.chunk.tasks;

import com.soarclient.libraries.sodium.client.render.chunk.ChunkGraphicsState;
import com.soarclient.libraries.sodium.client.render.chunk.ChunkRenderContainer;
import com.soarclient.libraries.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import com.soarclient.libraries.sodium.client.render.chunk.compile.ChunkBuildResult;
import com.soarclient.libraries.sodium.client.render.chunk.data.ChunkRenderData;
import com.soarclient.libraries.sodium.client.render.pipeline.context.ChunkRenderCacheLocal;
import com.soarclient.libraries.sodium.client.util.task.CancellationSource;

public class ChunkRenderEmptyBuildTask<T extends ChunkGraphicsState> extends ChunkRenderBuildTask<T> {
	private final ChunkRenderContainer<T> render;

	public ChunkRenderEmptyBuildTask(ChunkRenderContainer<T> render) {
		this.render = render;
	}

	@Override
	public ChunkBuildResult<T> performBuild(ChunkRenderCacheLocal cache, ChunkBuildBuffers buffers,
			CancellationSource cancellationSource) {
		return new ChunkBuildResult<>(this.render, ChunkRenderData.EMPTY);
	}

	@Override
	public void releaseResources() {
	}
}
