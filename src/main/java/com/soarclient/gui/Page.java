package com.soarclient.gui;

import com.soarclient.event.impl.RenderBlurEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Page {

	protected float x, y, width, height;
	private final String name, icon;

	public Page(String name, String icon, float x, float y, float width, float height) {
		this.name = name;
		this.icon = icon;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public Page(String name, String icon) {

		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

		this.name = name;
		this.icon = icon;
		this.x = 0;
		this.y = 0;
		this.width = sr.getScaledWidth();
		this.height = sr.getScaledHeight();
	}
	
	public Page() {
		this("", "");
	}
	
	public void draw(int mouseX, int mouseY) {
	}
	
	public void drawBlur(RenderBlurEvent evemt) {
	}

	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	}

	public String getName() {
		return name;
	}

	public String getIcon() {
		return icon;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
}
