package com.soarclient.libs.sodium.client.model.quad.blender;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface BlockColorSettings<T> {
	boolean oldium$useSmoothColorBlending(IBlockAccess iBlockAccess, T object, BlockPos blockPos);

	static boolean isSmoothBlendingEnabled(IBlockAccess world, IBlockState state, BlockPos pos) {
		if (state.getBlock() instanceof BlockColorSettings) {
			BlockColorSettings<IBlockState> settings = (BlockColorSettings<IBlockState>)state.getBlock();
			return settings.oldium$useSmoothColorBlending(world, state, pos);
		} else {
			return false;
		}
	}
}
