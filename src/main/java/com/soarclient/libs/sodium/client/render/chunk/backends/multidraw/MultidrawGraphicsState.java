package com.soarclient.libs.sodium.client.render.chunk.backends.multidraw;

import java.util.Map.Entry;

import com.soarclient.libs.sodium.client.gl.arena.GlBufferSegment;
import com.soarclient.libs.sodium.client.gl.attribute.GlVertexFormat;
import com.soarclient.libs.sodium.client.gl.device.CommandList;
import com.soarclient.libs.sodium.client.gl.util.BufferSlice;
import com.soarclient.libs.sodium.client.model.quad.properties.ModelQuadFacing;
import com.soarclient.libs.sodium.client.render.chunk.ChunkGraphicsState;
import com.soarclient.libs.sodium.client.render.chunk.ChunkRenderContainer;
import com.soarclient.libs.sodium.client.render.chunk.data.ChunkMeshData;
import com.soarclient.libs.sodium.client.render.chunk.region.ChunkRegion;

public class MultidrawGraphicsState extends ChunkGraphicsState {
	private final ChunkRegion<MultidrawGraphicsState> region;
	private final GlBufferSegment segment;
	private final long[] parts;

	public MultidrawGraphicsState(
		ChunkRenderContainer<?> container,
		ChunkRegion<MultidrawGraphicsState> region,
		GlBufferSegment segment,
		ChunkMeshData meshData,
		GlVertexFormat<?> vertexFormat
	) {
		super(container);
		this.region = region;
		this.segment = segment;
		this.parts = new long[ModelQuadFacing.COUNT];

		for (Entry<ModelQuadFacing, BufferSlice> entry : meshData.getSlices()) {
			ModelQuadFacing facing = (ModelQuadFacing)entry.getKey();
			BufferSlice slice = (BufferSlice)entry.getValue();
			int start = (segment.getStart() + slice.start) / vertexFormat.getStride();
			int count = slice.len / vertexFormat.getStride();
			this.parts[facing.ordinal()] = BufferSlice.pack(start, count);
		}
	}

	@Override
	public void delete(CommandList commandList) {
		this.segment.delete();
	}

	public ChunkRegion<MultidrawGraphicsState> getRegion() {
		return this.region;
	}

	public long getModelPart(int facing) {
		return this.parts[facing];
	}
}
