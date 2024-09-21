package com.soarclient.stk.component.impl.tswitch;

import java.awt.Color;

import com.soarclient.animation.SimpleAnimation;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.stk.api.Align;
import com.soarclient.stk.component.Component;
import com.soarclient.stk.palette.ColorHelper;
import com.soarclient.stk.palette.ColorPalette;
import com.soarclient.utils.ColorUtils;
import com.soarclient.utils.StyleUtils;
import com.soarclient.utils.mouse.MouseUtils;

public class Switch extends Component {

	private SimpleAnimation moveAnimation = new SimpleAnimation();
	private SimpleAnimation animation = new SimpleAnimation();
	private SimpleAnimation radiusAnimation = new SimpleAnimation();

	private Component parent;
	private Switch.State state;
	private boolean disabled;
	private SwitchHandler handler;

	public Switch(State state, Component parent, Align align) {
		super(align);
		this.state = state;
		this.parent = parent;
		this.disabled = false;
		this.width = StyleUtils.dpToPixel(52);
		this.height = StyleUtils.dpToPixel(32);

		if (this.parent != null && !align.equals(Align.NONE)) {

			float[] pos = align.getPosition(parent.getWidth(), parent.getHeight());
			float[] space = align.getSpace(StyleUtils.dpToPixel(24));

			this.x = (parent.getX() + pos[0]) - (this.width * align.getX()) + space[0];
			this.y = (parent.getY() + pos[1]) - (this.height * align.getY()) + space[1];
		}
	}

	public Switch(Component parent, Align align) {
		this(State.DISABLE, parent, align);
	}

	public Switch(State state, float x, float y) {
		this(state, null, Align.NONE);
		this.x = x;
		this.y = y;
	}
	
	public Switch(float x, float y) {
		this(State.DISABLE, null, Align.NONE);
		this.x = x;
		this.y = y;
	}

	@Override
	public void draw(int mouseX, int mouseY) {

		NanoVGHelper nvg = NanoVGHelper.getInstance();

		ColorPalette palette = ColorHelper.getPalette();
		Color btColor = state.equals(State.ENABLE) ? palette.getOnPrimary() : palette.getOutline();
		float ow = StyleUtils.dpToPixel(2);
		float radius = StyleUtils.dpToPixel(state.equals(State.ENABLE) ? 24 : 16);

		moveAnimation.onTick(state.equals(State.ENABLE) ? x + width - StyleUtils.dpToPixel(6) - (radius / 2)
				: x + StyleUtils.dpToPixel(6) + (radius / 2), 12);
		animation.onTick(state.equals(State.ENABLE) ? 1 : 0, 12);
		radiusAnimation.onTick(radius / 2, 12);

		nvg.drawRoundedRect(x, y, width, height, StyleUtils.dpToPixel(16),
				disabled ? ColorUtils.applyAlpha(palette.getOnSurface(), 0.38F) : palette.getOutline());

		if (!disabled) {
			nvg.drawRoundedRect(x + ow, y + ow, width - (ow * 2), height - (ow * 2), StyleUtils.dpToPixel(16),
					palette.getSurfaceContainerHighest());
			nvg.drawRoundedRect(x, y, width, height, StyleUtils.dpToPixel(16),
					ColorUtils.applyAlpha(palette.getPrimary(), animation.getValue()));
		}

		nvg.drawCircle(moveAnimation.getValue(), y + (height / 2), radiusAnimation.getValue(),
				disabled ? ColorUtils.applyAlpha(btColor, 0.38F) : btColor);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

		if (MouseUtils.isInside(mouseX, mouseY, x, y, width, height) && mouseButton == 0 && !disabled) {
			if (state.equals(State.ENABLE)) {
				state = State.DISABLE;
				if (handler != null) {
					handler.onDisable();
				}
			} else {
				state = State.ENABLE;
				if (handler != null) {
					handler.onEnable();
				}
			}
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {

	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public void setHandler(SwitchHandler handler) {
		this.handler = handler;
	}

	public static enum State {
		DISABLE, ENABLE;
	}
}
