package com.soarclient.libs.sodium.client.render.chunk.format.hfp;

import com.soarclient.libs.sodium.client.gl.attribute.GlVertexAttributeFormat;
import com.soarclient.libs.sodium.client.gl.attribute.GlVertexFormat;
import com.soarclient.libs.sodium.client.model.vertex.buffer.VertexBufferView;
import com.soarclient.libs.sodium.client.model.vertex.type.BlittableVertexType;
import com.soarclient.libs.sodium.client.model.vertex.type.ChunkVertexType;
import com.soarclient.libs.sodium.client.render.chunk.format.ChunkMeshAttribute;
import com.soarclient.libs.sodium.client.render.chunk.format.ModelVertexSink;

import net.minecraft.client.renderer.WorldRenderer;

public class HFPModelVertexType implements ChunkVertexType {
	public static final GlVertexFormat<ChunkMeshAttribute> VERTEX_FORMAT = GlVertexFormat.builder(ChunkMeshAttribute.class, 20)
		.addElement(ChunkMeshAttribute.POSITION, 0, GlVertexAttributeFormat.UNSIGNED_SHORT, 3, false)
		.addElement(ChunkMeshAttribute.COLOR, 8, GlVertexAttributeFormat.UNSIGNED_BYTE, 4, true)
		.addElement(ChunkMeshAttribute.TEXTURE, 12, GlVertexAttributeFormat.UNSIGNED_SHORT, 2, false)
		.addElement(ChunkMeshAttribute.LIGHT, 16, GlVertexAttributeFormat.UNSIGNED_SHORT, 2, true)
		.build();
	public static final float MODEL_SCALE = 4.8828125E-4F;
	public static final float TEXTURE_SCALE = 3.0517578E-5F;

	public ModelVertexSink createFallbackWriter(WorldRenderer consumer) {
		throw new UnsupportedOperationException();
	}

	public ModelVertexSink createBufferWriter(VertexBufferView buffer, boolean direct) {
		return (ModelVertexSink)(direct ? new HFPModelVertexBufferWriterUnsafe(buffer) : new HFPModelVertexBufferWriterNio(buffer));
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
		return 4.8828125E-4F;
	}

	@Override
	public float getTextureScale() {
		return 3.0517578E-5F;
	}
}
