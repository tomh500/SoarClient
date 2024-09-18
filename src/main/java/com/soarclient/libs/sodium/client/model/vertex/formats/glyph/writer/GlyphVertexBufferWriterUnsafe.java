package com.soarclient.libs.sodium.client.model.vertex.formats.glyph.writer;

import com.soarclient.libs.sodium.client.model.vertex.VanillaVertexTypes;
import com.soarclient.libs.sodium.client.model.vertex.buffer.VertexBufferView;
import com.soarclient.libs.sodium.client.model.vertex.buffer.VertexBufferWriterUnsafe;
import com.soarclient.libs.sodium.client.model.vertex.formats.glyph.GlyphVertexSink;
import com.soarclient.libs.sodium.client.util.CompatMemoryUtil;

public class GlyphVertexBufferWriterUnsafe extends VertexBufferWriterUnsafe implements GlyphVertexSink {
	public GlyphVertexBufferWriterUnsafe(VertexBufferView backingBuffer) {
		super(backingBuffer, VanillaVertexTypes.GLYPHS);
	}

	@Override
	public void writeGlyph(float x, float y, float z, int color, float u, float v, int light) {
		long i = this.writePointer;
		CompatMemoryUtil.memPutFloat(i, x);
		CompatMemoryUtil.memPutFloat(i + 4L, y);
		CompatMemoryUtil.memPutFloat(i + 8L, z);
		CompatMemoryUtil.memPutInt(i + 12L, color);
		CompatMemoryUtil.memPutFloat(i + 16L, u);
		CompatMemoryUtil.memPutFloat(i + 20L, v);
		CompatMemoryUtil.memPutInt(i + 24L, light);
		this.advance();
	}
}
