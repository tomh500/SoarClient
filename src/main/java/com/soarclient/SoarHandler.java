package com.soarclient;

import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.ClientTickEvent;

public class SoarHandler {

	@EventHandler
	public void onClientTick(ClientTickEvent event) {
		Soar.getInstance().getColorManager().onTick();
	}
}
