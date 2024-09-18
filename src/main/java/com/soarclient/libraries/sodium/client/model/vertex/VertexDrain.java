package com.soarclient.libraries.sodium.client.model.vertex;

import com.soarclient.libraries.sodium.client.model.vertex.type.VertexType;

import net.minecraft.client.renderer.WorldRenderer;

public interface VertexDrain {
	static VertexDrain of(WorldRenderer consumer) {
		return (VertexDrain)consumer;
	}

	<T extends VertexSink> T oldium$createSink(VertexType<T> vertexType);
}
