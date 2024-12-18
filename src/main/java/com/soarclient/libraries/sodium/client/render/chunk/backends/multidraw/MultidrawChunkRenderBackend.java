package com.soarclient.libraries.sodium.client.render.chunk.backends.multidraw;

import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Util;
import net.minecraft.util.Util.EnumOS;
import org.lwjgl.opengl.GL11;

import com.soarclient.libraries.sodium.client.gl.arena.GlBufferArena;
import com.soarclient.libraries.sodium.client.gl.arena.GlBufferSegment;
import com.soarclient.libraries.sodium.client.gl.attribute.GlVertexAttribute;
import com.soarclient.libraries.sodium.client.gl.attribute.GlVertexAttributeBinding;
import com.soarclient.libraries.sodium.client.gl.attribute.GlVertexAttributeFormat;
import com.soarclient.libraries.sodium.client.gl.buffer.GlBuffer;
import com.soarclient.libraries.sodium.client.gl.buffer.GlBufferTarget;
import com.soarclient.libraries.sodium.client.gl.buffer.GlBufferUsage;
import com.soarclient.libraries.sodium.client.gl.buffer.GlMutableBuffer;
import com.soarclient.libraries.sodium.client.gl.buffer.VertexData;
import com.soarclient.libraries.sodium.client.gl.device.CommandList;
import com.soarclient.libraries.sodium.client.gl.device.DrawCommandList;
import com.soarclient.libraries.sodium.client.gl.device.RenderDevice;
import com.soarclient.libraries.sodium.client.gl.func.GlFunctions;
import com.soarclient.libraries.sodium.client.gl.tessellation.GlPrimitiveType;
import com.soarclient.libraries.sodium.client.gl.tessellation.GlTessellation;
import com.soarclient.libraries.sodium.client.gl.tessellation.TessellationBinding;
import com.soarclient.libraries.sodium.client.gl.util.BufferSlice;
import com.soarclient.libraries.sodium.client.model.quad.properties.ModelQuadFacing;
import com.soarclient.libraries.sodium.client.model.vertex.type.ChunkVertexType;
import com.soarclient.libraries.sodium.client.render.chunk.ChunkCameraContext;
import com.soarclient.libraries.sodium.client.render.chunk.ChunkRenderContainer;
import com.soarclient.libraries.sodium.client.render.chunk.compile.ChunkBuildResult;
import com.soarclient.libraries.sodium.client.render.chunk.data.ChunkMeshData;
import com.soarclient.libraries.sodium.client.render.chunk.data.ChunkRenderData;
import com.soarclient.libraries.sodium.client.render.chunk.format.ChunkMeshAttribute;
import com.soarclient.libraries.sodium.client.render.chunk.lists.ChunkRenderListIterator;
import com.soarclient.libraries.sodium.client.render.chunk.passes.BlockRenderPass;
import com.soarclient.libraries.sodium.client.render.chunk.region.ChunkRegion;
import com.soarclient.libraries.sodium.client.render.chunk.region.ChunkRegionManager;
import com.soarclient.libraries.sodium.client.render.chunk.shader.ChunkRenderShaderBackend;
import com.soarclient.libraries.sodium.client.render.chunk.shader.ChunkShaderBindingPoints;

public class MultidrawChunkRenderBackend extends ChunkRenderShaderBackend<MultidrawGraphicsState> {
	private final ChunkRegionManager<MultidrawGraphicsState> bufferManager;
	private final ObjectArrayList<ChunkRegion<MultidrawGraphicsState>> pendingBatches = new ObjectArrayList<>();
	private final ObjectArrayFIFOQueue<ChunkRegion<MultidrawGraphicsState>> pendingUploads = new ObjectArrayFIFOQueue<>();
	private final GlMutableBuffer uploadBuffer;
	private final GlMutableBuffer uniformBuffer;
	private final GlMutableBuffer commandBuffer;
	private final ChunkDrawParamsVector uniformBufferBuilder;
	private final IndirectCommandBufferVector commandClientBufferBuilder;
	private boolean reverseRegions = false;
	private ChunkCameraContext regionCamera;
	private static final Comparator<ChunkRegion<?>> REGION_REVERSER = Comparator
			.<ChunkRegion<?>>comparingDouble(r -> r.camDistance).reversed();
	private static final Pattern INTEL_BUILD_MATCHER = Pattern
			.compile("(\\d.\\d.\\d) - Build (\\d+).(\\d+).(\\d+).(\\d+)");

	public MultidrawChunkRenderBackend(RenderDevice device, ChunkVertexType vertexType) {
		super(vertexType);
		this.bufferManager = new ChunkRegionManager<>(device);

		try (CommandList commands = device.createCommandList()) {
			this.uploadBuffer = commands.createMutableBuffer(GlBufferUsage.GL_STREAM_DRAW);
			this.uniformBuffer = commands.createMutableBuffer(GlBufferUsage.GL_STATIC_DRAW);
			this.commandBuffer = isWindowsIntelDriver() ? null
					: commands.createMutableBuffer(GlBufferUsage.GL_STREAM_DRAW);
		}

		this.uniformBufferBuilder = ChunkDrawParamsVector.create(2048);
		this.commandClientBufferBuilder = IndirectCommandBufferVector.create(2048);
	}

	@Override
	public void upload(CommandList commandList, Iterator<ChunkBuildResult<MultidrawGraphicsState>> queue) {
		if (queue != null) {
			this.setupUploadBatches(queue);
		}

		commandList.bindBuffer(GlBufferTarget.ARRAY_BUFFER, this.uploadBuffer);

		while (!this.pendingUploads.isEmpty()) {
			ChunkRegion<MultidrawGraphicsState> region = this.pendingUploads.dequeue();
			GlBufferArena arena = region.getBufferArena();
			GlBuffer buffer = arena.getBuffer();
			ObjectArrayList<ChunkBuildResult<MultidrawGraphicsState>> uploadQueue = region.getUploadQueue();
			arena.prepareBuffer(commandList, getUploadQueuePayloadSize(uploadQueue));

			for (ChunkBuildResult<MultidrawGraphicsState> result : uploadQueue) {
				ChunkRenderContainer<MultidrawGraphicsState> render = result.render;
				ChunkRenderData data = result.data;

				for (BlockRenderPass pass : result.passesToUpload) {
					MultidrawGraphicsState graphics = render.getGraphicsState(pass);
					if (graphics != null) {
						graphics.delete(commandList);
					}

					ChunkMeshData meshData = data.getMesh(pass);
					if (meshData.hasVertexData()) {
						VertexData upload = meshData.takeVertexData();
						commandList.uploadData(this.uploadBuffer, upload.buffer);
						GlBufferSegment segment = arena.uploadBuffer(commandList, this.uploadBuffer, 0,
								upload.buffer.capacity());
						MultidrawGraphicsState graphicsState = new MultidrawGraphicsState(render, region, segment,
								meshData, this.vertexFormat);
						if (pass.isTranslucent()) {
							upload.buffer.limit(upload.buffer.capacity());
							upload.buffer.position(0);
							graphicsState.setTranslucencyData(upload.buffer);
						}

						render.setGraphicsState(pass, graphicsState);
					} else {
						render.setGraphicsState(pass, null);
					}
				}

				render.setData(data);
			}

			if (region.getTessellation() == null || buffer != arena.getBuffer()) {
				if (region.getTessellation() != null) {
					commandList.deleteTessellation(region.getTessellation());
				}

				region.setTessellation(this.createRegionTessellation(commandList, arena.getBuffer()));
			}

			uploadQueue.clear();
		}

		commandList.invalidateBuffer(this.uploadBuffer);
	}

	private GlTessellation createRegionTessellation(CommandList commandList, GlBuffer buffer) {
		return commandList
				.createTessellation(GlPrimitiveType.QUADS,
						new TessellationBinding[] {
								new TessellationBinding(buffer,
										new GlVertexAttributeBinding[] {
												new GlVertexAttributeBinding(ChunkShaderBindingPoints.POSITION,
														this.vertexFormat.getAttribute(ChunkMeshAttribute.POSITION)),
												new GlVertexAttributeBinding(ChunkShaderBindingPoints.COLOR,
														this.vertexFormat.getAttribute(ChunkMeshAttribute.COLOR)),
												new GlVertexAttributeBinding(ChunkShaderBindingPoints.TEX_COORD,
														this.vertexFormat.getAttribute(ChunkMeshAttribute.TEXTURE)),
												new GlVertexAttributeBinding(ChunkShaderBindingPoints.LIGHT_COORD,
														this.vertexFormat.getAttribute(ChunkMeshAttribute.LIGHT)) },
										false),
								new TessellationBinding(this.uniformBuffer,
										new GlVertexAttributeBinding[] { new GlVertexAttributeBinding(
												ChunkShaderBindingPoints.MODEL_OFFSET,
												new GlVertexAttribute(GlVertexAttributeFormat.FLOAT, 4, false, 0, 0)) },
										true) });
	}

	public void setReverseRegions(boolean flag) {
		this.reverseRegions = flag;
	}

	@Override
	public void render(CommandList commandList, ChunkRenderListIterator<MultidrawGraphicsState> renders,
			ChunkCameraContext camera) {
		this.bufferManager.cleanup();
		this.setupDrawBatches(commandList, renders, camera);
		this.buildCommandBuffer();
		if (this.commandBuffer != null) {
			commandList.bindBuffer(GlBufferTarget.DRAW_INDIRECT_BUFFER, this.commandBuffer);
			commandList.uploadData(this.commandBuffer, this.commandClientBufferBuilder.getBuffer());
		}

		long pointer = 0L;
		int originalPointerBufferPos = 0;
		ByteBuffer pointerBuffer;
		if (this.commandBuffer != null) {
			pointerBuffer = null;
		} else {
			pointerBuffer = this.commandClientBufferBuilder.getBuffer();
			originalPointerBufferPos = pointerBuffer.position();
		}

		for (ChunkRegion<?> region : this.pendingBatches) {
			ChunkDrawCallBatcher batch = region.getDrawBatcher();
			if (!batch.isEmpty()) {
				try (DrawCommandList drawCommandList = commandList.beginTessellating(region.getTessellation())) {
					if (pointerBuffer == null) {
						drawCommandList.multiDrawArraysIndirect(pointer, batch.getCount(), 0);
					} else {
						drawCommandList.multiDrawArraysIndirect(pointerBuffer, batch.getCount(), 0);
					}
				}
			}

			if (pointerBuffer == null) {
				pointer += (long) batch.getArrayLength();
			} else {
				pointerBuffer.position(pointerBuffer.position() + batch.getArrayLength());
			}
		}

		if (pointerBuffer != null) {
			pointerBuffer.position(originalPointerBufferPos);
		}

		this.pendingBatches.clear();
	}

	private void buildCommandBuffer() {
		this.commandClientBufferBuilder.begin();
		if (this.reverseRegions) {
			ChunkCameraContext camera = this.regionCamera;

			for (ChunkRegion<?> region : this.pendingBatches) {
				float x = camera.getChunkModelOffset(region.getCenterBlockX(), camera.blockOriginX, camera.originX);
				float y = camera.getChunkModelOffset(region.getCenterBlockY(), camera.blockOriginY, camera.originY);
				float z = camera.getChunkModelOffset(region.getCenterBlockZ(), camera.blockOriginZ, camera.originZ);
				region.camDistance = x * x + y * y + z * z;
			}

			this.pendingBatches.sort(REGION_REVERSER);
		}

		for (ChunkRegion<?> region : this.pendingBatches) {
			ChunkDrawCallBatcher batcher = region.getDrawBatcher();
			batcher.end();
			this.commandClientBufferBuilder.pushCommandBuffer(batcher);
		}

		this.commandClientBufferBuilder.end();
	}

	private void setupUploadBatches(Iterator<ChunkBuildResult<MultidrawGraphicsState>> renders) {
		while (renders.hasNext()) {
			ChunkBuildResult<MultidrawGraphicsState> result = (ChunkBuildResult<MultidrawGraphicsState>) renders.next();
			if (result != null) {
				ChunkRenderContainer<MultidrawGraphicsState> render = result.render;
				ChunkRegion<MultidrawGraphicsState> region = this.bufferManager.getRegion(render.getChunkX(),
						render.getChunkY(), render.getChunkZ());
				if (region == null) {
					if (result.data.getMeshSize() <= 0) {
						render.setData(result.data);
						continue;
					}

					region = this.bufferManager.getOrCreateRegion(render.getChunkX(), render.getChunkY(),
							render.getChunkZ());
				}

				ObjectArrayList<ChunkBuildResult<MultidrawGraphicsState>> uploadQueue = region.getUploadQueue();
				if (uploadQueue.isEmpty()) {
					this.pendingUploads.enqueue(region);
				}

				uploadQueue.add(result);
			}
		}
	}

	private void setupDrawBatches(CommandList commandList, ChunkRenderListIterator<MultidrawGraphicsState> it,
			ChunkCameraContext camera) {
		this.uniformBufferBuilder.reset();
		this.regionCamera = camera;
		int drawCount = 0;

		while (it.hasNext()) {
			MultidrawGraphicsState state = it.getGraphicsState();
			int visible = it.getVisibleFaces();
			int index = drawCount++;
			float x = camera.getChunkModelOffset(state.getX(), camera.blockOriginX, camera.originX);
			float y = camera.getChunkModelOffset(state.getY(), camera.blockOriginY, camera.originY);
			float z = camera.getChunkModelOffset(state.getZ(), camera.blockOriginZ, camera.originZ);
			this.uniformBufferBuilder.pushChunkDrawParams(x, y, z);
			ChunkRegion<MultidrawGraphicsState> region = state.getRegion();
			ChunkDrawCallBatcher batch = region.getDrawBatcher();
			if (!batch.isBuilding()) {
				batch.begin();
				this.pendingBatches.add(region);
			}

			int mask = 1;

			for (int i = 0; i < ModelQuadFacing.COUNT; i++) {
				if ((visible & mask) != 0) {
					long part = state.getModelPart(i);
					batch.addIndirectDrawCall(BufferSlice.unpackStart(part), BufferSlice.unpackLength(part), index, 1);
				}

				mask <<= 1;
			}

			it.advance();
		}

		commandList.uploadData(this.uniformBuffer, this.uniformBufferBuilder.getBuffer());
	}

	private static int getUploadQueuePayloadSize(List<ChunkBuildResult<MultidrawGraphicsState>> queue) {
		int size = 0;

		for (ChunkBuildResult<MultidrawGraphicsState> result : queue) {
			size += result.data.getMeshSize();
		}

		return size;
	}

	@Override
	public void delete() {
		super.delete();

		try (CommandList commands = RenderDevice.INSTANCE.createCommandList()) {
			commands.deleteBuffer(this.uploadBuffer);
			commands.deleteBuffer(this.uniformBuffer);
			if (this.commandBuffer != null) {
				commands.deleteBuffer(this.commandBuffer);
			}
		}

		this.bufferManager.delete();
		this.commandClientBufferBuilder.delete();
		this.uniformBufferBuilder.delete();
	}

	@Override
	public Class<MultidrawGraphicsState> getGraphicsStateType() {
		return MultidrawGraphicsState.class;
	}

	public static boolean isSupported(boolean disableDriverBlacklist) {
		return !disableDriverBlacklist && isKnownBrokenIntelDriver() ? false
				: GlFunctions.isVertexArraySupported() && GlFunctions.isBufferCopySupported()
						&& GlFunctions.isIndirectMultiDrawSupported() && GlFunctions.isInstancedArraySupported();
	}

	private static boolean isWindowsIntelDriver() {
		return Util.getOSType() != EnumOS.WINDOWS ? false : Objects.equals(GL11.glGetString(7936), "Intel");
	}

	private static boolean isKnownBrokenIntelDriver() {
		if (!isWindowsIntelDriver()) {
			return false;
		} else {
			String version = GL11.glGetString(7938);
			if (version == null) {
				return false;
			} else {
				Matcher matcher = INTEL_BUILD_MATCHER.matcher(version);
				return !matcher.matches() ? false : Integer.parseInt(matcher.group(4)) < 100;
			}
		}
	}

	@Override
	public String getRendererName() {
		return "Multidraw";
	}

	@Override
	public List<String> getDebugStrings() {
		List<String> list = new ArrayList();
		list.add(String.format("Active Buffers: %s", this.bufferManager.getAllocatedRegionCount()));
		list.add(String.format("Submission Mode: %s", this.commandBuffer != null ? EnumChatFormatting.AQUA + "Buffer"
				: EnumChatFormatting.LIGHT_PURPLE + "Client Memory"));
		return list;
	}
}
