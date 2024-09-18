package com.soarclient.libs.sodium.client.render.chunk.backends.oneshot;

import java.util.Arrays;
import java.util.Map.Entry;

import com.soarclient.libs.sodium.client.gl.attribute.GlVertexAttributeBinding;
import com.soarclient.libs.sodium.client.gl.attribute.GlVertexFormat;
import com.soarclient.libs.sodium.client.gl.buffer.GlBufferUsage;
import com.soarclient.libs.sodium.client.gl.buffer.GlMutableBuffer;
import com.soarclient.libs.sodium.client.gl.buffer.VertexData;
import com.soarclient.libs.sodium.client.gl.device.CommandList;
import com.soarclient.libs.sodium.client.gl.device.RenderDevice;
import com.soarclient.libs.sodium.client.gl.tessellation.GlPrimitiveType;
import com.soarclient.libs.sodium.client.gl.tessellation.GlTessellation;
import com.soarclient.libs.sodium.client.gl.tessellation.TessellationBinding;
import com.soarclient.libs.sodium.client.gl.util.BufferSlice;
import com.soarclient.libs.sodium.client.model.quad.properties.ModelQuadFacing;
import com.soarclient.libs.sodium.client.render.chunk.ChunkGraphicsState;
import com.soarclient.libs.sodium.client.render.chunk.ChunkRenderContainer;
import com.soarclient.libs.sodium.client.render.chunk.data.ChunkMeshData;
import com.soarclient.libs.sodium.client.render.chunk.format.ChunkMeshAttribute;
import com.soarclient.libs.sodium.client.render.chunk.shader.ChunkShaderBindingPoints;

public class ChunkOneshotGraphicsState extends ChunkGraphicsState {
	private final GlMutableBuffer vertexBuffer;
	protected GlTessellation tessellation;
	private final long[] parts = new long[ModelQuadFacing.COUNT];

	protected ChunkOneshotGraphicsState(RenderDevice device, ChunkRenderContainer<?> container) {
		super(container);

		try (CommandList commands = device.createCommandList()) {
			this.vertexBuffer = commands.createMutableBuffer(GlBufferUsage.GL_STATIC_DRAW);
		}
	}

	public long getModelPart(int facing) {
		return this.parts[facing];
	}

	protected void setModelPart(ModelQuadFacing facing, long slice) {
		this.parts[facing.ordinal()] = slice;
	}

	protected void setupModelParts(ChunkMeshData meshData, GlVertexFormat<?> vertexFormat) {
		int stride = vertexFormat.getStride();
		Arrays.fill(this.parts, 0L);

		for (Entry<ModelQuadFacing, BufferSlice> entry : meshData.getSlices()) {
			ModelQuadFacing facing = (ModelQuadFacing)entry.getKey();
			BufferSlice slice = (BufferSlice)entry.getValue();
			this.setModelPart(facing, BufferSlice.pack(slice.start / stride, slice.len / stride));
		}
	}

	@Override
	public void delete(CommandList commandList) {
		commandList.deleteBuffer(this.vertexBuffer);
	}

	public void upload(CommandList commandList, ChunkMeshData meshData) {
		VertexData vertexData = meshData.takeVertexData();
		commandList.uploadData(this.vertexBuffer, vertexData);
		GlVertexFormat<ChunkMeshAttribute> vertexFormat = (GlVertexFormat<ChunkMeshAttribute>)vertexData.format;
		this.tessellation = commandList.createTessellation(
			GlPrimitiveType.QUADS,
			new TessellationBinding[]{
				new TessellationBinding(
					this.vertexBuffer,
					new GlVertexAttributeBinding[]{
						new GlVertexAttributeBinding(ChunkShaderBindingPoints.POSITION, vertexFormat.getAttribute(ChunkMeshAttribute.POSITION)),
						new GlVertexAttributeBinding(ChunkShaderBindingPoints.COLOR, vertexFormat.getAttribute(ChunkMeshAttribute.COLOR)),
						new GlVertexAttributeBinding(ChunkShaderBindingPoints.TEX_COORD, vertexFormat.getAttribute(ChunkMeshAttribute.TEXTURE)),
						new GlVertexAttributeBinding(ChunkShaderBindingPoints.LIGHT_COORD, vertexFormat.getAttribute(ChunkMeshAttribute.LIGHT))
					},
					false
				)
			}
		);
		this.setupModelParts(meshData, vertexData.format);
		vertexData.buffer.limit(vertexData.buffer.capacity());
		vertexData.buffer.position(0);
		this.setTranslucencyData(vertexData.buffer);
	}
}
