package com.soarclient.event.impl;

import com.soarclient.event.api.AbstractEvent;

public interface TickEventListener {

	void onClientTick();
	void onRenderTick();
	
	class ClientTickEvent extends AbstractEvent<TickEventListener> {

		public static final int ID = 0;
		
		@Override
		public void call(TickEventListener listener) {
			listener.onClientTick();
		}
	}
	
	class RenderTickEvent extends AbstractEvent<TickEventListener> {

		public static final int ID = 1;
		
		@Override
		public void call(TickEventListener listener) {
			listener.onRenderTick();
		}
	}
}
