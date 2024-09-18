package com.soarclient.libs.sodium.client.world;

import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper.ColorResolver;

public interface SodiumBlockAccess extends IBlockAccess {
	int getBlockTint(BlockPos blockPos, ColorResolver colorResolver);
}
