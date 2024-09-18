package com.soarclient.libs.sodium.client.model.vertex.formats.screen_quad.writer;

import com.soarclient.libs.sodium.client.model.vertex.fallback.VertexWriterFallback;
import com.soarclient.libs.sodium.client.model.vertex.formats.screen_quad.BasicScreenQuadVertexSink;
import com.soarclient.libs.sodium.client.util.color.ColorABGR;

import net.minecraft.client.renderer.WorldRenderer;

public class BasicScreenQuadVertexWriterFallback extends VertexWriterFallback implements BasicScreenQuadVertexSink {
	public BasicScreenQuadVertexWriterFallback(WorldRenderer consumer) {
		super(consumer);
	}

	@Override
	public void writeQuad(float x, float y, float z, int color) {
		WorldRenderer consumer = this.consumer;
		consumer.pos((double)x, (double)y, (double)z);
		consumer.color(ColorABGR.unpackRed(color), ColorABGR.unpackGreen(color), ColorABGR.unpackBlue(color), ColorABGR.unpackAlpha(color));
		consumer.endVertex();
	}
}
