package com.soarclient.management.mod.impl.hud;

import com.soarclient.event.EventBus;
import com.soarclient.event.impl.RenderSkiaEvent;
import com.soarclient.management.mod.api.hud.SimpleHUDMod;
import com.soarclient.skia.font.Icon;

public class CoordsMod extends SimpleHUDMod {

	public CoordsMod() {
		super("mod.coords.name", "mod.coords.description", Icon.PIN_DROP);
	}

	public final EventBus.EventListener<RenderSkiaEvent> onRenderSkia = event -> {
		this.draw();
	};

	@Override
	public String getText() {
		return "X: " + (int) client.player.getX() + " Y: " + (int) client.player.getY() + " Z: "
				+ (int) client.player.getZ();
	}

	@Override
	public String getIcon() {
		return Icon.PIN_DROP;
	}
}