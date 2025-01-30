package com.soarclient;

import com.soarclient.event.EventBus;
import com.soarclient.event.client.ClientTickEvent;

public class SoarHandler {

	public final EventBus.EventListener<ClientTickEvent> onClientTick = event -> {
		Soar.getInstance().getColorManager().onTick();
	};
}
