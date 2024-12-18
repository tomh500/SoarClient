package com.soarclient.management.mods.impl.hud;

import com.soarclient.management.mods.api.SimpleHUDMod;
import com.soarclient.nanovg.font.Icon;

public class HealthDisplayMod extends SimpleHUDMod {

	public HealthDisplayMod() {
		super("mod.healthdisplay.name", "mod.healthdisplay.description", Icon.FAVORITE);
	}

	@Override
	public String getText() {
		return (int) mc.thePlayer.getHealth() + " Health";
	}

	@Override
	public String getIcon() {
		return Icon.FAVORITE;
	}
}
