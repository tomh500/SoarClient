package com.soarclient.libs.sodium.client.model.vertex.formats.particle;

import com.soarclient.libs.sodium.client.model.vertex.buffer.VertexBufferView;
import com.soarclient.libs.sodium.client.model.vertex.formats.particle.writer.ParticleVertexBufferWriterNio;
import com.soarclient.libs.sodium.client.model.vertex.formats.particle.writer.ParticleVertexBufferWriterUnsafe;
import com.soarclient.libs.sodium.client.model.vertex.formats.particle.writer.ParticleVertexWriterFallback;
import com.soarclient.libs.sodium.client.model.vertex.type.BlittableVertexType;
import com.soarclient.libs.sodium.client.model.vertex.type.VanillaVertexType;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.VertexFormat;

public class ParticleVertexType implements VanillaVertexType<ParticleVertexSink>, BlittableVertexType<ParticleVertexSink> {
	public ParticleVertexSink createBufferWriter(VertexBufferView buffer, boolean direct) {
		return (ParticleVertexSink)(direct ? new ParticleVertexBufferWriterUnsafe(buffer) : new ParticleVertexBufferWriterNio(buffer));
	}

	public ParticleVertexSink createFallbackWriter(WorldRenderer consumer) {
		return new ParticleVertexWriterFallback(consumer);
	}

	@Override
	public BlittableVertexType<ParticleVertexSink> asBlittable() {
		return this;
	}

	@Override
	public VertexFormat getVertexFormat() {
		return ParticleVertexSink.VERTEX_FORMAT;
	}
}
