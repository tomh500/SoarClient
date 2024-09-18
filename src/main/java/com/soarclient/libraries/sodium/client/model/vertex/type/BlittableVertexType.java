package com.soarclient.libraries.sodium.client.model.vertex.type;

import com.soarclient.libraries.sodium.client.model.vertex.VertexSink;
import com.soarclient.libraries.sodium.client.model.vertex.buffer.VertexBufferView;

public interface BlittableVertexType<T extends VertexSink> extends BufferVertexType<T> {
	T createBufferWriter(VertexBufferView vertexBufferView, boolean boolean2);
}
