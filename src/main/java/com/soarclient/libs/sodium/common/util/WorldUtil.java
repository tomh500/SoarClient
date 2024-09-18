package com.soarclient.libs.sodium.common.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos.MutableBlockPos;
import net.minecraft.world.IBlockAccess;
import org.joml.Vector3d;

public class WorldUtil {
	public static Vector3d getVelocity(IBlockAccess world, BlockPos pos, IBlockState thizz) {
		Vector3d velocity = new Vector3d();
		int decay = getEffectiveFlowDecay(world, pos, thizz);
		MutableBlockPos mutable = new MutableBlockPos(pos.getX(), pos.getY(), pos.getZ());

		for (EnumFacing dire : DirectionUtil.HORIZONTAL_DIRECTIONS) {
			int adjX = pos.getX() + dire.getFrontOffsetX();
			int adjZ = pos.getZ() + dire.getFrontOffsetZ();
			mutable.set(adjX, pos.getY(), adjZ);
			int adjDecay = getEffectiveFlowDecay(world, mutable, thizz);
			if (adjDecay < 0) {
				if (!world.getBlockState(mutable).getBlock().getMaterial().blocksMovement()) {
					adjDecay = getEffectiveFlowDecay(world, mutable.down(), thizz);
					if (adjDecay >= 0) {
						adjDecay -= decay - 8;
						velocity = velocity.add((double)((adjX - pos.getX()) * adjDecay), 0.0, (double)((adjZ - pos.getZ()) * adjDecay));
					}
				}
			} else {
				adjDecay -= decay;
				velocity = velocity.add((double)((adjX - pos.getX()) * adjDecay), 0.0, (double)((adjZ - pos.getZ()) * adjDecay));
			}
		}

		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if ((Integer)state.getValue(BlockLiquid.LEVEL) >= 8
			&& (
				block.isBlockSolid(world, pos.north(), EnumFacing.NORTH)
					|| block.isBlockSolid(world, pos.south(), EnumFacing.SOUTH)
					|| block.isBlockSolid(world, pos.west(), EnumFacing.WEST)
					|| block.isBlockSolid(world, pos.east(), EnumFacing.EAST)
					|| block.isBlockSolid(world, pos.up().south(), EnumFacing.NORTH)
					|| block.isBlockSolid(world, pos.up().west(), EnumFacing.SOUTH)
					|| block.isBlockSolid(world, pos.up().west(), EnumFacing.WEST)
					|| block.isBlockSolid(world, pos.up().east(), EnumFacing.EAST)
			)) {
			velocity = velocity.normalize().add(0.0, -6.0, 0.0);
		}

		return velocity.x == 0.0 && velocity.y == 0.0 && velocity.z == 0.0 ? velocity.zero() : velocity.normalize();
	}

	public static boolean method_15756(IBlockAccess world, BlockPos pos, BlockLiquid fluid) {
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				IBlockState block = world.getBlockState(pos);
				if (!block.getBlock().isOpaqueCube() && getFluid(block) != fluid) {
					return true;
				}
			}
		}

		return false;
	}

	public static float getFluidHeight(BlockLiquid fluid, int meta) {
		return fluid == null ? 0.0F : 1.0F - BlockLiquid.getLiquidHeightPercent(meta);
	}

	public static int getEffectiveFlowDecay(IBlockAccess world, BlockPos pos, IBlockState thiz) {
		if (world.getBlockState(pos).getBlock().getMaterial() != thiz.getBlock().getMaterial()) {
			return -1;
		} else {
			int decay = (Integer)thiz.getValue(BlockLiquid.LEVEL);
			return decay >= 8 ? 0 : decay;
		}
	}

	public static boolean shouldDisplayFluidOverlay(IBlockState block) {
		return !block.getBlock().getMaterial().isOpaque() || block.getBlock().getMaterial() == Material.leaves;
	}

	public static BlockLiquid getFluid(IBlockState b) {
		return toFluidBlock(b.getBlock());
	}

	public static boolean isEmptyOrSame(BlockLiquid fluid, BlockLiquid otherFluid) {
		return otherFluid == null || fluid == otherFluid;
	}

	public static boolean method_15749(IBlockAccess world, BlockLiquid thiz, BlockPos pos, EnumFacing dir) {
		IBlockState b = world.getBlockState(pos);
		BlockLiquid f = getFluid(b);
		if (f == thiz) {
			return false;
		} else {
			return dir == EnumFacing.UP ? true : b.getBlock().getMaterial() != Material.ice && b.getBlock().isBlockSolid(world, pos, dir);
		}
	}

	public static BlockLiquid toFluidBlock(Block block) {
		return block instanceof BlockLiquid ? (BlockLiquid)block : null;
	}

	public static BlockLiquid getFluidOfBlock(Block block) {
		return toFluidBlock(block);
	}
}
