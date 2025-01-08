package com.soarclient.event.impl;

import com.soarclient.event.api.AbstractEvent;

public interface RenderGameOverlayEventListener {

	void onRenderGameOverlay();
	
	class RenderGameOverlayEvent extends AbstractEvent<RenderGameOverlayEventListener> {

		public static final int ID = 2;
		
		@Override
		public void call(RenderGameOverlayEventListener listener) {
			listener.onRenderGameOverlay();
		}
	}
}
