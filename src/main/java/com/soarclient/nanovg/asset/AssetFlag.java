package com.soarclient.nanovg.asset;

import org.lwjgl.nanovg.NanoVG;

public enum AssetFlag {
	NONE(0), DEFAULT(NanoVG.NVG_IMAGE_REPEATX | NanoVG.NVG_IMAGE_REPEATY | NanoVG.NVG_IMAGE_GENERATE_MIPMAPS);

	private int flags;

	private AssetFlag(int flags) {
		this.flags = flags;
	}

	public int getFlags() {
		return flags;
	}
}