package com.soarclient.stk.component.impl.button;

import java.awt.Color;

import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.nanovg.font.Fonts;
import com.soarclient.stk.api.Align;
import com.soarclient.stk.component.Component;
import com.soarclient.stk.palette.ColorHelper;
import com.soarclient.stk.palette.ColorPalette;
import com.soarclient.utils.ColorUtils;
import com.soarclient.utils.StyleUtils;
import com.soarclient.utils.mouse.MouseUtils;

public class Button extends Component {

	private Component parent;
	private ButtonHandler handler;
	private String text, icon;
	private boolean disabled;
	private ButtonStyle style;

	public Button(String text, String icon, ButtonStyle style, Component parent, Align align) {
		super(align);
		this.updateText(text, icon);
		this.disabled = false;
		this.parent = parent;
		this.height = StyleUtils.dpToPixel(40);

		if (this.parent != null && !align.equals(Align.NONE)) {

			float[] pos = align.getPosition(parent.getWidth(), parent.getHeight());
			float[] space = align.getSpace(StyleUtils.dpToPixel(24));

			this.x = (parent.getX() + pos[0]) - (this.width * align.getX()) + space[0];
			this.y = (parent.getY() + pos[1]) - (this.height * align.getY()) + space[1];
		}

		this.style = style;
	}

	public Button(String text, ButtonStyle style, Component parent, Align align) {
		this(text, null, style, parent, align);
	}

	public Button(String text, String icon, ButtonStyle style, float x, float y) {
		this(text, icon, style, null, Align.NONE);
		this.x = x;
		this.y = y;
	}

	public Button(String text, ButtonStyle style, float x, float y) {
		this(text, null, style, x, y);
	}

	@Override
	public void draw(int mouseX, int mouseY) {

		NanoVGHelper nvg = NanoVGHelper.getInstance();

		ColorPalette palette = ColorHelper.getPalette();

		float size = StyleUtils.dpToPixel(20);
		float iconSize = StyleUtils.dpToPixel(22);
		float iconWidth = icon != null ? nvg.getTextWidth(icon, iconSize, Fonts.ICON) : 0;
		float offset = icon != null ? StyleUtils.dpToPixel(8) + iconWidth : 0;
		Color[] color = getColor();
		Color[] disableColor = new Color[] { ColorUtils.applyAlpha(palette.getOnSurface(), 0.12F),
				ColorUtils.applyAlpha(palette.getOnSurface(), 0.38F) };

		nvg.drawRoundedRect(x, y, width, height, StyleUtils.dpToPixel(16), disabled ? disableColor[0] : color[0]);

		if (icon != null) {
			nvg.drawText(icon, x + StyleUtils.dpToPixel(16),
					y + (nvg.getTextHeight(icon, iconSize, Fonts.ICON) / 2) - 0.5F,
					disabled ? disableColor[1] : color[1], iconSize, Fonts.ICON);
		}

		nvg.drawText(text, x + StyleUtils.dpToPixel(icon != null ? 16 : 24) + offset,
				y + (nvg.getTextHeight(text, size, Fonts.MEDIUM) / 2), disabled ? disableColor[1] : color[1], size,
				Fonts.MEDIUM);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

		if (MouseUtils.isInside(mouseX, mouseY, x, y, width, height) && mouseButton == 0 && !disabled) {
			if (handler != null) {
				handler.onClicked();
			}
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {}

	private Color[] getColor() {

		ColorPalette palette = ColorHelper.getPalette();

		switch (style) {
		case ELEVATED:
			return new Color[] { palette.getSurfaceContainerLow(), palette.getSurfaceTint() };
		case FILLED:
			return new Color[] { palette.getPrimary(), palette.getOnPrimary() };
		case FILLED_TONAL:
			return new Color[] { palette.getSecondaryContainer(), palette.getOnSecondaryContainer() };
		default:
			return new Color[] { palette.getSurfaceContainerLow(), palette.getSurfaceTint() };
		}
	}

	public void updateText(String text, String icon) {

		NanoVGHelper nvg = NanoVGHelper.getInstance();
		float size = StyleUtils.dpToPixel(20);
		float iconSize = StyleUtils.dpToPixel(22);

		this.text = text;
		this.icon = icon;

		float textWidth = nvg.getTextWidth(this.text, size, Fonts.MEDIUM);
		float iconWidth = icon != null ? nvg.getTextWidth(icon, iconSize, Fonts.ICON) : 0;
		float leftSpace = StyleUtils.dpToPixel(this.icon != null ? 16 : 24);
		float rightSpace = StyleUtils.dpToPixel(24);

		this.width = textWidth + leftSpace + rightSpace + iconWidth;
	}

	public Button setHandler(ButtonHandler handler) {
		this.handler = handler;
		return this;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}
