package com.soarclient.libraries.sodium.client.model.vertex.type;

import com.soarclient.libraries.sodium.client.render.chunk.format.ChunkMeshAttribute;
import com.soarclient.libraries.sodium.client.render.chunk.format.ModelVertexSink;

public interface ChunkVertexType
		extends BlittableVertexType<ModelVertexSink>, CustomVertexType<ModelVertexSink, ChunkMeshAttribute> {
	float getModelScale();

	float getTextureScale();
}
