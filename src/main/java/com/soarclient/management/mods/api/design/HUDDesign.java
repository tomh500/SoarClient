package com.soarclient.management.mods.api.design;

import java.awt.Color;

import com.soarclient.nanovg.font.Font;

public abstract class HUDDesign {

	private String name;

	public HUDDesign(String name) {
		this.name = name;
	}

	public abstract void drawBackground(float x, float y, float width, float height, float radius);

	public abstract void drawText(String text, float x, float y, Color color, float size, Font font);

	public abstract Color getTextColor();

	public abstract Color getFocusColor();

	public String getName() {
		return name;
	}
}
