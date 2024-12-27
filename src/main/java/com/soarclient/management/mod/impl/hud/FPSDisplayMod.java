package com.soarclient.management.mod.impl.hud;

import com.soarclient.management.mod.api.hud.SimpleHUDMod;
import com.soarclient.skia.font.Icon;

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