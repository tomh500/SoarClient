package com.soarclient.management.mods.api.design;

import java.awt.Color;

import com.soarclient.nanovg.font.Font;

public abstract class HUDDesign {

	private String name;

	public HUDDesign(String name) {
		this.name = name;
	}

	public abstract void drawBackground(float x, float y, float width, float height, float radius);

	public abstract void drawText(String text, float x, float y, float size, Font font, Color color);

	public abstract Color getTextColor();

	public String getName() {
		return name;
	}
}
