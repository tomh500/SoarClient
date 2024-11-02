package com.soarclient.management.mods.impl.hud;

import java.text.DecimalFormat;

import com.soarclient.management.mods.api.SimpleHUDMod;
import com.soarclient.nanovg.font.Icon;

public class PitchDisplayMod extends SimpleHUDMod {

	private DecimalFormat df = new DecimalFormat("0.##");

	public PitchDisplayMod() {
		super("mod.pitchdisplay.name", "mod.pitchdisplay.description", Icon.ARROW_UPWARD);
	}

	@Override
	public String getText() {
		return "Pitch: " + df.format(mc.thePlayer.rotationPitch);
	}

	@Override
	public String getIcon() {
		return Icon.ARROW_UPWARD;
	}
}
