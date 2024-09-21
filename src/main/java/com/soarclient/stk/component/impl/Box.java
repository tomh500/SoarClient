package com.soarclient.stk.component.impl;

import java.util.ArrayList;
import java.util.List;

import com.soarclient.stk.api.Align;
import com.soarclient.stk.component.Component;

public class Box extends Component {

	protected List<Component> components = new ArrayList<>();

	public Box(float width, float height, Align align) {
		super(align);
		this.width = width;
		this.height = height;
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		for (Component c : components) {
			c.draw(mouseX, mouseY);
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		for (Component c : components) {
			c.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		for (Component c : components) {
			c.mouseReleased(mouseX, mouseY, mouseButton);
		}
	}

	public Box addComponent(Component component) {
		components.add(component);
		return this;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
}
