package com.soarclient.management.mod.api.hud;

import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.RenderSkiaEvent;
import com.soarclient.management.mod.settings.impl.BooleanSetting;
import com.soarclient.skia.Skia;
import com.soarclient.skia.font.Fonts;
import com.soarclient.skia.font.Icon;

import io.github.humbleui.skija.FontMetrics;
import io.github.humbleui.types.Rect;

public abstract class SimpleHUDMod extends HUDMod {

	private BooleanSetting iconSetting = new BooleanSetting("setting.icon", "setting.icon.description",
			Icon.NEW_RELEASES, this, true);

	public SimpleHUDMod(String name, String description, String icon) {
		super(name, description, icon);
	}

	@EventHandler
	public void onRenderSkia(RenderSkiaEvent event) {

		float fontSize = 9;
		float iconSize = 10.5F;
		float padding = 5;
		boolean hasIcon = getIcon() != null && iconSetting.isEnabled();
		Rect textBounds = Skia.getTextBounds(getText(), Fonts.getRegular(fontSize));
		Rect iconBounds = Skia.getTextBounds(getIcon(), Fonts.getIcon(iconSize));
		FontMetrics textMetrics = Fonts.getRegular(fontSize).getMetrics();
		float width = textBounds.getWidth() + (padding * 2) + (hasIcon ? iconBounds.getWidth() + 4 : 0);
		float height = fontSize + (padding * 2) - 1.5F;

		this.begin();
		this.drawBackground(getX(), getY(), width, height);

		if (hasIcon) {
			this.drawText(getIcon(), getX() + padding, getY() + (height / 2) - (iconBounds.getHeight() / 2),
					Fonts.getIcon(iconSize));
		}

		this.drawText(getText(), getX() + padding + (hasIcon ? iconBounds.getWidth() + 4 : 0),
				getY() + (height / 2) - ((textMetrics.getCapHeight() + 0.5F) / 2), Fonts.getRegular(fontSize));
		this.finish();

		position.setSize(width, height);
	}

	@Override
	public float getRadius() {
		return 6;
	}

	public abstract String getText();

	public abstract String getIcon();
}
