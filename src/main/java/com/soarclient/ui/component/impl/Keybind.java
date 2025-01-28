package com.soarclient.ui.component.impl;

import org.lwjgl.glfw.GLFW;

import com.soarclient.Soar;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.skia.Skia;
import com.soarclient.skia.font.Fonts;
import com.soarclient.ui.component.Component;
import com.soarclient.ui.component.api.PressAnimation;
import com.soarclient.ui.component.handler.impl.KeybindHandler;
import com.soarclient.utils.mouse.MouseUtils;

public class Keybind extends Component {

	private PressAnimation pressAnimation = new PressAnimation();
	private int[] pressedPos;

	private boolean binding;
	private int keyCode;

	public Keybind(float x, float y, int keyCode) {
		super(x, y);
		this.keyCode = keyCode;
		width = 126;
		height = 32;
		pressedPos = new int[] { 0, 0 };
	}

	@Override
	public void draw(double mouseX, double mouseY) {

		ColorPalette palette = Soar.getInstance().getColorManager().getPalette();

		Skia.drawRoundedRect(x, y, width, height, 12, palette.getPrimary());
		Skia.save();
		Skia.clip(x, y, width, height, 12);
		pressAnimation.draw(x + pressedPos[0], y + pressedPos[1], width, height, palette.getPrimaryContainer(), 0.12F);
		Skia.restore();
		Skia.drawFullCenteredText(binding ? "..." : GLFW.glfwGetKeyName(keyCode, 0), x + (width / 2), y + (height / 2),
				palette.getSurface(), Fonts.getMedium(14));
	}

	@Override
	public void mousePressed(double mouseX, double mouseY, int button) {
		if (MouseUtils.isInside(mouseX, mouseY, x, y, width, height) && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			pressAnimation.onPressed(mouseX, mouseY, x, y);
		}
	}

	@Override
	public void mouseReleased(double mouseX, double mouseY, int button) {

		if (MouseUtils.isInside(mouseX, mouseY, x, y, width, height) && !binding) {
			if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
				binding = true;
			}
		}

		if (binding) {

			if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
				setKeyCode(GLFW.GLFW_KEY_UNKNOWN);
			} else if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT && button != GLFW.GLFW_MOUSE_BUTTON_RIGHT
					&& button != GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
				setKeyCode(button);
			}

			binding = false;
		}

		pressAnimation.onReleased(mouseX, mouseY, x, y);
	}

	@Override
	public void keyPressed(int keyCode, int scanCode, int modifiers) {
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
