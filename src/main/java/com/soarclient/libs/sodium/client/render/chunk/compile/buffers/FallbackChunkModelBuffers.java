package com.soarclient.libs.sodium.client.render.chunk.compile.buffers;

import com.soarclient.libs.sodium.client.model.quad.properties.ModelQuadFacing;
import com.soarclient.libs.sodium.client.render.chunk.data.ChunkRenderData.Builder;
import com.soarclient.libs.sodium.client.render.chunk.format.ModelVertexSink;

public class FallbackChunkModelBuffers implements ChunkModelBuffers {
	@Override
	public ModelVertexSink getSink(ModelQuadFacing facing) {
		return null;
	}

	@Override
	public Builder getRenderData() {
		return null;
	}
}
