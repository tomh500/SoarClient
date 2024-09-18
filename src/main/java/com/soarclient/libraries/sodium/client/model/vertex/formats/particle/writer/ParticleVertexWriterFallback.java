package com.soarclient.libraries.sodium.client.model.vertex.formats.particle.writer;

import com.soarclient.libraries.sodium.client.model.vertex.fallback.VertexWriterFallback;
import com.soarclient.libraries.sodium.client.model.vertex.formats.particle.ParticleVertexSink;
import com.soarclient.libraries.sodium.client.util.color.ColorABGR;

import net.minecraft.client.renderer.WorldRenderer;

public class ParticleVertexWriterFallback extends VertexWriterFallback implements ParticleVertexSink {
	public ParticleVertexWriterFallback(WorldRenderer consumer) {
		super(consumer);
	}

	@Override
	public void writeParticle(float x, float y, float z, float u, float v, int color, int light) {
		WorldRenderer consumer = this.consumer;
		consumer.pos((double)x, (double)y, (double)z);
		consumer.tex((double)u, (double)v);
		consumer.color(ColorABGR.unpackRed(color), ColorABGR.unpackGreen(color), ColorABGR.unpackBlue(color), ColorABGR.unpackAlpha(color));
		consumer.lightmap(light, light);
		consumer.endVertex();
	}
}
