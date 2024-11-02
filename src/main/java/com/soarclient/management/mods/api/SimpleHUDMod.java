package com.soarclient.management.mods.api;

import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.RenderGameOverlayEvent;
import com.soarclient.management.mods.settings.impl.BooleanSetting;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.nanovg.font.Fonts;
import com.soarclient.nanovg.font.Icon;

public abstract class SimpleHUDMod extends HUDMod {

	private BooleanSetting iconSetting = new BooleanSetting("setting.icon", "setting.icon.description",
			Icon.NEW_RELEASES, this, true);

	public SimpleHUDMod(String name, String description, String icon) {
		super(name, description, icon);
	}

	@EventHandler
	public void onRenderGameOverlay(RenderGameOverlayEvent event) {

		NanoVGHelper nvg = NanoVGHelper.getInstance();

		boolean hasIcon = getIcon() != null && iconSetting.isEnabled();
		float iconWidth = hasIcon ? renderer.getTextWidth(getIcon(), 10.5F, Fonts.ICON) + 3 : 0;
		float width = renderer.getTextWidth(getText(), 9, Fonts.REGULAR) + 10 + iconWidth;

		nvg.setupAndDraw(() -> {

			renderer.drawBackground(width, 18);

			if (hasIcon) {
				renderer.drawText(getIcon(), 5.5F, 4F, 10.5F, Fonts.ICON);
			}

			renderer.drawText(getText(), 5.5F + iconWidth, 5.5F, 9, Fonts.REGULAR);
		});

		position.setSize(width, 18);
	}

	public abstract String getText();

	public abstract String getIcon();
}