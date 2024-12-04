package com.soarclient.libraries.sodium.client.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeColorHelper.ColorResolver;
import org.jetbrains.annotations.Nullable;

public class WorldSliceLocal implements SodiumBlockAccess {
	private final SodiumBlockAccess view;

	public WorldSliceLocal(SodiumBlockAccess view) {
		this.view = view;
	}

	@Nullable
	public TileEntity getTileEntity(BlockPos pos) {
		return this.view.getTileEntity(pos);
	}

	public int getCombinedLight(BlockPos pos, int lightValue) {
		return this.view.getCombinedLight(pos, lightValue);
	}

	public IBlockState getBlockState(BlockPos pos) {
		return this.view.getBlockState(pos);
	}

	public boolean isAirBlock(BlockPos pos) {
		return this.view.isAirBlock(pos);
	}

	public BiomeGenBase getBiomeGenForCoords(BlockPos pos) {
		return this.view.getBiomeGenForCoords(pos);
	}

	public boolean extendedLevelsInChunkCache() {
		return this.view.extendedLevelsInChunkCache();
	}

	public int getStrongPower(BlockPos pos, EnumFacing direction) {
		return this.view.getStrongPower(pos, direction);
	}

	public WorldType getWorldType() {
		return this.view.getWorldType();
	}

	@Override
	public int getBlockTint(BlockPos pos, ColorResolver resolver) {
		return this.view.getBlockTint(pos, resolver);
	}
}
