package com.soarclient.gui.component;

import com.soarclient.gui.component.handler.ComponentHandler;

import net.minecraft.client.Minecraft;

public class Component {

	protected Minecraft mc = Minecraft.getMinecraft();
	protected ComponentHandler handler;
	protected float x, y, width, height;
	
	public Component(float x, float y) {
		this.x = x;
		this.y = y;
		this.handler = new ComponentHandler() {
		};
	}
	
	public void draw(int mouseX, int mouseY) {
	}

	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	}

	public void keyTyped(char typedChar, int keyCode) {
	}
	
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

	public float getHeight() {
		return height;
	}

	public ComponentHandler getHandler() {
		return handler;
	}

	public void setHandler(ComponentHandler handler) {
		this.handler = handler;
	}
}
