package com.soarclient.libs.sodium.client.model.vertex.fallback;

import com.soarclient.libs.sodium.client.model.vertex.VertexSink;

import net.minecraft.client.renderer.WorldRenderer;

public abstract class VertexWriterFallback implements VertexSink {
	protected final WorldRenderer consumer;

	protected VertexWriterFallback(WorldRenderer consumer) {
		this.consumer = consumer;
	}

	@Override
	public void ensureCapacity(int count) {
	}

	@Override
	public void flush() {
	}
}
