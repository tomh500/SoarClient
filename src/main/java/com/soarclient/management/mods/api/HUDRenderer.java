package com.soarclient.management.mods.api;

import java.awt.Color;

import com.soarclient.Soar;
import com.soarclient.management.mods.api.design.HUDDesign;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.nanovg.font.Font;

public class HUDRenderer {

	private Position position;

	public HUDRenderer(Position position) {
		this.position = position;
	}
	
	public void drawBackground(float x, float y, float width, float height, float radius) {

		float scale = position.getScale();

		getDesign().drawBackground(position.getX() + (x * scale), position.getY() + (y * scale), width * scale,
				height * scale, radius * scale);
	}

	public void drawBackground(float width, float height, float radius) {
		drawBackground(0, 0, width, height, radius);
	}

	public void drawBackground(float width, float height) {
		drawBackground(0, 0, width, height, 6 /* TODO: Radius Setting */);
	}

	public void drawText(String text, float x, float y, Color color, float size, Font font) {

		float scale = position.getScale();

		getDesign().drawText(text, position.getX() + (x * scale), position.getY() + (y * scale), color, size * scale,
				font);
	}

	public void drawText(String text, float x, float y, float size, Font font) {
		drawText(text, x, y, getTextColor(), size, font);
	}

	public float getTextWidth(String text, float size, Font font) {
		return NanoVGHelper.getInstance().getTextWidth(text, size, font);
	}

	public void drawRoundedRect(float x, float y, float width, float height, float radius, Color color) {

		float scale = position.getScale();

		NanoVGHelper.getInstance().drawRoundedRect(position.getX() + (x * scale), position.getY() + (y * scale),
				width * scale, height * scale, radius * scale, color);
	}

	public void drawRect(float x, float y, float width, float height, Color color) {

		float scale = position.getScale();

		NanoVGHelper.getInstance().drawRect(position.getX() + (x * scale), position.getY() + (y * scale), width * scale,
				height * scale, color);
	}

	public void drawRect(float x, float y, float width, float height) {
		drawRect(x, y, width, height, getTextColor());
	}

	public void scale(float x, float y, float width, float height, float nvgScale) {

		float scale = position.getScale();

		NanoVGHelper.getInstance().scale(position.getX() + (x * scale), position.getY() + (y * scale), width * scale,
				height * scale, nvgScale);
	}

	public void drawArc(float x, float y, float radius, float startAngle, float endAngle, float strokeWidth,
			Color color) {

		float scale = position.getScale();

		NanoVGHelper.getInstance().drawArc(position.getX() + (x * scale), position.getY() + (y * scale), radius * scale,
				startAngle, endAngle, strokeWidth * scale, color);
	}

	public Color getTextColor() {
		return getDesign().getTextColor();
	}

	public Color getFocusColor() {
		return getDesign().getFocusColor();
	}

	public HUDDesign getDesign() {
		return Soar.getInstance().getModManager().getCurrentDesign();
	}
}