package com.soarclient.stk.renderer;

import java.awt.Color;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NanoVG;

import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.stk.styles.Materials;
import com.soarclient.stk.styles.Style;

public class AppleRenderer {

	public static void drawWindow(float x, float y, float width, float height) {

		NanoVGHelper nvg = getNvg();

		nvg.drawRoundedRect(x, y, width, height, Style.dpToPixel(50), Materials.WINDOWS_GLASS);
		drawWindowOutline(x, y, width, height, Style.dpToPixel(50));
	}

    private static float calc(float x) {
        return 0.25F / (float) Math.pow(x, 1.5F);
    }

    private static void drawWindowOutline(float x, float y, float width, float height, float radius) {

        NVGPaint paint = NVGPaint.create();
        NanoVGHelper nvg = getNvg();
        long ctx = nvg.getContext();

        NanoVG.nvgBeginPath(ctx);
        NanoVG.nvgRoundedRect(ctx, x, y, width, height, radius);
        NanoVG.nvgStrokeWidth(ctx, Style.dpToPixel(2));

        NVGColor nvgColor = nvg.getColor(Materials.WINDOWS_GLASS_OUTLINE_0);
        NVGColor tColor = nvg.getColor(new Color(0, 0, 0, 0));
        float aspectRatio = width / height;

        NanoVG.nvgStrokePaint(ctx,
                NanoVG.nvgLinearGradient(ctx, x + (width * 0.5F), y,
                        x + (width * 0.5F) + (width * calc(aspectRatio) * 0.22F), y + (height) * 0.22F, nvgColor, tColor, paint));
        NanoVG.nvgStroke(ctx);

        nvgColor.free();

        NVGColor nvgColor1 = nvg.getColor(Materials.WINDOWS_GLASS_OUTLINE_1);

        NanoVG.nvgBeginPath(ctx);
        NanoVG.nvgRoundedRect(ctx, x, y, width, height, radius);
        NanoVG.nvgStrokeWidth(ctx, Style.dpToPixel(2));

        NanoVG.nvgStrokePaint(ctx,
                NanoVG.nvgLinearGradient(ctx, x + (width * 0.5F) + (width * calc(aspectRatio) * 0.57F), y + (height * 0.57F),
                        x + (width * 0.5F) + (width * calc(aspectRatio)), y + height, tColor, nvgColor1, paint));
        NanoVG.nvgStroke(ctx);

        tColor.free();
        nvgColor1.free();
    }

	private static NanoVGHelper getNvg() {
		return NanoVGHelper.getInstance();
	}
}
