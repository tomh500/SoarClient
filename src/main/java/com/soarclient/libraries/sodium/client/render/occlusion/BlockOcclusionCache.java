package com.soarclient.libraries.sodium.client.render.occlusion;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos.MutableBlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockOcclusionCache {
	private final MutableBlockPos cpos = new MutableBlockPos(0, 0, 0);

	public boolean shouldDrawSide(IBlockAccess view, BlockPos pos, EnumFacing facing) {
		MutableBlockPos adjPos = this.cpos;
		adjPos.set(pos.getX() + facing.getFrontOffsetX(), pos.getY() + facing.getFrontOffsetY(),
				pos.getZ() + facing.getFrontOffsetZ());
		Block self = view.getBlockState(pos).getBlock();
		return self.shouldSideBeRendered(view, adjPos, facing);
	}
}
