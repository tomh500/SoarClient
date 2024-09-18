package com.soarclient.libraries.sodium.client.render.chunk.lists;

import java.util.Arrays;

public class ChunkRenderList<T> {
	private T[] stateArray;
	private int[] cullArray;
	private int size = 0;
	private int capacity;

	public ChunkRenderList() {
		this(1024);
	}

	public ChunkRenderList(int capacity) {
		this.capacity = capacity;
		this.stateArray = (T[])(new Object[capacity]);
		this.cullArray = new int[capacity];
	}

	private void resize() {
		this.capacity *= 2;
		this.stateArray = (T[])Arrays.copyOf(this.stateArray, this.capacity);
		this.cullArray = Arrays.copyOf(this.cullArray, this.capacity);
	}

	public void add(T state, int cull) {
		int idx = this.size++;
		if (idx >= this.capacity) {
			this.resize();
		}

		this.stateArray[idx] = state;
		this.cullArray[idx] = cull;
	}

	public void reset() {
		if (this.size != 0) {
			for (int i = 0; i < this.size; i++) {
				this.stateArray[i] = null;
			}

			for (int i = 0; i < this.size; i++) {
				this.cullArray[i] = 0;
			}

			this.size = 0;
		}
	}

	public ChunkRenderListIterator<T> iterator(boolean backwards) {
		return backwards ? new ChunkRenderListIterator<T>() {
			private int pos = ChunkRenderList.this.size - 1;

			@Override
			public T getGraphicsState() {
				return ChunkRenderList.this.stateArray[this.pos];
			}

			@Override
			public int getVisibleFaces() {
				return ChunkRenderList.this.cullArray[this.pos];
			}

			@Override
			public boolean hasNext() {
				return this.pos >= 0;
			}

			@Override
			public void advance() {
				this.pos--;
			}
		} : new ChunkRenderListIterator<T>() {
			private final int lim = ChunkRenderList.this.size;
			private int pos = 0;

			@Override
			public T getGraphicsState() {
				return ChunkRenderList.this.stateArray[this.pos];
			}

			@Override
			public int getVisibleFaces() {
				return ChunkRenderList.this.cullArray[this.pos];
			}

			@Override
			public boolean hasNext() {
				return this.pos < this.lim;
			}

			@Override
			public void advance() {
				this.pos++;
			}
		};
	}
}
