package com.soarclient.gui;

import net.minecraft.client.Minecraft;

public class Page {

	protected Minecraft mc = Minecraft.getMinecraft();
	protected float x, y, width, height;
	private String title, icon;
	private PageDirection direction;

	public Page(PageDirection direction, String title, String icon, float x, float y, float width, float height) {
		this.direction = direction;
		this.title = title;
		this.icon = icon;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public Page(String title, String icon, float x, float y, float width, float height) {
		this(PageDirection.NONE, title, icon, x, y, width, height);
	}

	public void init() {
	}

	public void draw(int mouseX, int mouseY) {
	}

	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	}

	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
	}

	public void keyTyped(char typedChar, int keyCode) {
	}

	public void onClosed() {
	}

	public String getTitle() {
		return title;
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

	public PageDirection getDirection() {
		return direction;
	}

	public void setDirection(PageDirection direction) {
		this.direction = direction;
	}
}