package com.soarclient.libraries.sodium.client.model.vertex.type;

import com.soarclient.libraries.sodium.client.gl.attribute.BufferVertexFormat;
import com.soarclient.libraries.sodium.client.model.vertex.VertexSink;

public interface BufferVertexType<T extends VertexSink> extends VertexType<T> {
	BufferVertexFormat getBufferVertexFormat();
}
