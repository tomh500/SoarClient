package com.soarclient.libraries.sodium.client.model.quad.blender;

import java.util.Arrays;

import com.soarclient.libraries.sodium.client.model.quad.ModelQuadView;
import com.soarclient.libraries.sodium.client.util.color.ColorARGB;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public class FlatBiomeColorBlender implements BiomeColorBlender {
	private final int[] cachedRet = new int[4];

	@Override
	public int[] getColors(IBlockColor colorizer, IBlockAccess world, IBlockState state, BlockPos origin, ModelQuadView quad) {
		Arrays.fill(this.cachedRet, ColorARGB.toABGR(colorizer.colorMultiplier(state, world, origin, quad.getColorIndex())));
		return this.cachedRet;
	}
}
