package com.soarclient.libs.sodium.client.model.vertex.type;

import com.soarclient.libs.sodium.client.gl.attribute.BufferVertexFormat;
import com.soarclient.libs.sodium.client.model.vertex.VertexSink;

public interface BufferVertexType<T extends VertexSink> extends VertexType<T> {
	BufferVertexFormat getBufferVertexFormat();
}
