package com.soarclient.libraries.sodium.common.util.collections;

import java.util.Deque;
import java.util.Iterator;

public class DequeDrain<T> implements Iterator<T> {
	private final Deque<T> deque;

	public DequeDrain(Deque<T> deque) {
		this.deque = deque;
	}

	public boolean hasNext() {
		return !this.deque.isEmpty();
	}

	public T next() {
		return (T) this.deque.remove();
	}
}
