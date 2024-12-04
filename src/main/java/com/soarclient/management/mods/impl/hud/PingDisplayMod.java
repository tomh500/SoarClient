package com.soarclient.management.mods.impl.hud;

import com.soarclient.management.mods.api.SimpleHUDMod;
import com.soarclient.nanovg.font.Icon;
import com.soarclient.utils.network.ServerUtils;

public class PingDisplayMod extends SimpleHUDMod {

	public PingDisplayMod() {
		super("mod.pingdisplay.name", "mod.pingdisplay.description", Icon.WIFI);
	}

	@Override
	public String getText() {
		return ServerUtils.getPing() + " ms";
	}

	@Override
	public String getIcon() {
		return Icon.WIFI;
	}
}
