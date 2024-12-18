package com.soarclient.libraries.sodium.client.render.chunk.passes;

import it.unimi.dsi.fastutil.objects.Reference2IntArrayMap;
import net.minecraft.util.EnumWorldBlockLayer;

public class BlockRenderPassManager {
	private final Reference2IntArrayMap<EnumWorldBlockLayer> mappingsId = new Reference2IntArrayMap<>();

	public BlockRenderPassManager() {
		this.mappingsId.defaultReturnValue(-1);
	}

	public int getRenderPassId(EnumWorldBlockLayer layer) {
		int pass = this.mappingsId.getInt(layer);
		if (pass < 0) {
			throw new NullPointerException("No render pass exists for layer: " + layer);
		} else {
			return pass;
		}
	}

	private void addMapping(EnumWorldBlockLayer layer, BlockRenderPass type) {
		if (this.mappingsId.put(layer, type.ordinal()) >= 0) {
			throw new IllegalArgumentException("Layer target already defined for " + layer);
		}
	}

	public static BlockRenderPassManager createDefaultMappings() {
		BlockRenderPassManager mapper = new BlockRenderPassManager();
		mapper.addMapping(EnumWorldBlockLayer.SOLID, BlockRenderPass.SOLID);
		mapper.addMapping(EnumWorldBlockLayer.CUTOUT_MIPPED, BlockRenderPass.CUTOUT_MIPPED);
		mapper.addMapping(EnumWorldBlockLayer.CUTOUT, BlockRenderPass.CUTOUT);
		mapper.addMapping(EnumWorldBlockLayer.TRANSLUCENT, BlockRenderPass.TRANSLUCENT);
		return mapper;
	}

	public BlockRenderPass getRenderPassForLayer(EnumWorldBlockLayer layer) {
		return this.getRenderPass(this.getRenderPassId(layer));
	}

	public BlockRenderPass getRenderPass(int i) {
		return BlockRenderPass.VALUES[i];
	}
}
