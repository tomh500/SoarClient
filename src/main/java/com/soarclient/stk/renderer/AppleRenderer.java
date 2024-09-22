package com.soarclient.stk.renderer;

import java.awt.Color;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NanoVG;

import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.stk.styles.Materials;

public class AppleRenderer {

	public static void drawWindow(float x, float y, float width, float height, float radius) {

		NanoVGHelper nvg = getNvg();

		nvg.drawRoundedRect(x, y, width, height, radius, Materials.WINDOWS_GLASS);
		drawWindowOutline(x, y, width, height, radius);
	}

	private static void drawWindowOutline(float x, float y, float width, float height, float radius) {

		NVGPaint paint = NVGPaint.create();
		NanoVGHelper nvg = getNvg();
		long ctx = nvg.getContext();

		NanoVG.nvgBeginPath(ctx);
		NanoVG.nvgRoundedRect(ctx, x, y, width, height, radius);
		NanoVG.nvgStrokeWidth(ctx, 1);

		NVGColor nvgColor = nvg.getColor(Materials.WINDOWS_GLASS_OUTLINE_0);
		NVGColor tColor = nvg.getColor(new Color(0, 0, 0, 0));

		NanoVG.nvgStrokePaint(ctx,
				NanoVG.nvgLinearGradient(ctx, x, y, x + (width / 10), y + (height / 2), nvgColor, tColor, paint));
		NanoVG.nvgStroke(ctx);

		nvgColor.free();

		NVGColor nvgColor1 = nvg.getColor(Materials.WINDOWS_GLASS_OUTLINE_1);

		NanoVG.nvgBeginPath(ctx);
		NanoVG.nvgRoundedRect(ctx, x, y, width, height, radius);
		NanoVG.nvgStrokeWidth(ctx, 1);

        NanoVG.nvgStrokePaint(ctx, NanoVG.nvgLinearGradient(ctx, x + (width / 2), y + height, x + (width / 2),
                y + (height / 1.6F), nvgColor1, tColor, paint));
		NanoVG.nvgStroke(ctx);

		tColor.free();
		nvgColor1.free();
	}

	private static NanoVGHelper getNvg() {
		return NanoVGHelper.getInstance();
	}
}
