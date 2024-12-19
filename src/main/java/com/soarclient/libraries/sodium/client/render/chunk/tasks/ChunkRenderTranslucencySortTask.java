package com.soarclient.libraries.sodium.client.render.chunk.tasks;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.soarclient.libraries.sodium.client.gl.buffer.VertexData;
import com.soarclient.libraries.sodium.client.gl.util.BufferSlice;
import com.soarclient.libraries.sodium.client.model.quad.properties.ModelQuadFacing;
import com.soarclient.libraries.sodium.client.render.chunk.ChunkGraphicsState;
import com.soarclient.libraries.sodium.client.render.chunk.ChunkRenderContainer;
import com.soarclient.libraries.sodium.client.render.chunk.compile.ChunkBufferSorter;
import com.soarclient.libraries.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import com.soarclient.libraries.sodium.client.render.chunk.compile.ChunkBuildResult;
import com.soarclient.libraries.sodium.client.render.chunk.data.ChunkMeshData;
import com.soarclient.libraries.sodium.client.render.chunk.data.ChunkRenderData;
import com.soarclient.libraries.sodium.client.render.chunk.passes.BlockRenderPass;
import com.soarclient.libraries.sodium.client.render.pipeline.context.ChunkRenderCacheLocal;
import com.soarclient.libraries.sodium.client.util.task.CancellationSource;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class ChunkRenderTranslucencySortTask<T extends ChunkGraphicsState> extends ChunkRenderBuildTask<T> {
	private static final BlockRenderPass[] TRANSLUCENT_PASSES = (BlockRenderPass[]) Arrays
			.stream(BlockRenderPass.VALUES).filter(BlockRenderPass::isTranslucent).toArray(BlockRenderPass[]::new);
	private static final BlockRenderPass[] NO_PASSES = new BlockRenderPass[0];
	private final ChunkRenderContainer<T> render;
	private final BlockPos offset;
	private final Vec3 camera;

	public ChunkRenderTranslucencySortTask(ChunkRenderContainer<T> render, BlockPos offset, Vec3 camera) {
		this.render = render;
		this.offset = offset;
		this.camera = camera;
	}

	@Override
	public ChunkBuildResult<T> performBuild(ChunkRenderCacheLocal cache, ChunkBuildBuffers buffers,
			CancellationSource cancellationSource) {
		ChunkRenderData data = this.render.getData();
		Map<BlockRenderPass, ChunkMeshData> replacementMeshes;
		if (!data.isEmpty()) {
			replacementMeshes = new HashMap();

			for (BlockRenderPass pass : TRANSLUCENT_PASSES) {
				ChunkGraphicsState state = this.render.getGraphicsState(pass);
				if (state != null) {
					ByteBuffer translucencyData = state.getTranslucencyData();
					if (translucencyData != null) {
						ChunkMeshData translucentMesh = data.getMesh(pass);
						if (translucentMesh != null) {
							ByteBuffer sortedData = GLAllocation.createDirectByteBuffer(translucencyData.capacity());
							synchronized (translucencyData) {
								sortedData.put(translucencyData);
								translucencyData.position(0);
								translucencyData.limit(translucencyData.capacity());
							}

							sortedData.flip();
							ChunkBufferSorter.sortStandardFormat(buffers.getVertexType(), sortedData,
									sortedData.capacity(), (float) this.camera.xCoord - (float) this.offset.getX(),
									(float) this.camera.yCoord - (float) this.offset.getY(),
									(float) this.camera.zCoord - (float) this.offset.getZ());
							ChunkMeshData newMesh = new ChunkMeshData();
							newMesh.setVertexData(
									new VertexData(sortedData, buffers.getVertexType().getCustomVertexFormat()));

							for (Entry<ModelQuadFacing, BufferSlice> entry : translucentMesh.getSlices()) {
								newMesh.setModelSlice((ModelQuadFacing) entry.getKey(), (BufferSlice) entry.getValue());
							}

							replacementMeshes.put(pass, newMesh);
						}
					}
				}
			}
		} else {
			replacementMeshes = Collections.emptyMap();
		}

		ChunkBuildResult<T> result = new ChunkBuildResult<>(this.render, data.copyAndReplaceMesh(replacementMeshes));
		result.passesToUpload = (BlockRenderPass[]) replacementMeshes.keySet().toArray(NO_PASSES);
		return result;
	}

	@Override
	public void releaseResources() {
	}
}
