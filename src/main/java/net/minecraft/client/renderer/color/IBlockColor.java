package net.minecraft.client.renderer.color;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IBlockColor {
	int colorMultiplier(IBlockState iBlockState, IBlockAccess iBlockAccess, BlockPos blockPos, int integer);
}
