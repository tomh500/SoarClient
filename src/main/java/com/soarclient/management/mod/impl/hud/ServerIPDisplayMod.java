package com.soarclient.management.mod.impl.hud;

import com.soarclient.management.mod.api.hud.SimpleHUDMod;
import com.soarclient.skia.font.Icon;
import com.soarclient.utils.ServerUtils;

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