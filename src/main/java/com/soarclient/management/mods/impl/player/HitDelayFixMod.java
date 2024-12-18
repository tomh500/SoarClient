package com.soarclient.management.mods.impl.player;

import com.soarclient.management.mods.Mod;
import com.soarclient.management.mods.ModCategory;
import com.soarclient.nanovg.font.Icon;

public class HitDelayFixMod extends Mod {

	private static HitDelayFixMod instance;

	public HitDelayFixMod() {
		super("mod.hitdelayfix.name", "mod.hitdelayfix.description", Icon.TIMER_OFF, ModCategory.PLAYER);

		instance = this;
	}

	public static HitDelayFixMod getInstance() {
		return instance;
	}
}
