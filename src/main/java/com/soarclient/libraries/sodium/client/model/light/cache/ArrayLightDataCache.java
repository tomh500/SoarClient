package com.soarclient.libraries.sodium.client.model.light.cache;

import java.util.Arrays;

import com.soarclient.libraries.sodium.client.model.light.data.LightDataAccess;
import com.soarclient.libraries.sodium.client.util.math.ChunkSectionPos;
import com.soarclient.libraries.sodium.client.world.WorldSlice;

public class ArrayLightDataCache extends LightDataAccess {
	private static final int NEIGHBOR_BLOCK_RADIUS = 2;
	private static final int BLOCK_LENGTH = 20;
	private final long[] light;
	private int xOffset;
	private int yOffset;
	private int zOffset;

	public ArrayLightDataCache(WorldSlice world) {
		this.world = world;
		this.light = new long[8000];
	}

	public void reset(ChunkSectionPos origin) {
		this.xOffset = origin.getMinX() - 2;
		this.yOffset = origin.getMinY() - 2;
		this.zOffset = origin.getMinZ() - 2;
		Arrays.fill(this.light, 0L);
	}

	private int index(int x, int y, int z) {
		int x2 = x - this.xOffset;
		int y2 = y - this.yOffset;
		int z2 = z - this.zOffset;
		return z2 * 20 * 20 + y2 * 20 + x2;
	}

	@Override
	public long get(int x, int y, int z) {
		int l = this.index(x, y, z);
		long word = this.light[l];
		return word != 0L ? word : (this.light[l] = this.compute(x, y, z));
	}
}
