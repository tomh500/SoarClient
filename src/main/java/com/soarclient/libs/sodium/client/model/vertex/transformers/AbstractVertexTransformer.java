package com.soarclient.libs.sodium.client.model.vertex.transformers;

import com.soarclient.libs.sodium.client.model.vertex.VertexSink;

public abstract class AbstractVertexTransformer<T extends VertexSink> implements VertexSink {
	protected final T delegate;

	protected AbstractVertexTransformer(T delegate) {
		this.delegate = delegate;
	}

	@Override
	public void ensureCapacity(int count) {
		this.delegate.ensureCapacity(count);
	}

	@Override
	public void flush() {
		this.delegate.flush();
	}
}
