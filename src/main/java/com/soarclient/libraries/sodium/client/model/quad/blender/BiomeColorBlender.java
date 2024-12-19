package com.soarclient.libraries.sodium.client.model.quad.blender;

import com.soarclient.libraries.sodium.client.model.quad.ModelQuadView;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface BiomeColorBlender {
	int[] getColors(IBlockColor iBlockColor, IBlockAccess iBlockAccess, IBlockState iBlockState, BlockPos blockPos,
			ModelQuadView modelQuadView);

	static BiomeColorBlender create(Minecraft client) {
		return new ConfigurableColorBlender(client);
	}
}
