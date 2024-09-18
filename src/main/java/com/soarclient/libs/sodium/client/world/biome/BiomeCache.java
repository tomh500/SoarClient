package com.soarclient.libs.sodium.client.world.biome;

import java.util.Arrays;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeCache {
	private final World world;
	private final BiomeGenBase[] biomes;

	public BiomeCache(World world) {
		this.world = world;
		this.biomes = new BiomeGenBase[256];
	}

	public BiomeGenBase getBiome(int x, int y, int z) {
		int idx = (z & 15) << 4 | x & 15;
		BiomeGenBase biome = this.biomes[idx];
		if (biome == null) {
			this.biomes[idx] = biome = this.world.getBiomeGenForCoords(new BlockPos(x, y, z));
		}

		return biome;
	}

	public void reset() {
		Arrays.fill(this.biomes, null);
	}
}
