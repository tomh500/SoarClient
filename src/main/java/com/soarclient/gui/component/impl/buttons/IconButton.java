package com.soarclient.gui.component.impl.buttons;

import java.awt.Color;

import com.soarclient.Soar;
import com.soarclient.animation.SimpleAnimation;
import com.soarclient.gui.component.Component;
import com.soarclient.gui.component.api.Size;
import com.soarclient.gui.component.api.Style;
import com.soarclient.gui.component.handler.impl.ButtonHandler;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.nanovg.font.Fonts;
import com.soarclient.utils.ColorUtils;
import com.soarclient.utils.mouse.MouseUtils;

public class IconButton extends Component {

	private SimpleAnimation animation = new SimpleAnimation();
	
	private String icon;
	private Size size;
	private Style style;
	
	public IconButton(String icon, float x, float y, Size size, Style style) {
		super(x, y);
		
		this.icon = icon;
		this.size = size;
		this.style = style;
		float[] s = getPanelSize();
		
		width = s[0];
		height = s[1];
	}
	
	@Override
	public void draw(int mouseX, int mouseY) {
		
		NanoVGHelper nvg = NanoVGHelper.getInstance();
		Color[] c = getColor();
		boolean focus = MouseUtils.isInside(mouseX, mouseY, x, y, width, height);
		
		animation.onTick(focus ? 20 : 0, 10);
		
		nvg.drawRoundedRect(x, y, width, height, getRadius(), c[0]);
		nvg.drawAlignCenteredText(icon, x + (width / 2), y + (height / 2), c[1], getFontSize(), Fonts.ICON_FILL);
		nvg.drawRoundedRect(x, y, width, height, getRadius(), ColorUtils.applyAlpha(c[1], (int) (animation.getValue())));
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if(MouseUtils.isInside(mouseX, mouseY, x, y, width, height) && mouseButton == 0) {
			if(handler instanceof ButtonHandler) {
				((ButtonHandler) handler).onClicked();
			}
		}
	}
	
	private float[] getPanelSize() {
		switch(size) {
			case LARGE:
				return new float[] {96, 96};
			case NORMAL:
				return new float[] {56, 56};
			case SMALL:
				return new float[] {40, 40};
			default:
				return new float[] {0, 0};
		}
	}
	
	private float getFontSize() {
		switch(size) {
			case LARGE:
				return 36;
			case NORMAL:
				return 24;
			case SMALL:
				return 24;
			default:
				return 0F;
		}
	}
	
	private float getRadius() {
		switch(size) {
			case LARGE:
				return 28;
			case NORMAL:
				return 16;
			case SMALL:
				return 12;
			default:
				return 0F;
		}
	}
	
	private Color[] getColor() {
		
		ColorPalette palette = Soar.getInstance().getColorManager().getPalette();
		
		switch(style) {
			case PRIMARY:
				return new Color[] { palette.getPrimaryContainer(), palette.getOnPrimaryContainer() };
			case SECONDARY:
				return new Color[] { palette.getSecondaryContainer(), palette.getOnSecondaryContainer() };
			case SURFACE:
				return new Color[] { palette.getSurfaceContainer(), palette.getPrimary() };
			case TERTIARY:
				return new Color[] { palette.getTertiaryContainer(), palette.getOnTertiaryContainer() };
			default:
				return new Color[] { Color.RED, Color.RED };
		}
	}
}
