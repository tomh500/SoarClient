package com.soarclient.management.mods.impl.hud;

import com.soarclient.management.mods.api.SimpleHUDMod;
import com.soarclient.nanovg.font.Icon;

public class PlayerCounterMod extends SimpleHUDMod {

	public PlayerCounterMod() {
		super("mod.playercounter.name", "mod.playercounter.description", Icon.GROUPS);
	}

	@Override
	public String getText() {
		return "Player: " + mc.thePlayer.sendQueue.getPlayerInfoMap().size();
	}

	@Override
	public String getIcon() {
		return Icon.GROUPS;
	}
}
