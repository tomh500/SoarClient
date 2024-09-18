package com.soarclient.libraries.sodium.client.render.pipeline.context;

import com.soarclient.libraries.sodium.client.model.light.LightPipelineProvider;
import com.soarclient.libraries.sodium.client.model.light.cache.ArrayLightDataCache;
import com.soarclient.libraries.sodium.client.model.quad.blender.BiomeColorBlender;
import com.soarclient.libraries.sodium.client.render.pipeline.BlockRenderer;
import com.soarclient.libraries.sodium.client.render.pipeline.ChunkRenderCache;
import com.soarclient.libraries.sodium.client.render.pipeline.FluidRenderer;
import com.soarclient.libraries.sodium.client.world.WorldSlice;
import com.soarclient.libraries.sodium.client.world.WorldSliceLocal;
import com.soarclient.libraries.sodium.client.world.cloned.ChunkRenderContext;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.world.World;

public class ChunkRenderCacheLocal extends ChunkRenderCache {
	private final ArrayLightDataCache lightDataCache;
	private final BlockRenderer blockRenderer;
	private final FluidRenderer fluidRenderer;
	private final BlockModelShapes blockModels;
	private final WorldSlice worldSlice;
	private WorldSliceLocal localSlice;

	public ChunkRenderCacheLocal(Minecraft client, World world) {
		this.worldSlice = new WorldSlice(world);
		this.lightDataCache = new ArrayLightDataCache(this.worldSlice);
		LightPipelineProvider lightPipelineProvider = new LightPipelineProvider(this.lightDataCache);
		BiomeColorBlender biomeColorBlender = this.createBiomeColorBlender();
		this.blockRenderer = new BlockRenderer(client, lightPipelineProvider, biomeColorBlender);
		this.fluidRenderer = new FluidRenderer(client, lightPipelineProvider, biomeColorBlender);
		this.blockModels = client.getBlockRendererDispatcher().getBlockModelShapes();
	}

	public void init(ChunkRenderContext context) {
		this.lightDataCache.reset(context.origin());
		this.worldSlice.copyData(context);
		this.localSlice = new WorldSliceLocal(this.worldSlice);
	}

	public BlockRenderer getBlockRenderer() {
		return this.blockRenderer;
	}

	public FluidRenderer getFluidRenderer() {
		return this.fluidRenderer;
	}

	public BlockModelShapes getBlockModels() {
		return this.blockModels;
	}

	public WorldSlice getWorldSlice() {
		return this.worldSlice;
	}

	public WorldSliceLocal getLocalSlice() {
		return this.localSlice;
	}
}
