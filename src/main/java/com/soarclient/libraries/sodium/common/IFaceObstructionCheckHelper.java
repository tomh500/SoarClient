package com.soarclient.libraries.sodium.common;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public interface IFaceObstructionCheckHelper {
	boolean oldium$isFaceNonObstructing(IBlockAccess iBlockAccess, BlockPos blockPos, EnumFacing enumFacing,
			double double4, double double5, double double6, double double7, double double8, double double9);
}
