package com.soarclient.stk.api;

import java.util.ArrayList;
import java.util.List;

import com.soarclient.event.EventBus;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.stk.component.Component;
import com.soarclient.stk.renderer.AppleRenderer;
import com.soarclient.stk.shader.blur.GaussianBlur;
import com.soarclient.utils.math.MathUtils;

public class Window extends Component {

	private List<Component> components = new ArrayList<>();
	private GaussianBlur blur = new GaussianBlur(true);

	public Window(float width, float height, Align align) {
		super(align);
		
		float[] pos = align.getPosition();
		
		this.width = MathUtils.dpToPixel(width);
		this.height = MathUtils.dpToPixel(height);
		
		this.x = pos[0] - (this.width * align.getX());
		this.y = pos[1] - (this.height * align.getY());
		
		EventBus.getInstance().register(this);
	}

	public void draw(int mouseX, int mouseY) {

		NanoVGHelper nvg = NanoVGHelper.getInstance();
		
		blur.draw(50);
		
		nvg.setupAndDraw(() -> {

			AppleRenderer.drawWindow(x, y, width, height, MathUtils.dpToPixel(30));

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
