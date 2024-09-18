package com.soarclient.libs.sodium.fluid;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockFluidRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class EmbeddiumFluidSpriteCache {
	private final TextureAtlasSprite[] waterSprites;
	private final TextureAtlasSprite[] lavaSprites;

	public EmbeddiumFluidSpriteCache() {
		BlockFluidRenderer fluidRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher().getFluidRenderer();
		this.waterSprites = fluidRenderer.getAtlasSpritesWater();
		this.lavaSprites = fluidRenderer.getAtlasSpritesLava();
	}

	public TextureAtlasSprite[] getSprites(BlockLiquid fluid) {
		return fluid.getMaterial() == Material.water ? this.waterSprites : this.lavaSprites;
	}
}
