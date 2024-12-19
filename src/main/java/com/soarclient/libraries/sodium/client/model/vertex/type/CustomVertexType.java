package com.soarclient.libraries.sodium.client.model.vertex.type;

import com.soarclient.libraries.sodium.client.gl.attribute.BufferVertexFormat;
import com.soarclient.libraries.sodium.client.gl.attribute.GlVertexFormat;
import com.soarclient.libraries.sodium.client.model.vertex.VertexSink;

public interface CustomVertexType<T extends VertexSink, A extends Enum<A>> extends BufferVertexType<T> {
	GlVertexFormat<A> getCustomVertexFormat();

	@Override
	default BufferVertexFormat getBufferVertexFormat() {
		return this.getCustomVertexFormat();
	}
}
