package com.soarclient.management.mod.api.hud;

import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.RenderSkiaEvent;
import com.soarclient.management.mod.settings.impl.BooleanSetting;
import com.soarclient.skia.Skia;
import com.soarclient.skia.font.Fonts;
import com.soarclient.skia.font.Icon;

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
		float textWidth = Skia.getTextWidth(getText(), Fonts.getRegular(fontSize));
		float textHeight = Skia.getTextHeight(getText(), Fonts.getRegular(fontSize));
		float iconWidth = Skia.getTextWidth(getIcon(), Fonts.getIcon(iconSize));
		float width = textWidth + (padding * 2) + (hasIcon ? iconWidth + 4 : 0);
		float height = fontSize + (padding * 2) - 1.5F;
		
		this.begin();
		this.drawBackground(getX(), getY(), width, height);
		
		if(hasIcon) {
			this.drawText(getIcon(), getX() + padding, getY() + padding - 1F, Fonts.getIcon(iconSize));
		}
		
		this.drawText(getText(), getX() + padding + (hasIcon ? iconWidth + 4 : 0), getY() + (height / 2) - (textHeight / 2) - 0.5F, Fonts.getRegular(fontSize));
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
