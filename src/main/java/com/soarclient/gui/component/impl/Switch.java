package com.soarclient.gui.component.impl;

import java.awt.Color;

import com.soarclient.Soar;
import com.soarclient.animation.SimpleAnimation;
import com.soarclient.gui.component.Component;
import com.soarclient.gui.component.handler.impl.SwitchHandler;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.utils.ColorUtils;
import com.soarclient.utils.mouse.MouseUtils;

public class Switch extends Component {

	private SimpleAnimation animation = new SimpleAnimation();

	private boolean enabled;

	public Switch(float x, float y, boolean enabled) {
		super(x, y);
		this.enabled = enabled;
		width = 52;
		height = 32;
	}

	@Override
	public void draw(int mouseX, int mouseY) {

		NanoVGHelper nvg = NanoVGHelper.getInstance();
		ColorPalette palette = Soar.getInstance().getColorManager().getPalette();

		animation.onTick(enabled ? 1 : 0, 16);

		nvg.drawRoundedRect(x, y, width, height, 16, palette.getSurfaceContainerHigh());
		nvg.drawOutline(x, y, width, height, 16, 2,
				ColorUtils.applyAlpha(palette.getOutline(), 1F - animation.getValue()));
		nvg.drawRoundedRect(x, y, width, height, 16, ColorUtils.applyAlpha(palette.getPrimary(), animation.getValue()));

		Color cColor = enabled ? palette.getSurface() : palette.getOutline();

		nvg.drawCircle(x + 16 + (animation.getValue() * 20), y + 16, 8 + (4 * animation.getValue()), cColor);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

		if (MouseUtils.isInside(mouseX, mouseY, x, y, width, height) && mouseButton == 0) {
			enabled = !enabled;

			if (handler instanceof SwitchHandler) {

				SwitchHandler sHandler = (SwitchHandler) handler;

				if (enabled) {
					sHandler.onEnable();
				} else {
					sHandler.onDisable();
				}
			}
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
