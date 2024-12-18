package com.soarclient.management.mods.impl.hud;

import com.soarclient.management.mods.api.SimpleHUDMod;
import com.soarclient.nanovg.font.Icon;
import com.soarclient.utils.PlayerUtils;

public class GameModeDisplayMod extends SimpleHUDMod {

	public GameModeDisplayMod() {
		super("mod.gamemodedisplay.name", "mod.gamemodedisplay.description", Icon.SPORTS_ESPORTS);
	}

	@Override
	public String getText() {

		String prefix = "Mode: ";

		if (PlayerUtils.isSurvival()) {
			return prefix + "Survival";
		}

		if (PlayerUtils.isCreative()) {
			return prefix + "Creative";
		}

		if (PlayerUtils.isSpectator()) {
			return prefix + "Spectator";
		}

		return prefix + "Error";
	}

	@Override
	public String getIcon() {
		return Icon.SPORTS_ESPORTS;
	}
}
