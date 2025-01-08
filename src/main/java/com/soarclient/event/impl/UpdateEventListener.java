package com.soarclient.event.impl;

import com.soarclient.event.api.AbstractEvent;

public interface UpdateEventListener {

	void onUpdate();
	
	class UpdateEvent extends AbstractEvent<UpdateEventListener> {

		public static final int ID = 10;
		
		@Override
		public void call(UpdateEventListener listener) {
			listener.onUpdate();
		}
	}
}
