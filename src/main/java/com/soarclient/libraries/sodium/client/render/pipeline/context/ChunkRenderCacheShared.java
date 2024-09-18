package com.soarclient.libraries.sodium.client.render.pipeline.context;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import java.util.Map;

import com.soarclient.libraries.sodium.client.model.light.LightPipelineProvider;
import com.soarclient.libraries.sodium.client.model.light.cache.HashLightDataCache;
import com.soarclient.libraries.sodium.client.model.quad.blender.BiomeColorBlender;
import com.soarclient.libraries.sodium.client.render.pipeline.BlockRenderer;
import com.soarclient.libraries.sodium.client.render.pipeline.ChunkRenderCache;
import com.soarclient.libraries.sodium.client.world.WorldSlice;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.World;

public class ChunkRenderCacheShared extends ChunkRenderCache {
	private static final Map<World, ChunkRenderCacheShared> INSTANCES = new Reference2ObjectOpenHashMap<>();
	private final BlockRenderer blockRenderer;
	private final HashLightDataCache lightCache;

	private ChunkRenderCacheShared(WorldSlice world) {
		Minecraft client = Minecraft.getMinecraft();
		this.lightCache = new HashLightDataCache(world);
		BiomeColorBlender biomeColorBlender = this.createBiomeColorBlender();
		LightPipelineProvider lightPipelineProvider = new LightPipelineProvider(this.lightCache);
		this.blockRenderer = new BlockRenderer(client, lightPipelineProvider, biomeColorBlender);
	}

	private void resetCache() {
		this.lightCache.clearCache();
	}

	public static ChunkRenderCacheShared getInstance(WorldClient world) {
		ChunkRenderCacheShared instance = (ChunkRenderCacheShared)INSTANCES.get(world);
		if (instance == null) {
			throw new IllegalStateException("No global renderer exists");
		} else {
			return instance;
		}
	}

	public static void destroyRenderContext(WorldClient world) {
		if (INSTANCES.remove(world) == null) {
			throw new IllegalStateException("No render context exists for world: " + world);
		}
	}

	public static void createRenderContext(WorldClient world) {
		if (INSTANCES.containsKey(world)) {
			throw new IllegalStateException("Render context already exists for world: " + world);
		} else {
			INSTANCES.put(world, new ChunkRenderCacheShared(new WorldSlice(world)));
		}
	}

	public static void resetCaches() {
		for (ChunkRenderCacheShared context : INSTANCES.values()) {
			context.resetCache();
		}
	}
}
