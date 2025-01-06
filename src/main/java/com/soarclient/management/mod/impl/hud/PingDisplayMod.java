package com.soarclient.management.mod.impl.hud;

import com.soarclient.management.mod.api.hud.SimpleHUDMod;
import com.soarclient.skia.font.Icon;
import com.soarclient.utils.ServerUtils;

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