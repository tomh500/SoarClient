package com.soarclient.libraries.sodium.client.render.chunk.format.hfp;

import com.soarclient.libraries.sodium.client.model.vertex.buffer.VertexBufferView;
import com.soarclient.libraries.sodium.client.model.vertex.buffer.VertexBufferWriterUnsafe;
import com.soarclient.libraries.sodium.client.render.chunk.format.DefaultModelVertexFormats;
import com.soarclient.libraries.sodium.client.render.chunk.format.ModelVertexSink;
import com.soarclient.libraries.sodium.client.render.chunk.format.ModelVertexUtil;
import com.soarclient.libraries.sodium.client.util.CompatMemoryUtil;

public class HFPModelVertexBufferWriterUnsafe extends VertexBufferWriterUnsafe implements ModelVertexSink {
	public HFPModelVertexBufferWriterUnsafe(VertexBufferView backingBuffer) {
		super(backingBuffer, DefaultModelVertexFormats.MODEL_VERTEX_HFP);
	}

	@Override
	public void writeQuad(float x, float y, float z, int color, float u, float v, int light) {
		this.writeQuadInternal(ModelVertexUtil.denormalizeVertexPositionFloatAsShort(x),
				ModelVertexUtil.denormalizeVertexPositionFloatAsShort(y),
				ModelVertexUtil.denormalizeVertexPositionFloatAsShort(z), color,
				ModelVertexUtil.denormalizeVertexTextureFloatAsShort(u),
				ModelVertexUtil.denormalizeVertexTextureFloatAsShort(v), ModelVertexUtil.encodeLightMapTexCoord(light));
	}

	private void writeQuadInternal(short x, short y, short z, int color, short u, short v, int light) {
		long i = this.writePointer;
		CompatMemoryUtil.memPutShort(i, x);
		CompatMemoryUtil.memPutShort(i + 2L, y);
		CompatMemoryUtil.memPutShort(i + 4L, z);
		CompatMemoryUtil.memPutInt(i + 8L, color);
		CompatMemoryUtil.memPutShort(i + 12L, u);
		CompatMemoryUtil.memPutShort(i + 14L, v);
		CompatMemoryUtil.memPutInt(i + 16L, light);
		this.advance();
	}
}
