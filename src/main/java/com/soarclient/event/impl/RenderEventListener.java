package com.soarclient.event.impl;

import com.soarclient.event.api.AbstractEvent;

public interface RenderEventListener {
	
	void onRenderGameOverlay();
	void onRenderSkia();
	
	class RenderGameOverlayEvent extends AbstractEvent<RenderEventListener> {

		public static final int ID = 2;
		
		@Override
		public void call(RenderEventListener listener) {
			listener.onRenderGameOverlay();
		}
	}
	
	class RenderSkiaEvent extends AbstractEvent<RenderEventListener> {

		public static final int ID = 3;
		
		@Override
		public void call(RenderEventListener listener) {
			listener.onRenderSkia();
		}
	}
}
