package com.soarclient.management.mods.impl.hud;

import com.soarclient.management.mods.api.SimpleHUDMod;
import com.soarclient.nanovg.font.Icon;

public class NameDisplayMod extends SimpleHUDMod {

	public NameDisplayMod() {
		super("mod.namedisplay.name", "mod.namedisplay.description", Icon.PERSON);
	}

	@Override
	public String getText() {
		return "Name: " + mc.thePlayer.getGameProfile().getName();
	}

	@Override
	public String getIcon() {
		return Icon.PERSON;
	}
}
