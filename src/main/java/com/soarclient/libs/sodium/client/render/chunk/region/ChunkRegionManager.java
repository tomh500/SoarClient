package com.soarclient.libs.sodium.client.render.chunk.region;

import it.unimi.dsi.fastutil.longs.Long2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import org.apache.commons.lang3.Validate;

import com.soarclient.libs.sodium.client.gl.device.RenderDevice;
import com.soarclient.libs.sodium.client.render.chunk.ChunkGraphicsState;
import com.soarclient.libs.sodium.client.util.MathUtil;
import com.soarclient.libs.sodium.client.util.math.ChunkSectionPos;

public class ChunkRegionManager<T extends ChunkGraphicsState> {
	public static final int BUFFER_WIDTH = 8;
	public static final int BUFFER_HEIGHT = 4;
	public static final int BUFFER_LENGTH = 8;
	public static final int BUFFER_SIZE = 256;
	private static final int BUFFER_WIDTH_SH = Integer.bitCount(7);
	private static final int BUFFER_HEIGHT_SH = Integer.bitCount(3);
	private static final int BUFFER_LENGTH_SH = Integer.bitCount(7);
	private final Long2ReferenceOpenHashMap<ChunkRegion<T>> regions = new Long2ReferenceOpenHashMap<>();
	private final RenderDevice device;

	public ChunkRegionManager(RenderDevice device) {
		this.device = device;
	}

	public ChunkRegion<T> getRegion(int x, int y, int z) {
		return this.regions.get(getRegionKey(x, y, z));
	}

	public ChunkRegion<T> getOrCreateRegion(int x, int y, int z) {
		long key = getRegionKey(x, y, z);
		ChunkRegion<T> region = this.regions.get(key);
		if (region == null) {
			this.regions.put(key, region = new ChunkRegion<>(this.device, 256, x >> BUFFER_WIDTH_SH, y >> BUFFER_HEIGHT_SH, z >> BUFFER_LENGTH_SH));
		}

		return region;
	}

	public static long getRegionKey(int x, int y, int z) {
		return ChunkSectionPos.asLong(x >> BUFFER_WIDTH_SH, y >> BUFFER_HEIGHT_SH, z >> BUFFER_LENGTH_SH);
	}

	public void delete() {
		for (ChunkRegion<T> region : this.regions.values()) {
			region.deleteResources();
		}

		this.regions.clear();
	}

	public void cleanup() {
		ObjectIterator<ChunkRegion<T>> iterator = this.regions.values().iterator();

		while (iterator.hasNext()) {
			ChunkRegion<T> region = (ChunkRegion<T>)iterator.next();
			if (region.isArenaEmpty()) {
				region.deleteResources();
				iterator.remove();
			}
		}
	}

	public int getAllocatedRegionCount() {
		return this.regions.size();
	}

	static {
		Validate.isTrue(MathUtil.isPowerOfTwo(8));
		Validate.isTrue(MathUtil.isPowerOfTwo(8));
		Validate.isTrue(MathUtil.isPowerOfTwo(4));
	}
}
