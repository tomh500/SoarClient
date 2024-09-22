package com.soarclient.stk.renderer;

import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.stk.styles.Materials;

public class AppleRenderer {

	public static void drawWindow(float x, float y, float width, float height, float radius) {

		NanoVGHelper nvg = getNvg();

		nvg.drawRoundedRect(x, y, width, height, radius, Materials.WINDOWS_GLASS);
		nvg.drawOutline(x, y, width, height, radius, 1, Materials.WINDOWS_GLASS_OUTLINE);
	}

	private static NanoVGHelper getNvg() {
		return NanoVGHelper.getInstance();
	}
}
