package com.soarclient.stk.api;

import java.util.ArrayList;
import java.util.List;

import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.stk.component.Component;

public class Window {

	private List<Component> components = new ArrayList<>();

	private Size size;
	private ColorPalette palette;
	private Align align;

	public Window(Size size, ColorPalette palette, Align align) {
		this.size = size;
		this.palette = palette;
		this.align = align;
	}

	public void draw(int mouseX, int mouseY) {

		NanoVGHelper nvg = NanoVGHelper.getInstance();

		nvg.setupAndDraw(() -> {

			float[] pos = align.getPosition();
			int cx = 1;
			int cy = 1;

			if (align.equals(Align.MIDDLE_CENTER)) {
				cx = 2;
				cy = 2;
			} else if (align.equals(Align.BOTTOM_CENTER) || align.equals(Align.TOP_CENTER)) {
				cx = 2;
			}

			nvg.drawRoundedRect(pos[0] - (size.getWidth() / cx), pos[1] - (size.getHeight() / cy), size.getWidth(),
					size.getHeight(), 20, palette.getSurface());

			for (Component c : components) {
				c.draw(mouseX, mouseY);
			}
		});
	}

	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		for (Component c : components) {
			c.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}

	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		for (Component c : components) {
			c.mouseReleased(mouseX, mouseY, mouseButton);
		}
	}
}
