package com.soarclient.libraries.sodium.client.model.vertex.formats.screen_quad.writer;

import com.soarclient.libraries.sodium.client.model.vertex.VanillaVertexTypes;
import com.soarclient.libraries.sodium.client.model.vertex.buffer.VertexBufferView;
import com.soarclient.libraries.sodium.client.model.vertex.buffer.VertexBufferWriterUnsafe;
import com.soarclient.libraries.sodium.client.model.vertex.formats.screen_quad.BasicScreenQuadVertexSink;
import com.soarclient.libraries.sodium.client.util.CompatMemoryUtil;

public class BasicScreenQuadVertexBufferWriterUnsafe extends VertexBufferWriterUnsafe implements BasicScreenQuadVertexSink {
	public BasicScreenQuadVertexBufferWriterUnsafe(VertexBufferView backingBuffer) {
		super(backingBuffer, VanillaVertexTypes.BASIC_SCREEN_QUADS);
	}

	@Override
	public void writeQuad(float x, float y, float z, int color) {
		long i = this.writePointer;
		CompatMemoryUtil.memPutFloat(i, x);
		CompatMemoryUtil.memPutFloat(i + 4L, y);
		CompatMemoryUtil.memPutFloat(i + 8L, z);
		CompatMemoryUtil.memPutInt(i + 12L, color);
		this.advance();
	}
}
