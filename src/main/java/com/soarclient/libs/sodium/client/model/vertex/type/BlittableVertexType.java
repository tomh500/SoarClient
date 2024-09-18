package com.soarclient.libs.sodium.client.model.vertex.type;

import com.soarclient.libs.sodium.client.model.vertex.VertexSink;
import com.soarclient.libs.sodium.client.model.vertex.buffer.VertexBufferView;

public interface BlittableVertexType<T extends VertexSink> extends BufferVertexType<T> {
	T createBufferWriter(VertexBufferView vertexBufferView, boolean boolean2);
}
