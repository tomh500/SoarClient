package com.soarclient.event.impl;

import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NanoVG;

import com.soarclient.event.Event;
import com.soarclient.nanovg.NanoVGHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class RenderBlurEvent extends Event {

	private Minecraft mc = Minecraft.getMinecraft();
	private NVGPaint paint;
	private int texture;

	public RenderBlurEvent(int texture, NVGPaint paint) {
		this.texture = texture;
		this.paint = paint;
	}

	public void setupAndDraw(Runnable task) {

		NanoVGHelper nvg = NanoVGHelper.getInstance();

		NanoVG.nvgBeginPath(nvg.getContext());
		task.run();
	}

	public void scale(float x, float y, float width, float height, float scale) {

		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		NanoVGHelper nvg = NanoVGHelper.getInstance();
		int srScale = sr.getScaleFactor();

		nvg.scale(x * srScale, y * srScale, width * srScale, height * srScale, scale);
	}

	public void drawRect(float x, float y, float width, float height, float alpha) {

		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		NanoVGHelper nvg = NanoVGHelper.getInstance();

		int scale = sr.getScaleFactor();
		int mcWidth = mc.displayWidth;
		int mcHeight = mc.displayHeight;

		NanoVG.nvgBeginPath(nvg.getContext());
		NanoVG.nvgRect(nvg.getContext(), x * scale, y * scale, width * scale, height * scale);
		NanoVG.nvgFillPaint(nvg.getContext(),
				NanoVG.nvgImagePattern(nvg.getContext(), 0, mcHeight, mcWidth, -mcHeight, 0, texture, alpha, paint));
		NanoVG.nvgFill(nvg.getContext());
	}

	public void drawRoundedRect(float x, float y, float width, float height, float radius, float alpha) {

		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		NanoVGHelper nvg = NanoVGHelper.getInstance();

		int scale = sr.getScaleFactor();
		int mcWidth = mc.displayWidth;
		int mcHeight = mc.displayHeight;

		NanoVG.nvgBeginPath(nvg.getContext());
		NanoVG.nvgRoundedRect(nvg.getContext(), x * scale, y * scale, width * scale, height * scale, radius * scale);
		NanoVG.nvgFillPaint(nvg.getContext(),
				NanoVG.nvgImagePattern(nvg.getContext(), 0, mcHeight, mcWidth, -mcHeight, 0, texture, alpha, paint));
		NanoVG.nvgFill(nvg.getContext());
	}

	public void drawRoundedRectVarying(float x, float y, float width, float height, float topLeftRadius,
			float topRightRadius, float bottomLeftRadius, float bottomRightRadius, float alpha) {

		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		NanoVGHelper nvg = NanoVGHelper.getInstance();

		int scale = sr.getScaleFactor();
		int mcWidth = mc.displayWidth;
		int mcHeight = mc.displayHeight;

		NanoVG.nvgBeginPath(nvg.getContext());
		NanoVG.nvgRoundedRectVarying(nvg.getContext(), x * scale, y * scale, width * scale, height * scale, topLeftRadius * scale,
				topRightRadius * scale, bottomRightRadius * scale, bottomLeftRadius * scale);
		NanoVG.nvgFillPaint(nvg.getContext(),
				NanoVG.nvgImagePattern(nvg.getContext(), 0, mcHeight, mcWidth, -mcHeight, 0, texture, alpha, paint));
		NanoVG.nvgFill(nvg.getContext());
	}

	public void drawCircle(float x, float y, float radius, float alpha) {

		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		NanoVGHelper nvg = NanoVGHelper.getInstance();

		int scale = sr.getScaleFactor();
		int mcWidth = mc.displayWidth;
		int mcHeight = mc.displayHeight;

		NanoVG.nvgBeginPath(nvg.getContext());
		NanoVG.nvgCircle(nvg.getContext(), x * scale, y * scale, radius * scale);
		NanoVG.nvgFillPaint(nvg.getContext(),
				NanoVG.nvgImagePattern(nvg.getContext(), 0, mcHeight, mcWidth, -mcHeight, 0, texture, alpha, paint));
		NanoVG.nvgFill(nvg.getContext());
	}
}