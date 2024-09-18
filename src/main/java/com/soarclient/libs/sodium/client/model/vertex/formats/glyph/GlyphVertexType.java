package com.soarclient.libs.sodium.client.model.vertex.formats.glyph;

import com.soarclient.libs.sodium.client.model.vertex.buffer.VertexBufferView;
import com.soarclient.libs.sodium.client.model.vertex.formats.glyph.writer.GlyphVertexBufferWriterNio;
import com.soarclient.libs.sodium.client.model.vertex.formats.glyph.writer.GlyphVertexBufferWriterUnsafe;
import com.soarclient.libs.sodium.client.model.vertex.formats.glyph.writer.GlyphVertexWriterFallback;
import com.soarclient.libs.sodium.client.model.vertex.type.BlittableVertexType;
import com.soarclient.libs.sodium.client.model.vertex.type.VanillaVertexType;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.VertexFormat;

public class GlyphVertexType implements VanillaVertexType<GlyphVertexSink>, BlittableVertexType<GlyphVertexSink> {
	public GlyphVertexSink createBufferWriter(VertexBufferView buffer, boolean direct) {
		return (GlyphVertexSink)(direct ? new GlyphVertexBufferWriterUnsafe(buffer) : new GlyphVertexBufferWriterNio(buffer));
	}

	public GlyphVertexSink createFallbackWriter(WorldRenderer consumer) {
		return new GlyphVertexWriterFallback(consumer);
	}

	@Override
	public VertexFormat getVertexFormat() {
		return GlyphVertexSink.VERTEX_FORMAT;
	}

	@Override
	public BlittableVertexType<GlyphVertexSink> asBlittable() {
		return this;
	}
}
