package com.soarclient.libs.sodium.client.model.vertex.formats.line.writer;

import java.nio.ByteBuffer;

import com.soarclient.libs.sodium.client.model.vertex.VanillaVertexTypes;
import com.soarclient.libs.sodium.client.model.vertex.buffer.VertexBufferView;
import com.soarclient.libs.sodium.client.model.vertex.buffer.VertexBufferWriterNio;
import com.soarclient.libs.sodium.client.model.vertex.formats.line.LineVertexSink;

public class LineVertexBufferWriterNio extends VertexBufferWriterNio implements LineVertexSink {
	public LineVertexBufferWriterNio(VertexBufferView backingBuffer) {
		super(backingBuffer, VanillaVertexTypes.LINES);
	}

	@Override
	public void vertexLine(float x, float y, float z, int color) {
		int i = this.writeOffset;
		ByteBuffer buffer = this.byteBuffer;
		buffer.putFloat(i, x);
		buffer.putFloat(i + 4, y);
		buffer.putFloat(i + 8, z);
		buffer.putInt(i + 12, color);
		this.advance();
	}
}
