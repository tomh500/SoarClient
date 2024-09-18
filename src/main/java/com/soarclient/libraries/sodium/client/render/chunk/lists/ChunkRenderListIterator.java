package com.soarclient.libraries.sodium.client.render.chunk.lists;

public interface ChunkRenderListIterator<T> {
	T getGraphicsState();

	int getVisibleFaces();

	boolean hasNext();

	void advance();
}
