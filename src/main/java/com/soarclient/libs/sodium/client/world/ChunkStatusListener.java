package com.soarclient.libs.sodium.client.world;

public interface ChunkStatusListener {
	void onChunkAdded(int integer1, int integer2);

	void onChunkRemoved(int integer1, int integer2);
}
