package com.soarclient.stk.component.impl.checkbox;

import java.awt.Color;

import com.soarclient.animation.SimpleAnimation;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.nanovg.font.Fonts;
import com.soarclient.nanovg.font.Icon;
import com.soarclient.stk.api.Align;
import com.soarclient.stk.component.Component;
import com.soarclient.stk.palette.ColorHelper;
import com.soarclient.stk.palette.ColorPalette;
import com.soarclient.utils.ColorUtils;
import com.soarclient.utils.StyleUtils;
import com.soarclient.utils.mouse.MouseUtils;

public class Checkbox extends Component {

	private SimpleAnimation animation = new SimpleAnimation();

	private CheckboxHandler handler;
	private Component parent;
	private boolean disabled;
	private boolean selected;

	public Checkbox(boolean selected, Component parent, Align align) {
		super(align);
		this.parent = parent;
		this.width = StyleUtils.dpToPixel(18);
		this.height = StyleUtils.dpToPixel(18);
		this.selected = false;

		if (this.parent != null && !align.equals(Align.NONE)) {

			float[] pos = align.getPosition(parent.getWidth(), parent.getHeight());
			float[] space = align.getSpace(StyleUtils.dpToPixel(24));

			this.x = (parent.getX() + pos[0]) - (this.width * align.getX()) + space[0];
			this.y = (parent.getY() + pos[1]) - (this.height * align.getY()) + space[1];
		}
	}

	public Checkbox(Component parent, Align align) {
		this(false, parent, align);
	}

	public Checkbox(boolean selected, Component parent, float x, float y) {
		this(selected, parent, Align.NONE);
		this.x = x;
		this.y = y;
	}

	public Checkbox(Component parent, float x, float y) {
		this(false, parent, Align.NONE);
		this.x = x;
		this.y = y;
	}

	@Override
	public void draw(int mouseX, int mouseY) {

		NanoVGHelper nvg = NanoVGHelper.getInstance();
		ColorPalette palette = ColorHelper.getPalette();

		float iconSize = StyleUtils.dpToPixel(18);
		float ow = StyleUtils.dpToPixel(2);
		Color disableColor = ColorUtils.applyAlpha(palette.getOnSurface(), 0.38F);

		animation.onTick(selected ? 1 : 0, 12);

		nvg.drawRect(x, y, width, height, disabled ? disableColor : palette.getOutline());
		nvg.drawRect(x + ow, y + ow, width - (ow * 2), height - (ow * 2),
				ColorUtils.applyAlpha(palette.getOnSurface(), 0.38F));
		nvg.drawRect(x, y, width, height,
				disabled ? disableColor : ColorUtils.applyAlpha(palette.getPrimary(), animation.getValue()));
		nvg.drawText(Icon.CHECK, x + StyleUtils.dpToPixel(2), y + StyleUtils.dpToPixel(3),
				disabled ? ColorUtils.applyAlpha(disableColor, selected ? 0.38F : 0F)
						: ColorUtils.applyAlpha(palette.getSurface(), animation.getValue()),
				iconSize, Fonts.ICON);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (MouseUtils.isInside(mouseX, mouseY, x, y, width, height) && mouseButton == 0 && !disabled) {
			selected = !selected;

			if (handler != null) {
				if (selected) {
					handler.onSelect();
				} else {
					handler.onUnSelect();
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

	public boolean isSelected() {
		return selected;
	}

	public void setHandler(CheckboxHandler handler) {
		this.handler = handler;
	}
}
