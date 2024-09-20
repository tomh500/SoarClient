package com.soarclient.stk.component;

public abstract class Component {
	public abstract void draw(int mouseX, int mouseY);
	public abstract void mouseClicked(int mouesX, int mouseY, int mouseButton);
	public abstract void mouseReleased(int mouseX, int mouseY, int mouseButton);
}
