package com.soarclient.libraries.sodium.client.render.chunk.format.sfp;

import com.soarclient.libraries.sodium.client.gl.attribute.GlVertexAttributeFormat;
import com.soarclient.libraries.sodium.client.gl.attribute.GlVertexFormat;
import com.soarclient.libraries.sodium.client.model.vertex.buffer.VertexBufferView;
import com.soarclient.libraries.sodium.client.model.vertex.type.BlittableVertexType;
import com.soarclient.libraries.sodium.client.model.vertex.type.ChunkVertexType;
import com.soarclient.libraries.sodium.client.render.chunk.format.ChunkMeshAttribute;
import com.soarclient.libraries.sodium.client.render.chunk.format.ModelVertexSink;

import net.minecraft.client.renderer.WorldRenderer;

public class SFPModelVertexType implements ChunkVertexType {
	public static final GlVertexFormat<ChunkMeshAttribute> VERTEX_FORMAT = GlVertexFormat
			.builder(ChunkMeshAttribute.class, 32)
			.addElement(ChunkMeshAttribute.POSITION, 0, GlVertexAttributeFormat.FLOAT, 3, false)
			.addElement(ChunkMeshAttribute.COLOR, 12, GlVertexAttributeFormat.UNSIGNED_BYTE, 4, true)
			.addElement(ChunkMeshAttribute.TEXTURE, 16, GlVertexAttributeFormat.FLOAT, 2, false)
			.addElement(ChunkMeshAttribute.LIGHT, 24, GlVertexAttributeFormat.UNSIGNED_SHORT, 2, true).build();
	public static final float MODEL_SCALE = 1.0F;
	public static final float TEXTURE_SCALE = 1.0F;

	public ModelVertexSink createFallbackWriter(WorldRenderer consumer) {
		throw new UnsupportedOperationException();
	}

	public ModelVertexSink createBufferWriter(VertexBufferView buffer, boolean direct) {
		return (ModelVertexSink) (direct ? new SFPModelVertexBufferWriterUnsafe(buffer)
				: new SFPModelVertexBufferWriterNio(buffer));
	}

	@Override
	public BlittableVertexType<ModelVertexSink> asBlittable() {
		return this;
	}

	@Override
	public GlVertexFormat<ChunkMeshAttribute> getCustomVertexFormat() {
		return VERTEX_FORMAT;
	}

	@Override
	public float getModelScale() {
		return 1.0F;
	}

	@Override
	public float getTextureScale() {
		return 1.0F;
	}
}
