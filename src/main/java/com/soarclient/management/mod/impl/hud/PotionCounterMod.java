package com.soarclient.management.mod.impl.hud;

import com.soarclient.management.mod.api.hud.SimpleHUDMod;
import com.soarclient.skia.font.Icon;
import com.soarclient.utils.PlayerUtils;

import net.minecraft.potion.Potion;

public class PotionCounterMod extends SimpleHUDMod {

	public PotionCounterMod() {
		super("mod.potioncounter.name", "mod.potioncounter.description", Icon.EXPERIMENT);
	}

	@Override
	public String getText() {

		int amount = PlayerUtils.getPotionsFromInventory(Potion.heal);

		return amount + " " + (amount <= 1 ? "pot" : "pots");
	}

	@Override
	public String getIcon() {
		return Icon.EXPERIMENT;
	}
}