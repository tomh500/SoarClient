package com.soarclient.libraries.sodium.client.model.vertex;

public interface VertexSink {
	void ensureCapacity(int integer);

	void flush();
}
