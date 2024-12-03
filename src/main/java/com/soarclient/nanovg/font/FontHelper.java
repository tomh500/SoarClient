package com.soarclient.nanovg.font;

import java.nio.ByteBuffer;

import org.lwjgl.nanovg.NanoVG;

import com.soarclient.utils.IOUtils;

public class FontHelper {

	private static FontHelper instance = new FontHelper();

	public void init(long nvg) {
		for (Font f : Fonts.getFonts()) {
			loadFont(nvg, f);
		}
	}

	private void loadFont(long nvg, Font font) {

		if (font.isLoaded()) {
			return;
		}

		int loaded = -1;

		try {
			ByteBuffer buffer = IOUtils.resourceToByteBuffer(font.getFilePath());
			loaded = NanoVG.nvgCreateFontMem(nvg, font.getName(), buffer, false);
			font.setBuffer(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (loaded == -1) {
			throw new RuntimeException("Failed to init font " + font.getName());
		} else {
			font.setLoaded(true);
		}
	}

	public static FontHelper getInstance() {
		return instance;
	}
}