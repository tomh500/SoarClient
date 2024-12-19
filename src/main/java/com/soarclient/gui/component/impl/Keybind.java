package com.soarclient.gui.component.impl;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.soarclient.Soar;
import com.soarclient.gui.component.Component;
import com.soarclient.gui.component.handler.impl.KeybindHandler;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.nanovg.font.Fonts;
import com.soarclient.utils.mouse.MouseUtils;

public class Keybind extends Component {

	private boolean binding;
	private int keyCode;

	public Keybind(float x, float y, int keyCode) {
		super(x, y);
		this.keyCode = keyCode;
		width = 126;
		height = 32;
	}

	@Override
	public void draw(int mouseX, int mouseY) {

		NanoVGHelper nvg = NanoVGHelper.getInstance();
		ColorPalette palette = Soar.getInstance().getColorManager().getPalette();

		nvg.drawRoundedRect(x, y, width, height, 12, palette.getPrimary());
		nvg.drawAlignCenteredText(
				binding ? "..." : keyCode < 0 ? Mouse.getButtonName(keyCode + 100) : Keyboard.getKeyName(keyCode),
				x + (width / 2), y + (height / 2), palette.getSurface(), 14, Fonts.MEDIUM);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

		if (MouseUtils.isInside(mouseX, mouseY, x, y, width, height)) {

			if (mouseButton == 0 && !binding) {
				binding = true;
			} else if (mouseButton == 2 && binding) {
				setKeyCode(Keyboard.KEY_NONE);
				binding = false;
			} else if (binding && mouseButton != 0 && mouseButton != 1 && mouseButton != 2) {
				setKeyCode(-100 + mouseButton);
				binding = false;
			} else {
				binding = false;
			}

		} else if (binding) {

			if (mouseButton != 0 && mouseButton != 1 && mouseButton != 2) {
				setKeyCode(-100 + mouseButton);
			}

			binding = false;
		}
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {

		if (binding) {
			setKeyCode(keyCode);
			this.binding = false;
		}
	}

	public int getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(int keyCode) {

		this.keyCode = keyCode;

		if (handler instanceof KeybindHandler) {
			((KeybindHandler) handler).onBinded(keyCode);
		}
	}
}