package com.soarclient.libraries.sodium.client.render.chunk.compile.buffers;

import com.soarclient.libraries.sodium.client.model.quad.properties.ModelQuadFacing;
import com.soarclient.libraries.sodium.client.render.chunk.data.ChunkRenderData.Builder;
import com.soarclient.libraries.sodium.client.render.chunk.format.ModelVertexSink;

public interface ChunkModelBuffers {
	ModelVertexSink getSink(ModelQuadFacing modelQuadFacing);

	@Deprecated
	Builder getRenderData();
}
