package com.soarclient;

import com.soarclient.event.EventBus;
import com.soarclient.event.client.ClientTickEvent;
import com.soarclient.event.server.impl.GameJoinEvent;

public class SoarHandler {

	public final EventBus.EventListener<ClientTickEvent> onClientTick = event -> {
		Soar.getInstance().getColorManager().onTick();
		Soar.getInstance().getHypixelManager().update();
	};
	
	public final EventBus.EventListener<GameJoinEvent> onGameJoin = event -> {
		Soar.getInstance().getHypixelManager().clear();
	};
}
