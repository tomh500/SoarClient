package com.soarclient.libraries.sodium.client.render.chunk.data;

import java.util.EnumMap;
import java.util.Map.Entry;

import com.soarclient.libraries.sodium.client.gl.buffer.VertexData;
import com.soarclient.libraries.sodium.client.gl.util.BufferSlice;
import com.soarclient.libraries.sodium.client.model.quad.properties.ModelQuadFacing;

public class ChunkMeshData {
	public static final ChunkMeshData EMPTY = new ChunkMeshData();
	private final EnumMap<ModelQuadFacing, BufferSlice> parts = new EnumMap(ModelQuadFacing.class);
	private VertexData vertexData;

	public void setModelSlice(ModelQuadFacing facing, BufferSlice slice) {
		this.parts.put(facing, slice);
	}

	public VertexData takeVertexData() {
		VertexData data = this.vertexData;
		if (data == null) {
			throw new NullPointerException("No pending data to upload");
		} else {
			this.vertexData = null;
			return data;
		}
	}

	public boolean hasVertexData() {
		return this.vertexData != null;
	}

	public int getVertexDataSize() {
		return this.vertexData != null ? this.vertexData.buffer.capacity() : 0;
	}

	public Iterable<? extends Entry<ModelQuadFacing, BufferSlice>> getSlices() {
		return this.parts.entrySet();
	}

	public void setVertexData(VertexData vertexData) {
		this.vertexData = vertexData;
	}
}
