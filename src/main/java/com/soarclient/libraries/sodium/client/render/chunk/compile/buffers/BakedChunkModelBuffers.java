package com.soarclient.libraries.sodium.client.render.chunk.compile.buffers;

import com.soarclient.libraries.sodium.client.model.quad.properties.ModelQuadFacing;
import com.soarclient.libraries.sodium.client.render.chunk.data.ChunkRenderData.Builder;
import com.soarclient.libraries.sodium.client.render.chunk.format.ModelVertexSink;

public class BakedChunkModelBuffers implements ChunkModelBuffers {
	private final ModelVertexSink[] builders;
	private final Builder renderData;

	public BakedChunkModelBuffers(ModelVertexSink[] builders, Builder renderData) {
		this.builders = builders;
		this.renderData = renderData;
	}

	@Override
	public ModelVertexSink getSink(ModelQuadFacing facing) {
		return this.builders[facing.ordinal()];
	}

	@Override
	public Builder getRenderData() {
		return this.renderData;
	}
}
