package com.soarclient.nanovg;

import java.awt.Color;
import java.util.function.LongConsumer;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL2;
import org.lwjgl.opengl.GL11;

import com.soarclient.nanovg.font.Font;
import com.soarclient.nanovg.font.FontHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class NanoVGHelper {

	private static NanoVGHelper instance = new NanoVGHelper();
	private Minecraft mc = Minecraft.getMinecraft();
	private long nvg = -1;
	
	public void start() {

		if (nvg == -1) {

			nvg = NanoVGGL2.nvgCreate(NanoVGGL2.NVG_ANTIALIAS);

			if (nvg == -1) {
				throw new RuntimeException("Failed to create nano vg context");
			}
			
			FontHelper.getInstance().init(nvg);
		}
	}
	
	public void setupAndDraw(Runnable task, boolean mcScale) {

		ScaledResolution sr = new ScaledResolution(mc);

		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
		NanoVG.nvgBeginFrame(nvg, mc.displayWidth, mc.displayHeight, 1);

		if (mcScale) {
			NanoVG.nvgScale(nvg, sr.getScaleFactor(), sr.getScaleFactor());
		}
		
		task.run();

		GL11.glDisable(GL11.GL_ALPHA_TEST);
		NanoVG.nvgEndFrame(nvg);
		GL11.glPopAttrib();
	}
	
	public void setupAndDraw(Runnable task) {
		setupAndDraw(task, true);
	}
	
	public void drawRect(float x, float y, float width, float height, Color color) {

		NanoVG.nvgBeginPath(nvg);
		NanoVG.nvgRect(nvg, x, y, width, height);

		NVGColor nvgColor = getColor(color);

		NanoVG.nvgFill(nvg);
		nvgColor.free();
	}

	public void drawRoundedRect(float x, float y, float width, float height, float radius, Color color) {

		NanoVG.nvgBeginPath(nvg);
		NanoVG.nvgRoundedRect(nvg, x, y, width, height, radius);

		NVGColor nvgColor = getColor(color);

		NanoVG.nvgFill(nvg);
		nvgColor.free();
	}
	
	public void drawText(String text, float x, float y, Color color, float size, Font font) {

		if (text == null) {
			text = "null";
		}

		y += size / 2;

		NanoVG.nvgBeginPath(nvg);
		NanoVG.nvgFontSize(nvg, size);
		NanoVG.nvgFontFace(nvg, font.getName());
		NanoVG.nvgTextAlign(nvg, NanoVG.NVG_ALIGN_LEFT | NanoVG.NVG_ALIGN_MIDDLE);

		NVGColor nvgColor = getColor(color);

		NanoVG.nvgText(nvg, x, y, text);
		nvgColor.free();
	}

	public void drawCenteredText(String text, float x, float y, Color color, float size, Font font) {

		float textWidth = getTextWidth(text, size, font);

		drawText(text, x - (textWidth / 2F), y, color, size, font);
	}
	
	public float getTextWidth(String text, float size, Font font) {

		float[] bounds = new float[4];

		NanoVG.nvgFontSize(nvg, size);
		NanoVG.nvgFontFace(nvg, font.getName());
		NanoVG.nvgTextBounds(nvg, 0, 0, text, bounds);
		NanoVG.nvgTextAlign(nvg, NanoVG.NVG_ALIGN_LEFT | NanoVG.NVG_ALIGN_MIDDLE);

		return bounds[2] - bounds[0];
	}

	public float getTextHeight(String text, float size, Font font) {

		float[] bounds = new float[4];

		NanoVG.nvgFontSize(nvg, size);
		NanoVG.nvgFontFace(nvg, font.getName());
		NanoVG.nvgTextBounds(nvg, 0, 0, text, bounds);

		return bounds[3] - bounds[1];
	}

	public NVGColor getColor(Color color) {

		NVGColor nvgColor = NVGColor.calloc();
		int rgb = color.getRGB();

		NanoVG.nvgRGBA((byte) (rgb >> 16 & 0xFF), (byte) (rgb >> 8 & 0xFF), (byte) (rgb & 0xFF),
				(byte) (rgb >> 24 & 0xFF), nvgColor);
		NanoVG.nvgFillColor(nvg, nvgColor);

		return nvgColor;
	}

	public static NanoVGHelper getInstance() {
		return instance;
	}
}
