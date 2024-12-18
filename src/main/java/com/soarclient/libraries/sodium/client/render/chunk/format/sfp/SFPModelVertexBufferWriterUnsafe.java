package com.soarclient.libraries.sodium.client.render.chunk.format.sfp;

import com.soarclient.libraries.sodium.client.model.vertex.buffer.VertexBufferView;
import com.soarclient.libraries.sodium.client.model.vertex.buffer.VertexBufferWriterUnsafe;
import com.soarclient.libraries.sodium.client.render.chunk.format.DefaultModelVertexFormats;
import com.soarclient.libraries.sodium.client.render.chunk.format.ModelVertexSink;
import com.soarclient.libraries.sodium.client.util.CompatMemoryUtil;

public class SFPModelVertexBufferWriterUnsafe extends VertexBufferWriterUnsafe implements ModelVertexSink {
	public SFPModelVertexBufferWriterUnsafe(VertexBufferView backingBuffer) {
		super(backingBuffer, DefaultModelVertexFormats.MODEL_VERTEX_SFP);
	}

	@Override
	public void writeQuad(float x, float y, float z, int color, float u, float v, int light) {
		long i = this.writePointer;
		CompatMemoryUtil.memPutFloat(i, x);
		CompatMemoryUtil.memPutFloat(i + 4L, y);
		CompatMemoryUtil.memPutFloat(i + 8L, z);
		CompatMemoryUtil.memPutInt(i + 12L, color);
		CompatMemoryUtil.memPutFloat(i + 16L, u);
		CompatMemoryUtil.memPutFloat(i + 20L, v);
		CompatMemoryUtil.memPutInt(i + 24L, encodeLightMapTexCoord(light));
		this.advance();
	}

	private static int encodeLightMapTexCoord(int light) {
		int sl = light >> 16 & 0xFF;
		sl = (sl << 8) + 2048;
		int bl = light & 0xFF;
		bl = (bl << 8) + 2048;
		return sl << 16 | bl;
	}
}
