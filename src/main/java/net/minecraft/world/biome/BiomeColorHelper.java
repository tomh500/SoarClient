package net.minecraft.world.biome;

import com.soarclient.libs.sodium.SodiumClientMod;
import com.soarclient.libs.sodium.client.world.SodiumBlockAccess;

import net.minecraft.util.BlockPos;
import net.minecraft.util.BlockPos.MutableBlockPos;
import net.minecraft.world.IBlockAccess;

public class BiomeColorHelper
{
    private static final BiomeColorHelper.ColorResolver GRASS_COLOR = new BiomeColorHelper.ColorResolver()
    {
        public int getColorAtPos(BiomeGenBase biome, BlockPos blockPosition)
        {
            return biome.getGrassColorAtPos(blockPosition);
        }
    };
    private static final BiomeColorHelper.ColorResolver FOLIAGE_COLOR = new BiomeColorHelper.ColorResolver()
    {
        public int getColorAtPos(BiomeGenBase biome, BlockPos blockPosition)
        {
            return biome.getFoliageColorAtPos(blockPosition);
        }
    };
    private static final BiomeColorHelper.ColorResolver WATER_COLOR_MULTIPLIER = new BiomeColorHelper.ColorResolver()
    {
        public int getColorAtPos(BiomeGenBase biome, BlockPos blockPosition)
        {
            return biome.waterColorMultiplier;
        }
    };

    private static int getColorAtPos(IBlockAccess blockAccess, BlockPos pos, BiomeColorHelper.ColorResolver colorResolver)
    {
		if (blockAccess instanceof SodiumBlockAccess) {
			return ((SodiumBlockAccess)blockAccess).getBlockTint(pos, colorResolver);
		} else {
			int radius = SodiumClientMod.options().quality.biomeBlendRadius;
			if (radius == 0) {
				return colorResolver.getColorAtPos(blockAccess.getBiomeGenForCoords(pos), pos);
			} else {
				int blockCount = (radius * 2 + 1) * (radius * 2 + 1);
				int i = 0;
				int j = 0;
				int k = 0;
				MutableBlockPos mutablePos = new MutableBlockPos();

				for (int z = -radius; z <= radius; z++) {
					for (int x = -radius; x <= radius; x++) {
						mutablePos.set(pos.getX() + x, pos.getY(), pos.getZ() + z);
						int l = colorResolver.getColorAtPos(blockAccess.getBiomeGenForCoords(mutablePos), mutablePos);
						i += (l & 0xFF0000) >> 16;
						j += (l & 0xFF00) >> 8;
						k += l & 0xFF;
					}
				}

				return (i / blockCount & 0xFF) << 16 | (j / blockCount & 0xFF) << 8 | k / blockCount & 0xFF;
			}
		}
    }

    public static int getGrassColorAtPos(IBlockAccess p_180286_0_, BlockPos p_180286_1_)
    {
        return getColorAtPos(p_180286_0_, p_180286_1_, GRASS_COLOR);
    }

    public static int getFoliageColorAtPos(IBlockAccess p_180287_0_, BlockPos p_180287_1_)
    {
        return getColorAtPos(p_180287_0_, p_180287_1_, FOLIAGE_COLOR);
    }

    public static int getWaterColorAtPos(IBlockAccess p_180288_0_, BlockPos p_180288_1_)
    {
        return getColorAtPos(p_180288_0_, p_180288_1_, WATER_COLOR_MULTIPLIER);
    }

    public interface ColorResolver
    {
        int getColorAtPos(BiomeGenBase biome, BlockPos blockPosition);
    }
}
