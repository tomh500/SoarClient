package com.soarclient.libraries.sodium.client.model.vertex.formats.line;

import com.soarclient.libraries.sodium.client.model.vertex.buffer.VertexBufferView;
import com.soarclient.libraries.sodium.client.model.vertex.formats.line.writer.LineVertexBufferWriterNio;
import com.soarclient.libraries.sodium.client.model.vertex.formats.line.writer.LineVertexBufferWriterUnsafe;
import com.soarclient.libraries.sodium.client.model.vertex.formats.line.writer.LineVertexWriterFallback;
import com.soarclient.libraries.sodium.client.model.vertex.type.BlittableVertexType;
import com.soarclient.libraries.sodium.client.model.vertex.type.VanillaVertexType;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.VertexFormat;

public class LineVertexType implements VanillaVertexType<LineVertexSink>, BlittableVertexType<LineVertexSink> {
	public LineVertexSink createBufferWriter(VertexBufferView buffer, boolean direct) {
		return (LineVertexSink) (direct ? new LineVertexBufferWriterUnsafe(buffer)
				: new LineVertexBufferWriterNio(buffer));
	}

	public LineVertexSink createFallbackWriter(WorldRenderer consumer) {
		return new LineVertexWriterFallback(consumer);
	}

	@Override
	public VertexFormat getVertexFormat() {
		return LineVertexSink.VERTEX_FORMAT;
	}

	@Override
	public BlittableVertexType<LineVertexSink> asBlittable() {
		return this;
	}
}
