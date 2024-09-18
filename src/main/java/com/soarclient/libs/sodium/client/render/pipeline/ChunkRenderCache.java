package com.soarclient.libs.sodium.client.render.pipeline;

import com.soarclient.libs.sodium.client.model.quad.blender.BiomeColorBlender;

import net.minecraft.client.Minecraft;

public class ChunkRenderCache {
	protected BiomeColorBlender createBiomeColorBlender() {
		return BiomeColorBlender.create(Minecraft.getMinecraft());
	}
}
