package com.soarclient.stk.component;

import com.soarclient.stk.api.Align;

public abstract class Component {

	protected float x, y, width, height;
	private Align align;

	public Component(Align align) {
		this.x = 0;
		this.y = 0;
		this.width = 0;
		this.height = 0;
		this.align = align;
	}

	public abstract void draw(int mouseX, int mouseY);

	public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton);

	public abstract void mouseReleased(int mouseX, int mouseY, int mouseButton);

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

	public Align getAlign() {
		return align;
	}
}
