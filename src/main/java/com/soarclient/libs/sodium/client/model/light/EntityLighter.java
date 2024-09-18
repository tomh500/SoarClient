package com.soarclient.libs.sodium.client.model.light;

import com.soarclient.libs.sodium.client.util.MathUtil;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.BlockPos.MutableBlockPos;
import net.minecraft.world.EnumSkyBlock;

public class EntityLighter {

	public static int getBlendedLight(Entity entity, float tickDelta) {
		boolean calcBlockLight = !entity.isBurning();
		double x1 = MathUtil.lerp((double)tickDelta, entity.prevPosX, entity.posX);
		double y1 = MathUtil.lerp((double)tickDelta, entity.prevPosY, entity.posY);
		double z1 = MathUtil.lerp((double)tickDelta, entity.prevPosZ, entity.posZ);
		double width = Math.max((double)entity.width, 0.001);
		double height = Math.max((double)entity.height, 0.001);
		double x2 = x1 + width;
		double y2 = y1 + height;
		double z2 = z1 + width;
		int bMinX = MathHelper.floor_double(x1);
		int bMinY = MathHelper.floor_double(y1);
		int bMinZ = MathHelper.floor_double(z1);
		int bMaxX = MathHelper.ceiling_double_int(x2);
		int bMaxY = MathHelper.ceiling_double_int(y2);
		int bMaxZ = MathHelper.ceiling_double_int(z2);
		double max = 0.0;
		double sl = 0.0;
		double bl = 0.0;
		MutableBlockPos pos = new MutableBlockPos();

		for (int bX = bMinX; bX < bMaxX; bX++) {
			double ix1 = Math.max((double)bX, x1);
			double ix2 = Math.min((double)(bX + 1), x2);

			for (int bY = bMinY; bY < bMaxY; bY++) {
				double iy1 = Math.max((double)bY, y1);
				double iy2 = Math.min((double)(bY + 1), y2);

				for (int bZ = bMinZ; bZ < bMaxZ; bZ++) {
					pos.set(bX, bY, bZ);
					IBlockState blockState = entity.worldObj.getBlockState(pos);
					if (!blockState.getBlock().isOpaqueCube() || blockState.getBlock().getLightValue() > 0) {
						double iz1 = Math.max((double)bZ, z1);
						double iz2 = Math.min((double)(bZ + 1), z2);
						double weight = (ix2 - ix1) * (iy2 - iy1) * (iz2 - iz1);
						max += weight;
						sl += weight * ((double)entity.worldObj.getLightFor(EnumSkyBlock.SKY, pos) / 15.0);
						if (calcBlockLight) {
							bl += weight * ((double)entity.worldObj.getLightFor(EnumSkyBlock.BLOCK, pos) / 15.0);
						} else {
							bl += weight;
						}
					}
				}
			}
		}

		int bli = MathHelper.floor_double(bl / max * 240.0);
		int sli = MathHelper.floor_double(sl / max * 240.0);
		return (sli & 65535) << 16 | bli & 65535;
	}
}
