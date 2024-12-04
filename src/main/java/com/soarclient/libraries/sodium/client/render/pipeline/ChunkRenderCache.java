package com.soarclient.libraries.sodium.client.render.pipeline;

import com.soarclient.libraries.sodium.client.model.quad.blender.BiomeColorBlender;

import net.minecraft.client.Minecraft;

public class ChunkRenderCache {
	protected BiomeColorBlender createBiomeColorBlender() {
		return BiomeColorBlender.create(Minecraft.getMinecraft());
	}
}
