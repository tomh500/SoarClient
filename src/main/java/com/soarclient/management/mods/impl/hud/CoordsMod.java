package com.soarclient.management.mods.impl.hud;

import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.RenderGameOverlayEvent;
import com.soarclient.management.mods.api.SimpleHUDMod;
import com.soarclient.nanovg.font.Icon;

public class CoordsMod extends SimpleHUDMod {

	public CoordsMod() {
		super("mod.coords.name", "mod.coords.description", Icon.PIN_DROP);
	}

	@EventHandler
	public void onRenderGameOverlay(RenderGameOverlayEvent event) {
		super.onRenderGameOverlay(event);
	}

	@Override
	public String getText() {
		return "X: " + (int) mc.thePlayer.posX + " Y: " + (int) mc.thePlayer.posY + " Z: " + (int) mc.thePlayer.posZ;
	}

	@Override
	public String getIcon() {
		return Icon.PIN_DROP;
	}
}
