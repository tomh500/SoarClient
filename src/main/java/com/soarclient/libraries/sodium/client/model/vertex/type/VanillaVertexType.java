package com.soarclient.libraries.sodium.client.model.vertex.type;

import com.soarclient.libraries.sodium.client.gl.attribute.BufferVertexFormat;
import com.soarclient.libraries.sodium.client.model.vertex.VertexSink;

import net.minecraft.client.renderer.vertex.VertexFormat;

public interface VanillaVertexType<T extends VertexSink> extends BufferVertexType<T> {
	@Override
	default BufferVertexFormat getBufferVertexFormat() {
		return BufferVertexFormat.from(this.getVertexFormat());
	}

	VertexFormat getVertexFormat();
}
