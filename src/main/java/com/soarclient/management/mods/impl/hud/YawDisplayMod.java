package com.soarclient.management.mods.impl.hud;

import java.text.DecimalFormat;

import com.soarclient.management.mods.api.SimpleHUDMod;
import com.soarclient.nanovg.font.Icon;

public class YawDisplayMod extends SimpleHUDMod {

	private DecimalFormat df = new DecimalFormat("0.##");

	public YawDisplayMod() {
		super("mod.yawdisplay.name", "mod.yawdisplay.description", Icon.ARROW_RANGE);
	}

	@Override
	public String getText() {
		return "Yaw: " + df.format(mc.thePlayer.rotationYaw);
	}

	@Override
	public String getIcon() {
		return Icon.ARROW_RANGE;
	}
}
