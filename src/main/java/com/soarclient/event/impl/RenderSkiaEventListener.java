package com.soarclient.event.impl;

import com.soarclient.event.api.AbstractEvent;

public interface RenderSkiaEventListener {

	void onRenderSkia();
	
	class RenderSkiaEvent extends AbstractEvent<RenderSkiaEventListener> {

		public static final int ID = 3;
		
		@Override
		public void call(RenderSkiaEventListener listener) {
			listener.onRenderSkia();
		}
	}
}
