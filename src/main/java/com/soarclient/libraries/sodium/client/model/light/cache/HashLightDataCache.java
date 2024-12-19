package com.soarclient.libraries.sodium.client.model.light.cache;

import com.soarclient.libraries.sodium.client.model.light.data.LightDataAccess;
import com.soarclient.libraries.sodium.client.world.WorldSlice;

import it.unimi.dsi.fastutil.longs.Long2LongLinkedOpenHashMap;
import net.minecraft.util.BlockPos;

public class HashLightDataCache extends LightDataAccess {
	private final Long2LongLinkedOpenHashMap map = new Long2LongLinkedOpenHashMap(1024, 0.5F);

	public HashLightDataCache(WorldSlice world) {
		this.world = world;
	}

	@Override
	public long get(int x, int y, int z) {
		long key = new BlockPos(x, y, z).toLong();
		long word = this.map.getAndMoveToFirst(key);
		if (word == 0L) {
			if (this.map.size() > 1024) {
				this.map.removeLastLong();
			}

			this.map.put(key, word = this.compute(x, y, z));
		}

		return word;
	}

	public void clearCache() {
		this.map.clear();
	}
}
