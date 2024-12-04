package com.soarclient.libraries.sodium.client.model.light.data;

import com.soarclient.libraries.sodium.client.world.WorldSlice;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos.MutableBlockPos;

public abstract class LightDataAccess {
	private final MutableBlockPos pos = new MutableBlockPos();
	protected WorldSlice world;

	public long get(int x, int y, int z, EnumFacing d1, EnumFacing d2) {
		return this.get(x + d1.getFrontOffsetX() + d2.getFrontOffsetX(),
				y + d1.getFrontOffsetY() + d2.getFrontOffsetY(), z + d1.getFrontOffsetZ() + d2.getFrontOffsetZ());
	}

	public long get(int x, int y, int z, EnumFacing dir) {
		return this.get(x + dir.getFrontOffsetX(), y + dir.getFrontOffsetY(), z + dir.getFrontOffsetZ());
	}

	public long get(BlockPos pos, EnumFacing dir) {
		return this.get(pos.getX(), pos.getY(), pos.getZ(), dir);
	}

	public long get(BlockPos pos) {
		return this.get(pos.getX(), pos.getY(), pos.getZ());
	}

	public abstract long get(int integer1, int integer2, int integer3);

	protected long compute(int x, int y, int z) {
		BlockPos pos = this.pos.set(x, y, z);
		WorldSlice world = this.world;
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		float ao;
		boolean em;
		if (block.getLightValue() == 0) {
			ao = block.getAmbientOcclusionLightValue();
			em = false;
		} else {
			ao = 1.0F;
			em = true;
		}

		boolean op = !block.isOpaqueCube() || block.getLightOpacity() == 0;
		boolean fo = block.isOpaqueCube();
		boolean fc = block.isFullCube();
		int lm = fo && !em ? 0 : block.getMixedBrightnessForBlock(world, pos);
		return packAO(ao) | packLM(lm) | packOP(op) | packFO(fo) | packFC(fc) | 1152921504606846976L;
	}

	public static long packOP(boolean opaque) {
		return (opaque ? 1L : 0L) << 56;
	}

	public static boolean unpackOP(long word) {
		return (word >>> 56 & 1L) != 0L;
	}

	public static long packFO(boolean opaque) {
		return (opaque ? 1L : 0L) << 57;
	}

	public static boolean unpackFO(long word) {
		return (word >>> 57 & 1L) != 0L;
	}

	public static long packFC(boolean fullCube) {
		return (fullCube ? 1L : 0L) << 58;
	}

	public static boolean unpackFC(long word) {
		return (word >>> 58 & 1L) != 0L;
	}

	public static long packLM(int lm) {
		return (long) lm & 4294967295L;
	}

	public static int unpackLM(long word) {
		return (int) (word & 4294967295L);
	}

	public static long packAO(float ao) {
		int aoi = (int) (ao * 4096.0F);
		return ((long) aoi & 65535L) << 32;
	}

	public static float unpackAO(long word) {
		int aoi = (int) (word >>> 32 & 65535L);
		return (float) aoi * 2.4414062E-4F;
	}

	public WorldSlice getWorld() {
		return this.world;
	}
}
