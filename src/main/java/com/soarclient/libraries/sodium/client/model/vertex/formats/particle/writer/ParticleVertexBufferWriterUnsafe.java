package com.soarclient.libraries.sodium.client.model.vertex.formats.particle.writer;

import com.soarclient.libraries.sodium.client.model.vertex.VanillaVertexTypes;
import com.soarclient.libraries.sodium.client.model.vertex.buffer.VertexBufferView;
import com.soarclient.libraries.sodium.client.model.vertex.buffer.VertexBufferWriterUnsafe;
import com.soarclient.libraries.sodium.client.model.vertex.formats.particle.ParticleVertexSink;
import com.soarclient.libraries.sodium.client.util.CompatMemoryUtil;

public class ParticleVertexBufferWriterUnsafe extends VertexBufferWriterUnsafe implements ParticleVertexSink {
	public ParticleVertexBufferWriterUnsafe(VertexBufferView backingBuffer) {
		super(backingBuffer, VanillaVertexTypes.PARTICLES);
	}

	@Override
	public void writeParticle(float x, float y, float z, float u, float v, int color, int light) {
		long i = this.writePointer;
		CompatMemoryUtil.memPutFloat(i, x);
		CompatMemoryUtil.memPutFloat(i + 4L, y);
		CompatMemoryUtil.memPutFloat(i + 8L, z);
		CompatMemoryUtil.memPutFloat(i + 12L, u);
		CompatMemoryUtil.memPutFloat(i + 16L, v);
		CompatMemoryUtil.memPutInt(i + 20L, color);
		CompatMemoryUtil.memPutInt(i + 24L, light);
		this.advance();
	}
}
