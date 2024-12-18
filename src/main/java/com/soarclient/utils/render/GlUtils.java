package com.soarclient.utils.render;

import net.minecraft.client.renderer.GlStateManager;

public class GlUtils {

	public static void startScale(float x, float y, float scale) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, 0);
		GlStateManager.scale(scale, scale, 1);
		GlStateManager.translate(-x, -y, 0);
	}

	public static void startScale(float x, float y, float width, float height, float scale) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((x + (x + width)) / 2, (y + (y + height)) / 2, 0);
		GlStateManager.scale(scale, scale, 1);
		GlStateManager.translate(-(x + (x + width)) / 2, -(y + (y + height)) / 2, 0);
	}

	public static void stopScale() {
		GlStateManager.popMatrix();
	}

	public static void startTranslate(float x, float y) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, 0);
	}

	public static void stopTranslate() {
		GlStateManager.popMatrix();
	}
}
