package com.soarclient.gui.api;

import net.minecraft.client.Minecraft;

public abstract class Page {
	
	protected Minecraft mc = Minecraft.getMinecraft();
	
	protected float x, y, width, height;
	private String title, icon;
	private PageGui parent;
	
	public Page(PageGui parent, String title, String icon, float x, float y, float width, float height) {
		this.parent = parent;
		this.title = title;
		this.icon = icon;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public abstract void init();
	public abstract void draw(float mouseX, float mouseY);
	public abstract void mousePressed(float mouseX, float mouseY, int mouseButton);
	public abstract void mouseReleased(float mouseX, float mouseY, int mouseButton);
	public abstract void keyTyped(char typedChar, int keyCode);
	public abstract void onClosed();
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public String getTitle() {
		return title;
	}

	public String getIcon() {
		return icon;
	}
	
	public void setCurrentPage(Page page) {
		parent.setCurrentPage(page);
	}
	
	public void setCurrentPage(Class<? extends Page> clazz) {
		parent.setCurrentPage(clazz);
	}
}
