package com.soarclient.stk.api;

import java.util.ArrayList;
import java.util.List;

import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.stk.component.Component;
import com.soarclient.stk.palette.ColorHelper;
import com.soarclient.stk.palette.ColorPalette;
import com.soarclient.utils.StyleUtils;

public class Window extends Component {

	private List<Component> components = new ArrayList<>();

	public Window(float width, float height, Align align) {
		super(align);
		
		float[] pos = align.getPosition();
		
		this.width = StyleUtils.dpToPixel(width);
		this.height = StyleUtils.dpToPixel(height);
		
		this.x = pos[0] - (this.width * align.getX());
		this.y = pos[1] - (this.height * align.getY());
	}

	public void draw(int mouseX, int mouseY) {

		NanoVGHelper nvg = NanoVGHelper.getInstance();
		ColorPalette palette = ColorHelper.getPalette();
		
		nvg.setupAndDraw(() -> {

			nvg.drawRoundedRect(x, y, width, height, StyleUtils.dpToPixel(30), palette.getSurface());

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

	public Window addComponent(Component component) {
		components.add(component);
		return this;
	}
}
