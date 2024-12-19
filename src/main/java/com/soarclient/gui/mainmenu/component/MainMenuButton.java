package com.soarclient.gui.mainmenu.component;

import com.soarclient.animation.SimpleAnimation;
import com.soarclient.gui.component.Component;
import com.soarclient.gui.component.handler.impl.ButtonHandler;
import com.soarclient.gui.mainmenu.api.MainMenuAPI;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.nanovg.font.Fonts;
import com.soarclient.utils.ColorUtils;
import com.soarclient.utils.language.I18n;
import com.soarclient.utils.mouse.MouseUtils;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class MainMenuButton extends Component {

	private SimpleAnimation focusAnimation = new SimpleAnimation();

	private String title, icon;

	public MainMenuButton(String title, String icon, float x, float y, float width) {
		super(x, y);
		this.width = width;
		this.height = 20;
		this.title = title;
		this.icon = icon;
	}

	@Override
	public void draw(int mouseX, int mouseY) {

		ColorPalette palette = MainMenuAPI.getPalette();
		NanoVGHelper nvg = NanoVGHelper.getInstance();

		ScaledResolution sr = new ScaledResolution(mc);

		float size = 76;

		focusAnimation.onTick(MouseUtils.isInside(mouseX, mouseY, x, y, width, height) ? 0.8F : 0.6F, 10);

		nvg.drawImage(new ResourceLocation("soar/logo.png"), (sr.getScaledWidth() / 2) - (size / 2),
				sr.getScaledHeight() / 4 + 56 - size - 4, size, size);

		nvg.drawRoundedRect(x, y, width, height, 9,
				ColorUtils.applyAlpha(palette.getBackground(), focusAnimation.getValue()));
		nvg.drawCenteredText(I18n.get(title), x + (width / 2), y + 6, palette.getOnSurface(), 9, Fonts.REGULAR);
		nvg.drawText(icon, x + 6, y + 5.5F, palette.getOnSurface(), 10.5F, Fonts.ICON);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

		if (MouseUtils.isInside(mouseX, mouseY, x, y, width, height) && mouseButton == 0) {
			if (handler instanceof ButtonHandler) {
				((ButtonHandler) handler).onClicked();
			}
		}
	}
}
