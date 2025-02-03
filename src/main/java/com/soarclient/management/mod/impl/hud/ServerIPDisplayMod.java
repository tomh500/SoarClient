package com.soarclient.management.mod.impl.hud;

import com.soarclient.event.EventBus;
import com.soarclient.event.client.RenderSkiaEvent;
import com.soarclient.management.mod.api.hud.SimpleHUDMod;
import com.soarclient.skia.font.Icon;

public class ServerIPDisplayMod extends SimpleHUDMod {

	public ServerIPDisplayMod() {
		super("mod.serveripdisplay.name", "mod.serveripdisplay.description", Icon.DNS);
	}

	public final EventBus.EventListener<RenderSkiaEvent> onRenderSkia = event -> {
		this.draw();
	};

	@Override
	public String getText() {

		if (client.isConnectedToLocalServer()) {
			return "Singleplayer";
		}

		if (client.getCurrentServerEntry() == null) {
			return "None";
		}

		return client.getCurrentServerEntry().address;
	}

	@Override
	public String getIcon() {
		return Icon.DNS;
	}
}