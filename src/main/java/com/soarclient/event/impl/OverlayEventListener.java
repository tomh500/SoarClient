package com.soarclient.event.impl;

import com.soarclient.event.api.CancellableEvent;

public interface OverlayEventListener {
	
	void onRenderFireOverlay();
	void onRenderPumpkinOverlay();
	void onRenderWaterOverlay();
	
	class RenderFireOverlayEvent extends CancellableEvent<OverlayEventListener> {

		public static final int ID = 4;
		
		@Override
		public void call(OverlayEventListener listener) {
			listener.onRenderFireOverlay();
		}
	}
	
	class RenderPumpkinOverlayEvent extends CancellableEvent<OverlayEventListener> {

		public static final int ID = 5;
		
		@Override
		public void call(OverlayEventListener listener) {
			listener.onRenderPumpkinOverlay();
		}
	}
	
	class RenderWaterOverlayEvent extends CancellableEvent<OverlayEventListener> {

		public static final int ID = 6;
		
		@Override
		public void call(OverlayEventListener listener) {
			listener.onRenderWaterOverlay();
		}
	}
}
