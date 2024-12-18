package com.soarclient.libraries.sodium.client.model.vertex.formats.glyph.writer;

import com.soarclient.libraries.sodium.client.model.vertex.fallback.VertexWriterFallback;
import com.soarclient.libraries.sodium.client.model.vertex.formats.glyph.GlyphVertexSink;
import com.soarclient.libraries.sodium.client.util.color.ColorABGR;

import net.minecraft.client.renderer.WorldRenderer;

public class GlyphVertexWriterFallback extends VertexWriterFallback implements GlyphVertexSink {
	public GlyphVertexWriterFallback(WorldRenderer consumer) {
		super(consumer);
	}

	@Override
	public void writeGlyph(float x, float y, float z, int color, float u, float v, int light) {
		WorldRenderer consumer = this.consumer;
		int red = ColorABGR.unpackRed(color);
		int green = ColorABGR.unpackGreen(color);
		int blue = ColorABGR.unpackBlue(color);
		int alpha = ColorABGR.unpackAlpha(color);
		consumer.pos((double) x, (double) y, (double) z);
		consumer.color(red, green, blue, alpha);
		consumer.tex((double) u, (double) v);
		consumer.lightmap(light & 65535, light >> 16 & 65535);
		consumer.endVertex();
	}
}
