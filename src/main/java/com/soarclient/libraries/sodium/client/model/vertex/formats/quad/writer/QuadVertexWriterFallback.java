package com.soarclient.libraries.sodium.client.model.vertex.formats.quad.writer;

import com.soarclient.libraries.sodium.client.model.vertex.fallback.VertexWriterFallback;
import com.soarclient.libraries.sodium.client.model.vertex.formats.quad.QuadVertexSink;
import com.soarclient.libraries.sodium.client.util.Norm3b;
import com.soarclient.libraries.sodium.client.util.color.ColorABGR;

import net.minecraft.client.renderer.WorldRenderer;

public class QuadVertexWriterFallback extends VertexWriterFallback implements QuadVertexSink {
	public QuadVertexWriterFallback(WorldRenderer consumer) {
		super(consumer);
	}

	@Override
	public void writeQuad(float x, float y, float z, int color, float u, float v, int light, int overlay, int normal) {
		WorldRenderer consumer = this.consumer;
		consumer.pos((double) x, (double) y, (double) z);
		consumer.color(ColorABGR.unpackRed(color), ColorABGR.unpackGreen(color), ColorABGR.unpackBlue(color),
				ColorABGR.unpackAlpha(color));
		consumer.tex((double) u, (double) v);
		consumer.lightmap(light, light);
		consumer.normal(Norm3b.unpackX(normal), Norm3b.unpackY(normal), Norm3b.unpackZ(normal));
		consumer.endVertex();
	}
}
