package com.soarclient.libraries.sodium.client.model.vertex.type;

import com.soarclient.libraries.sodium.client.model.vertex.VertexSink;

import net.minecraft.client.renderer.WorldRenderer;

public interface VertexType<T extends VertexSink> {
	T createFallbackWriter(WorldRenderer worldRenderer);

	default BlittableVertexType<T> asBlittable() {
		return null;
	}
}
