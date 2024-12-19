package com.soarclient.management.mods.impl.hud;

import com.soarclient.management.mods.api.SimpleHUDMod;
import com.soarclient.nanovg.font.Icon;

import net.minecraft.client.Minecraft;

public class FPSDisplayMod extends SimpleHUDMod {

	public FPSDisplayMod() {
		super("mod.fpsdisplay.name", "mod.fpsdisplay.description", Icon.MONITOR);
	}

	@Override
	public String getText() {
		return Minecraft.getDebugFPS() + " FPS";
	}

	@Override
	public String getIcon() {
		return Icon.MONITOR;
	}
}
