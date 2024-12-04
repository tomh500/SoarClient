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
		
		float padding = 5;
		float iconPadding = 3;
		float fontSize = 9;
		float iconSize = 10.5F;
		boolean hasIcon = getIcon() != null && iconSetting.isEnabled();
		float addX = hasIcon ? iconSize + iconPadding : 0;
		float width = renderer.getTextWidth(getText(), fontSize, Fonts.REGULAR) + (padding * 2) + addX;
		float height = fontSize + (padding * 2) - 1.5F;
		
		NanoVGHelper.getInstance().setupAndDraw(() -> {
			
			renderer.drawBackground(width, height);
			
			if(hasIcon) {
				renderer.drawText(getIcon(), padding, padding - 0.5F, iconSize, Fonts.ICON);
			}
			
			renderer.drawText(getText(), padding + addX, padding, fontSize, Fonts.REGULAR);
		});
		
		position.setSize(width, height);
	}

	public abstract String getText();

	public abstract String getIcon();
}