package com.soarclient.libs.sodium.client.model.vertex.formats.screen_quad.writer;

import java.nio.ByteBuffer;

import com.soarclient.libs.sodium.client.model.vertex.VanillaVertexTypes;
import com.soarclient.libs.sodium.client.model.vertex.buffer.VertexBufferView;
import com.soarclient.libs.sodium.client.model.vertex.buffer.VertexBufferWriterNio;
import com.soarclient.libs.sodium.client.model.vertex.formats.screen_quad.BasicScreenQuadVertexSink;

public class BasicScreenQuadVertexBufferWriterNio extends VertexBufferWriterNio implements BasicScreenQuadVertexSink {
	public BasicScreenQuadVertexBufferWriterNio(VertexBufferView backingBuffer) {
		super(backingBuffer, VanillaVertexTypes.BASIC_SCREEN_QUADS);
	}

	@Override
	public void writeQuad(float x, float y, float z, int color) {
		int i = this.writeOffset;
		ByteBuffer buf = this.byteBuffer;
		buf.putFloat(i, x);
		buf.putFloat(i + 4, y);
		buf.putFloat(i + 8, z);
		buf.putInt(i + 12, color);
		this.advance();
	}
}
