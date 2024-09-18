package com.soarclient.libs.sodium.client.render.chunk.compile.buffers;

import com.soarclient.libs.sodium.client.model.quad.properties.ModelQuadFacing;
import com.soarclient.libs.sodium.client.render.chunk.data.ChunkRenderData.Builder;
import com.soarclient.libs.sodium.client.render.chunk.format.ModelVertexSink;

public interface ChunkModelBuffers {
	ModelVertexSink getSink(ModelQuadFacing modelQuadFacing);

	@Deprecated
	Builder getRenderData();
}
