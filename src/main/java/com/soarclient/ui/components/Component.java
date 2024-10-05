package com.soarclient.ui.components;

import com.soarclient.event.impl.RenderBlurEvent;

public class Component {

	protected float x, y, width, height;

	public Component(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void draw(int mouseX, int mouseY) {
	}

	public void drawBlur(RenderBlurEvent evemt) {
	}

	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
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
