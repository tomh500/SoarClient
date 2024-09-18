package com.soarclient.libs.sodium.common.util.collections;

import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;

public class FutureDequeDrain<T> implements Iterator<T> {
	private final Deque<CompletableFuture<T>> deque;
	private T next = (T)null;

	public FutureDequeDrain(Deque<CompletableFuture<T>> deque) {
		this.deque = deque;
	}

	public boolean hasNext() {
		if (this.next != null) {
			return true;
		} else {
			this.findNext();
			return this.next != null;
		}
	}

	private void findNext() {
		while (!this.deque.isEmpty()) {
			CompletableFuture<T> future = (CompletableFuture<T>)this.deque.remove();

			try {
				this.next = (T)future.join();
				return;
			} catch (CancellationException var3) {
			}
		}
	}

	public T next() {
		if (!this.hasNext()) {
			throw new NoSuchElementException();
		} else {
			T result = this.next;
			this.next = null;
			return result;
		}
	}
}
