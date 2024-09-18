package com.soarclient.libs.sodium.client.render.chunk.backends.oneshot;

import java.util.Iterator;

import org.lwjgl.opengl.GL20;

import com.soarclient.libs.sodium.client.gl.device.CommandList;
import com.soarclient.libs.sodium.client.gl.device.DrawCommandList;
import com.soarclient.libs.sodium.client.gl.device.RenderDevice;
import com.soarclient.libs.sodium.client.gl.util.BufferSlice;
import com.soarclient.libs.sodium.client.gl.util.GlMultiDrawBatch;
import com.soarclient.libs.sodium.client.model.quad.properties.ModelQuadFacing;
import com.soarclient.libs.sodium.client.model.vertex.type.ChunkVertexType;
import com.soarclient.libs.sodium.client.render.chunk.ChunkCameraContext;
import com.soarclient.libs.sodium.client.render.chunk.ChunkRenderContainer;
import com.soarclient.libs.sodium.client.render.chunk.compile.ChunkBuildResult;
import com.soarclient.libs.sodium.client.render.chunk.data.ChunkMeshData;
import com.soarclient.libs.sodium.client.render.chunk.data.ChunkRenderData;
import com.soarclient.libs.sodium.client.render.chunk.lists.ChunkRenderListIterator;
import com.soarclient.libs.sodium.client.render.chunk.passes.BlockRenderPass;
import com.soarclient.libs.sodium.client.render.chunk.shader.ChunkRenderShaderBackend;
import com.soarclient.libs.sodium.client.render.chunk.shader.ChunkShaderBindingPoints;

public class ChunkRenderBackendOneshot extends ChunkRenderShaderBackend<ChunkOneshotGraphicsState> {
	private final GlMultiDrawBatch batch = new GlMultiDrawBatch(ModelQuadFacing.COUNT);

	public ChunkRenderBackendOneshot(ChunkVertexType vertexType) {
		super(vertexType);
	}

	@Override
	public void upload(CommandList commandList, Iterator<ChunkBuildResult<ChunkOneshotGraphicsState>> queue) {
		while (queue.hasNext()) {
			ChunkBuildResult<ChunkOneshotGraphicsState> result = (ChunkBuildResult<ChunkOneshotGraphicsState>)queue.next();
			ChunkRenderContainer<ChunkOneshotGraphicsState> render = result.render;
			ChunkRenderData data = result.data;

			for (BlockRenderPass pass : result.passesToUpload) {
				ChunkOneshotGraphicsState state = render.getGraphicsState(pass);
				ChunkMeshData mesh = data.getMesh(pass);
				if (mesh.hasVertexData()) {
					if (state == null) {
						state = new ChunkOneshotGraphicsState(RenderDevice.INSTANCE, render);
					}

					state.upload(commandList, mesh);
					if (!pass.isTranslucent()) {
						state.setTranslucencyData(null);
					}
				} else {
					if (state != null) {
						state.delete(commandList);
					}

					state = null;
				}

				render.setGraphicsState(pass, state);
			}

			render.setData(data);
		}
	}

	@Override
	public void render(CommandList commandList, ChunkRenderListIterator<ChunkOneshotGraphicsState> it, ChunkCameraContext camera) {
		while (it.hasNext()) {
			ChunkOneshotGraphicsState state = it.getGraphicsState();
			int visibleFaces = it.getVisibleFaces();
			this.buildBatch(state, visibleFaces);
			if (this.batch.isBuilding()) {
				this.prepareDrawBatch(camera, state);
				this.drawBatch(commandList, state);
			}

			it.advance();
		}
	}

	protected void prepareDrawBatch(ChunkCameraContext camera, ChunkOneshotGraphicsState state) {
		float modelX = camera.getChunkModelOffset(state.getX(), camera.blockOriginX, camera.originX);
		float modelY = camera.getChunkModelOffset(state.getY(), camera.blockOriginY, camera.originY);
		float modelZ = camera.getChunkModelOffset(state.getZ(), camera.blockOriginZ, camera.originZ);
		GL20.glVertexAttrib4f(ChunkShaderBindingPoints.MODEL_OFFSET.getGenericAttributeIndex(), modelX, modelY, modelZ, 0.0F);
	}

	protected void buildBatch(ChunkOneshotGraphicsState state, int visibleFaces) {
		GlMultiDrawBatch batch = this.batch;
		batch.begin();

		for (int i = 0; i < ModelQuadFacing.COUNT; i++) {
			if ((visibleFaces & 1 << i) != 0) {
				long part = state.getModelPart(i);
				batch.addChunkRender(BufferSlice.unpackStart(part), BufferSlice.unpackLength(part));
			}
		}
	}

	protected void drawBatch(CommandList commandList, ChunkOneshotGraphicsState state) {
		this.batch.end();
		if (!this.batch.isEmpty()) {
			try (DrawCommandList drawCommandList = commandList.beginTessellating(state.tessellation)) {
				drawCommandList.multiDrawArrays(this.batch.getIndicesBuffer(), this.batch.getLengthBuffer());
			}
		}
	}

	@Override
	public void delete() {
		super.delete();
		this.batch.delete();
	}

	@Override
	public Class<ChunkOneshotGraphicsState> getGraphicsStateType() {
		return ChunkOneshotGraphicsState.class;
	}

	@Override
	public String getRendererName() {
		return "Oneshot";
	}
}
