package com.soarclient.management.mods.api.design.impl;

import java.awt.Color;

import com.soarclient.management.mods.api.design.HUDDesign;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.nanovg.font.Font;

public class SimpleDesign extends HUDDesign {

	public SimpleDesign() {
		super("design.simple");
	}

	@Override
	public void drawBackground(float x, float y, float width, float height, float radius) {

		NanoVGHelper nvg = NanoVGHelper.getInstance();

		// TODO: nvg.drawShadow(x, y, width, height, radius);
		nvg.drawRoundedRect(x, y, width, height, radius, new Color(0, 0, 0, 100));
	}

	@Override
	public void drawText(String text, float x, float y, float size, Font font, Color color) {

		NanoVGHelper nvg = NanoVGHelper.getInstance();

		nvg.drawText(text, x, y, color, size, font);
	}

	@Override
	public Color getTextColor() {
		return Color.WHITE;
	}
}
