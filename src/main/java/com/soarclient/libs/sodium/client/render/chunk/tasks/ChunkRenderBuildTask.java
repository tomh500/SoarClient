package com.soarclient.libs.sodium.client.render.chunk.tasks;

import com.soarclient.libs.sodium.client.render.chunk.ChunkGraphicsState;
import com.soarclient.libs.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import com.soarclient.libs.sodium.client.render.chunk.compile.ChunkBuildResult;
import com.soarclient.libs.sodium.client.render.pipeline.context.ChunkRenderCacheLocal;
import com.soarclient.libs.sodium.client.util.task.CancellationSource;

public abstract class ChunkRenderBuildTask<T extends ChunkGraphicsState> {
	public abstract ChunkBuildResult<T> performBuild(
		ChunkRenderCacheLocal chunkRenderCacheLocal, ChunkBuildBuffers chunkBuildBuffers, CancellationSource cancellationSource
	);

	public abstract void releaseResources();
}
