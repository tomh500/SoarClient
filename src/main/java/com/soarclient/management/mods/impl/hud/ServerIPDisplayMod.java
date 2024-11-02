package com.soarclient.management.mods.impl.hud;

import com.soarclient.management.mods.api.SimpleHUDMod;
import com.soarclient.nanovg.font.Icon;
import com.soarclient.utils.network.ServerUtils;

public class ServerIPDisplayMod extends SimpleHUDMod {

	public ServerIPDisplayMod() {
		super("mod.serveripdisplay.name", "mod.serveripdisplay.description", Icon.DNS);
	}

	@Override
	public String getText() {
		return ServerUtils.getServerIP();
	}

	@Override
	public String getIcon() {
		return Icon.DNS;
	}
}
