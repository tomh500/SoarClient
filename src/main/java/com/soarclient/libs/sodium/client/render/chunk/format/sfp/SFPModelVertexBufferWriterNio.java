package com.soarclient.libs.sodium.client.render.chunk.format.sfp;

import java.nio.ByteBuffer;

import com.soarclient.libs.sodium.client.model.vertex.buffer.VertexBufferView;
import com.soarclient.libs.sodium.client.model.vertex.buffer.VertexBufferWriterNio;
import com.soarclient.libs.sodium.client.render.chunk.format.DefaultModelVertexFormats;
import com.soarclient.libs.sodium.client.render.chunk.format.ModelVertexSink;
import com.soarclient.libs.sodium.client.render.chunk.format.ModelVertexUtil;

public class SFPModelVertexBufferWriterNio extends VertexBufferWriterNio implements ModelVertexSink {
	public SFPModelVertexBufferWriterNio(VertexBufferView backingBuffer) {
		super(backingBuffer, DefaultModelVertexFormats.MODEL_VERTEX_SFP);
	}

	@Override
	public void writeQuad(float x, float y, float z, int color, float u, float v, int light) {
		int i = this.writeOffset;
		ByteBuffer buffer = this.byteBuffer;
		buffer.putFloat(i, x);
		buffer.putFloat(i + 4, y);
		buffer.putFloat(i + 8, z);
		buffer.putInt(i + 12, color);
		buffer.putFloat(i + 16, u);
		buffer.putFloat(i + 20, v);
		buffer.putInt(i + 24, ModelVertexUtil.encodeLightMapTexCoord(light));
		this.advance();
	}
}
