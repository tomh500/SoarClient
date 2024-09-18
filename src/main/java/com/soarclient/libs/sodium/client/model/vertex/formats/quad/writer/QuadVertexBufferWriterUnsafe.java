package com.soarclient.libs.sodium.client.model.vertex.formats.quad.writer;

import com.soarclient.libs.sodium.client.model.vertex.VanillaVertexTypes;
import com.soarclient.libs.sodium.client.model.vertex.buffer.VertexBufferView;
import com.soarclient.libs.sodium.client.model.vertex.buffer.VertexBufferWriterUnsafe;
import com.soarclient.libs.sodium.client.model.vertex.formats.quad.QuadVertexSink;
import com.soarclient.libs.sodium.client.util.CompatMemoryUtil;

public class QuadVertexBufferWriterUnsafe extends VertexBufferWriterUnsafe implements QuadVertexSink {
	public QuadVertexBufferWriterUnsafe(VertexBufferView backingBuffer) {
		super(backingBuffer, VanillaVertexTypes.QUADS);
	}

	@Override
	public void writeQuad(float x, float y, float z, int color, float u, float v, int light, int overlay, int normal) {
		long i = this.writePointer;
		CompatMemoryUtil.memPutFloat(i, x);
		CompatMemoryUtil.memPutFloat(i + 4L, y);
		CompatMemoryUtil.memPutFloat(i + 8L, z);
		CompatMemoryUtil.memPutInt(i + 12L, color);
		CompatMemoryUtil.memPutFloat(i + 16L, u);
		CompatMemoryUtil.memPutFloat(i + 20L, v);
		CompatMemoryUtil.memPutInt(i + 24L, overlay);
		CompatMemoryUtil.memPutInt(i + 28L, light);
		CompatMemoryUtil.memPutInt(i + 32L, normal);
		this.advance();
	}
}
