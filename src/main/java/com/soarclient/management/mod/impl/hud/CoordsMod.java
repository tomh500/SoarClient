package com.soarclient.management.mod.impl.hud;

import com.soarclient.event.EventBus;
import com.soarclient.event.impl.RenderGameOverlayEventListener;
import com.soarclient.management.mod.api.hud.SimpleHUDMod;
import com.soarclient.skia.font.Icon;

public class CoordsMod extends SimpleHUDMod implements RenderGameOverlayEventListener {

	public CoordsMod() {
		super("mod.coords.name", "mod.coords.description", Icon.PIN_DROP);
	}

	@Override
	public void onRenderGameOverlay() {
		super.draw();
	}
	
	@Override
	public void onEnable() {
		EventBus.getInstance().register(this, RenderGameOverlayEvent.ID);
	}

	@Override
	public void onDisable() {
		EventBus.getInstance().unregister(this, RenderGameOverlayEvent.ID);
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