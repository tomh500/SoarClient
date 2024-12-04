package com.soarclient.libraries.sodium.client.render.chunk.format.hfp;

import java.nio.ByteBuffer;

import com.soarclient.libraries.sodium.client.model.vertex.buffer.VertexBufferView;
import com.soarclient.libraries.sodium.client.model.vertex.buffer.VertexBufferWriterNio;
import com.soarclient.libraries.sodium.client.render.chunk.format.DefaultModelVertexFormats;
import com.soarclient.libraries.sodium.client.render.chunk.format.ModelVertexSink;
import com.soarclient.libraries.sodium.client.render.chunk.format.ModelVertexUtil;

public class HFPModelVertexBufferWriterNio extends VertexBufferWriterNio implements ModelVertexSink {
	public HFPModelVertexBufferWriterNio(VertexBufferView backingBuffer) {
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
		int i = this.writeOffset;
		ByteBuffer buffer = this.byteBuffer;
		buffer.putShort(i, x);
		buffer.putShort(i + 2, y);
		buffer.putShort(i + 4, z);
		buffer.putInt(i + 8, color);
		buffer.putShort(i + 12, u);
		buffer.putShort(i + 14, v);
		buffer.putInt(i + 16, light);
		this.advance();
	}
}
