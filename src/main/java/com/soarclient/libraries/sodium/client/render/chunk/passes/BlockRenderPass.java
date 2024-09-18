package com.soarclient.libraries.sodium.client.render.chunk.passes;

import net.minecraft.util.EnumWorldBlockLayer;

public enum BlockRenderPass {
	SOLID(EnumWorldBlockLayer.SOLID, false),
	CUTOUT(EnumWorldBlockLayer.CUTOUT, false),
	CUTOUT_MIPPED(EnumWorldBlockLayer.CUTOUT_MIPPED, false),
	TRANSLUCENT(EnumWorldBlockLayer.TRANSLUCENT, true);

	public static final BlockRenderPass[] VALUES = values();
	public static final int COUNT = VALUES.length;
	private final EnumWorldBlockLayer layer;
	private final boolean translucent;

	private BlockRenderPass(EnumWorldBlockLayer layer, boolean translucent) {
		this.layer = layer;
		this.translucent = translucent;
	}

	public boolean isTranslucent() {
		return this.translucent;
	}
}
