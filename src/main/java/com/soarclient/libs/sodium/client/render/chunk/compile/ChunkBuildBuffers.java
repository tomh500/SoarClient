package com.soarclient.libs.sodium.client.render.chunk.compile;

import java.nio.ByteBuffer;
import java.util.Map.Entry;

import com.soarclient.libs.sodium.SodiumClientMod;
import com.soarclient.libs.sodium.client.gl.buffer.VertexData;
import com.soarclient.libs.sodium.client.gl.util.BufferSlice;
import com.soarclient.libs.sodium.client.model.quad.properties.ModelQuadFacing;
import com.soarclient.libs.sodium.client.model.vertex.buffer.VertexBufferBuilder;
import com.soarclient.libs.sodium.client.model.vertex.type.ChunkVertexType;
import com.soarclient.libs.sodium.client.render.chunk.compile.buffers.BakedChunkModelBuffers;
import com.soarclient.libs.sodium.client.render.chunk.compile.buffers.ChunkModelBuffers;
import com.soarclient.libs.sodium.client.render.chunk.compile.buffers.ChunkModelVertexTransformer;
import com.soarclient.libs.sodium.client.render.chunk.data.ChunkMeshData;
import com.soarclient.libs.sodium.client.render.chunk.data.ChunkRenderData.Builder;
import com.soarclient.libs.sodium.client.render.chunk.format.ChunkModelOffset;
import com.soarclient.libs.sodium.client.render.chunk.passes.BlockRenderPass;
import com.soarclient.libs.sodium.client.render.chunk.passes.BlockRenderPassManager;
import com.soarclient.libs.sodium.client.util.BufferSizeUtil;
import com.soarclient.libs.sodium.client.util.EnumUtil;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.util.EnumWorldBlockLayer;

public class ChunkBuildBuffers {
	private final ChunkModelBuffers[] delegates;
	private final VertexBufferBuilder[][] buffersByLayer;
	private final ChunkVertexType vertexType;
	private final BlockRenderPassManager renderPassManager;
	private final ChunkModelOffset offset;

	public ChunkBuildBuffers(ChunkVertexType vertexType, BlockRenderPassManager renderPassManager) {
		this.vertexType = vertexType;
		this.renderPassManager = renderPassManager;
		this.delegates = new ChunkModelBuffers[BlockRenderPass.COUNT];
		this.buffersByLayer = new VertexBufferBuilder[BlockRenderPass.COUNT][ModelQuadFacing.COUNT];
		this.offset = new ChunkModelOffset();

		for (EnumWorldBlockLayer layer : EnumUtil.LAYERS) {
			int passId = this.renderPassManager.getRenderPassId(layer);
			VertexBufferBuilder[] buffers = this.buffersByLayer[passId];

			for (ModelQuadFacing facing : ModelQuadFacing.VALUES) {
				buffers[facing.ordinal()] = new VertexBufferBuilder(
					vertexType.getBufferVertexFormat(), (Integer)BufferSizeUtil.BUFFER_SIZES.get(layer) / ModelQuadFacing.COUNT
				);
			}
		}
	}

	public void init(Builder renderData) {
		for (int i = 0; i < this.buffersByLayer.length; i++) {
			ChunkModelVertexTransformer[] writers = new ChunkModelVertexTransformer[ModelQuadFacing.COUNT];

			for (ModelQuadFacing facing : ModelQuadFacing.VALUES) {
				writers[facing.ordinal()] = new ChunkModelVertexTransformer(
					this.vertexType.createBufferWriter(this.buffersByLayer[i][facing.ordinal()], SodiumClientMod.isDirectMemoryAccessEnabled()), this.offset
				);
			}

			this.delegates[i] = new BakedChunkModelBuffers(writers, renderData);
		}
	}

	public ChunkModelBuffers get(EnumWorldBlockLayer layer) {
		return this.delegates[this.renderPassManager.getRenderPassId(layer)];
	}

	public ChunkMeshData createMesh(BlockRenderPass pass, float x, float y, float z, boolean sortTranslucent) {
		VertexBufferBuilder[] builders = this.buffersByLayer[pass.ordinal()];
		ChunkMeshData meshData = null;
		int bufferLen = 0;

		for (int facingId = 0; facingId < builders.length; facingId++) {
			VertexBufferBuilder builder = builders[facingId];
			if (builder != null && !builder.isEmpty()) {
				int size = builder.getSize();
				if (meshData == null) {
					meshData = new ChunkMeshData();
				}

				meshData.setModelSlice(ModelQuadFacing.VALUES[facingId], new BufferSlice(bufferLen, size));
				bufferLen += size;
			}
		}

		if (bufferLen <= 0) {
			return null;
		} else {
			ByteBuffer buffer = GLAllocation.createDirectByteBuffer(bufferLen);

			for (Entry<ModelQuadFacing, BufferSlice> entry : meshData.getSlices()) {
				BufferSlice slice = (BufferSlice)entry.getValue();
				buffer.position(slice.start);
				VertexBufferBuilder builder = this.buffersByLayer[pass.ordinal()][((ModelQuadFacing)entry.getKey()).ordinal()];
				builder.copyInto(buffer);
			}

			buffer.flip();
			if (sortTranslucent && pass.isTranslucent()) {
				ChunkBufferSorter.sortStandardFormat(this.vertexType, buffer, bufferLen, x, y, z);
			}

			meshData.setVertexData(new VertexData(buffer, this.vertexType.getCustomVertexFormat()));
			return meshData;
		}
	}

	public void setRenderOffset(float x, float y, float z) {
		this.offset.set(x, y, z);
	}

	public ChunkVertexType getVertexType() {
		return this.vertexType;
	}
}
