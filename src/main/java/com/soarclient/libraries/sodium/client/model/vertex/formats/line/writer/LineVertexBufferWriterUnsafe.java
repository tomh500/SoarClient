package com.soarclient.libraries.sodium.client.model.vertex.formats.line.writer;

import com.soarclient.libraries.sodium.client.model.vertex.VanillaVertexTypes;
import com.soarclient.libraries.sodium.client.model.vertex.buffer.VertexBufferView;
import com.soarclient.libraries.sodium.client.model.vertex.buffer.VertexBufferWriterUnsafe;
import com.soarclient.libraries.sodium.client.model.vertex.formats.line.LineVertexSink;
import com.soarclient.libraries.sodium.client.util.CompatMemoryUtil;

public class LineVertexBufferWriterUnsafe extends VertexBufferWriterUnsafe implements LineVertexSink {
	public LineVertexBufferWriterUnsafe(VertexBufferView backingBuffer) {
		super(backingBuffer, VanillaVertexTypes.LINES);
	}

	@Override
	public void vertexLine(float x, float y, float z, int color) {
		long i = this.writePointer;
		CompatMemoryUtil.memPutFloat(i, x);
		CompatMemoryUtil.memPutFloat(i + 4L, y);
		CompatMemoryUtil.memPutFloat(i + 8L, z);
		CompatMemoryUtil.memPutInt(i + 12L, color);
		this.advance();
	}
}
