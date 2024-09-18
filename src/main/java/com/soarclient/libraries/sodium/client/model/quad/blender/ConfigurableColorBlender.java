package com.soarclient.libraries.sodium.client.model.quad.blender;

import com.soarclient.libraries.sodium.SodiumClientMod;
import com.soarclient.libraries.sodium.client.model.quad.ModelQuadView;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

class ConfigurableColorBlender implements BiomeColorBlender {
	private final BiomeColorBlender defaultBlender = new FlatBiomeColorBlender();
	private final BiomeColorBlender smoothBlender = (BiomeColorBlender)(isSmoothBlendingEnabled() ? new SmoothBiomeColorBlender() : this.defaultBlender);

	public ConfigurableColorBlender(Minecraft client) {
	}

	private static boolean isSmoothBlendingEnabled() {
		return SodiumClientMod.options().quality.biomeBlendRadius > 0;
	}

	@Override
	public int[] getColors(IBlockColor colorizer, IBlockAccess world, IBlockState state, BlockPos origin, ModelQuadView quad) {
		BiomeColorBlender blender;
		if (BlockColorSettings.isSmoothBlendingEnabled(world, state, origin)) {
			blender = this.smoothBlender;
		} else {
			blender = this.defaultBlender;
		}

		return blender.getColors(colorizer, world, state, origin, quad);
	}
}
