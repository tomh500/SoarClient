package com.soarclient.libs.sodium.client.model.vertex.formats.screen_quad;

import com.soarclient.libs.sodium.client.model.vertex.buffer.VertexBufferView;
import com.soarclient.libs.sodium.client.model.vertex.formats.screen_quad.writer.BasicScreenQuadVertexBufferWriterNio;
import com.soarclient.libs.sodium.client.model.vertex.formats.screen_quad.writer.BasicScreenQuadVertexBufferWriterUnsafe;
import com.soarclient.libs.sodium.client.model.vertex.formats.screen_quad.writer.BasicScreenQuadVertexWriterFallback;
import com.soarclient.libs.sodium.client.model.vertex.type.BlittableVertexType;
import com.soarclient.libs.sodium.client.model.vertex.type.VanillaVertexType;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.VertexFormat;

public class BasicScreenQuadVertexType implements VanillaVertexType<BasicScreenQuadVertexSink>, BlittableVertexType<BasicScreenQuadVertexSink> {
	public BasicScreenQuadVertexSink createFallbackWriter(WorldRenderer consumer) {
		return new BasicScreenQuadVertexWriterFallback(consumer);
	}

	public BasicScreenQuadVertexSink createBufferWriter(VertexBufferView buffer, boolean direct) {
		return (BasicScreenQuadVertexSink)(direct ? new BasicScreenQuadVertexBufferWriterUnsafe(buffer) : new BasicScreenQuadVertexBufferWriterNio(buffer));
	}

	@Override
	public VertexFormat getVertexFormat() {
		return BasicScreenQuadVertexSink.VERTEX_FORMAT;
	}

	@Override
	public BlittableVertexType<BasicScreenQuadVertexSink> asBlittable() {
		return this;
	}
}
