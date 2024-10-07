package com.soarclient.nanovg;

import java.awt.Color;
import java.io.File;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL2;
import org.lwjgl.opengl.GL11;

import com.soarclient.nanovg.asset.AssetFlag;
import com.soarclient.nanovg.asset.AssetHelper;
import com.soarclient.nanovg.font.Font;
import com.soarclient.nanovg.font.FontHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

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

	public void drawOutline(float x, float y, float width, float height, float radius, float strokeWidth, Color color) {

		NanoVG.nvgBeginPath(nvg);
		NanoVG.nvgRoundedRect(nvg, x, y, width, height, radius);
		NanoVG.nvgStrokeWidth(nvg, strokeWidth);

		NVGColor nvgColor = getColor(color);

		NanoVG.nvgStrokeColor(nvg, nvgColor);
		NanoVG.nvgStroke(nvg);

		nvgColor.free();
	}

	public void drawCircle(float x, float y, float radius, Color color) {

		NanoVG.nvgBeginPath(nvg);
		NanoVG.nvgCircle(nvg, x, y, radius);

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

	public void drawAlignCenteredText(String text, float x, float y, Color color, float size, Font font) {

		if (text == null) {
			text = "null";
		}

		NanoVG.nvgBeginPath(nvg);
		NanoVG.nvgFontSize(nvg, size);
		NanoVG.nvgFontFace(nvg, font.getName());
		NanoVG.nvgTextAlign(nvg, NanoVG.NVG_ALIGN_MIDDLE | NanoVG.NVG_ALIGN_CENTER);

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

	public void scale(float x, float y, float scale) {
		if (scale != 0 && scale != 1) {
			NanoVG.nvgTranslate(nvg, x, y);
			NanoVG.nvgScale(nvg, scale, scale);
			NanoVG.nvgTranslate(nvg, -x, -y);
		}
	}

	public void scale(float x, float y, float width, float height, float scale) {
		if (scale != 0 && scale != 1) {
			NanoVG.nvgTranslate(nvg, (x + (x + width)) / 2, (y + (y + height)) / 2);
			NanoVG.nvgScale(nvg, scale, scale);
			NanoVG.nvgTranslate(nvg, -(x + (x + width)) / 2, -(y + (y + height)) / 2);
		}
	}

	public void rotate(float x, float y, float width, float height, float angle, boolean toRadians) {
		NanoVG.nvgTranslate(nvg, (x + (x + width)) / 2, (y + (y + height)) / 2);
		NanoVG.nvgRotate(nvg, toRadians ? (float) Math.toRadians(angle) : angle);
		NanoVG.nvgTranslate(nvg, -(x + (x + width)) / 2, -(y + (y + height)) / 2);
	}
	
	public void save() {
		NanoVG.nvgSave(nvg);
	}

	public void restore() {
		NanoVG.nvgRestore(nvg);
	}

	public void drawImage(int texture, float x, float y, float width, float height, float alpha) {

		AssetHelper assetHelper = AssetHelper.getInstance();

		if (assetHelper.loadImage(nvg, texture, width, height)) {

			int image = assetHelper.getImage(texture);

			NanoVG.nvgImageSize(nvg, image, new int[] { (int) width }, new int[] { -(int) height });
			NVGPaint p = NVGPaint.calloc();

			NanoVG.nvgImagePattern(nvg, x, y, width, height, 0, image, alpha, p);
			NanoVG.nvgBeginPath(nvg);
			NanoVG.nvgRect(nvg, x, y, width, height);
			NanoVG.nvgFillPaint(nvg, p);
			NanoVG.nvgFill(nvg);
			NanoVG.nvgClosePath(nvg);

			p.free();
		}
	}

	public void drawImage(File file, float x, float y, float width, float height, AssetFlag assetFlag) {

		AssetHelper assetHelper = AssetHelper.getInstance();

		if (assetHelper.loadImage(nvg, file, assetFlag)) {

			NVGPaint imagePaint = NVGPaint.calloc();
			int image = assetHelper.getImage(file);

			NanoVG.nvgBeginPath(nvg);
			NanoVG.nvgImagePattern(nvg, x, y, width, height, 0, image, 1, imagePaint);
			NanoVG.nvgRect(nvg, x, y, width, height);
			NanoVG.nvgFillPaint(nvg, imagePaint);
			NanoVG.nvgFill(nvg);
			imagePaint.free();
		}
	}

	public void drawImage(File file, float x, float y, float width, float height) {
		drawImage(file, x, y, width, height, AssetFlag.DEFAULT);
	}

	public void drawImage(ResourceLocation location, float x, float y, float width, float height, AssetFlag assetFlag) {

		AssetHelper assetHelper = AssetHelper.getInstance();

		if (assetHelper.loadImage(nvg, location, assetFlag)) {

			NVGPaint imagePaint = NVGPaint.calloc();
			int image = assetHelper.getImage(location);

			NanoVG.nvgBeginPath(nvg);
			NanoVG.nvgImagePattern(nvg, x, y, width, height, 0, image, 1, imagePaint);
			NanoVG.nvgRect(nvg, x, y, width, height);
			NanoVG.nvgFillPaint(nvg, imagePaint);
			NanoVG.nvgFill(nvg);
			imagePaint.free();
		}
	}

	public void drawImage(ResourceLocation location, float x, float y, float width, float height) {
		drawImage(location, x, y, width, height, AssetFlag.DEFAULT);
	}

	public void drawRoundedImage(File file, float x, float y, float width, float height, float radius,
			AssetFlag assetFlag) {

		AssetHelper assetHelper = AssetHelper.getInstance();

		if (assetHelper.loadImage(nvg, file, assetFlag)) {

			NVGPaint imagePaint = NVGPaint.calloc();
			int image = assetHelper.getImage(file);

			NanoVG.nvgBeginPath(nvg);
			NanoVG.nvgImagePattern(nvg, x, y, width, height, 0, image, 1, imagePaint);
			NanoVG.nvgRoundedRect(nvg, x, y, width, height, radius);
			NanoVG.nvgFillPaint(nvg, imagePaint);
			NanoVG.nvgFill(nvg);
			imagePaint.free();
		}
	}

	public void drawRoundedImage(File file, float x, float y, float width, float height, float radius) {
		drawRoundedImage(file, x, y, width, height, radius, AssetFlag.DEFAULT);
	}

	public void drawRoundedImage(ResourceLocation location, float x, float y, float width, float height, float radius,
			AssetFlag assetFlag) {

		AssetHelper assetHelper = AssetHelper.getInstance();

		if (assetHelper.loadImage(nvg, location, assetFlag)) {

			NVGPaint imagePaint = NVGPaint.calloc();
			int image = assetHelper.getImage(location);

			NanoVG.nvgBeginPath(nvg);
			NanoVG.nvgImagePattern(nvg, x, y, width, height, 0, image, 1, imagePaint);
			NanoVG.nvgRoundedRect(nvg, x, y, width, height, radius);
			NanoVG.nvgFillPaint(nvg, imagePaint);
			NanoVG.nvgFill(nvg);
			imagePaint.free();
		}
	}

	public void drawRoundedImage(ResourceLocation location, float x, float y, float width, float height, float radius) {
		drawRoundedImage(location, x, y, width, height, radius, AssetFlag.DEFAULT);
	}

	public void drawRoundedImage(int texture, float x, float y, float width, float height, float radius, float alpha) {

		AssetHelper assetHelper = AssetHelper.getInstance();

		if (assetHelper.loadImage(nvg, texture, width, height)) {

			int image = assetHelper.getImage(texture);

			NanoVG.nvgImageSize(nvg, image, new int[] { (int) width }, new int[] { -(int) height });
			NVGPaint p = NVGPaint.calloc();

			NanoVG.nvgImagePattern(nvg, x, y, width, height, 0, image, alpha, p);
			NanoVG.nvgBeginPath(nvg);
			NanoVG.nvgRoundedRect(nvg, x, y, width, height, radius);
			NanoVG.nvgFillPaint(nvg, p);
			NanoVG.nvgFill(nvg);
			NanoVG.nvgClosePath(nvg);

			p.free();
		}
	}

	public long getContext() {
		return nvg;
	}

	public static NanoVGHelper getInstance() {
		return instance;
	}
}
