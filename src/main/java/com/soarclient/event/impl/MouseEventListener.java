package com.soarclient.event.impl;

import com.soarclient.event.api.CancellableEvent;

public interface MouseEventListener {
	
	void onMouseClick(int button);
	void onMouseScroll(int amount);
	
	class MouseClickEvent extends CancellableEvent<MouseEventListener> {

		public static final int ID = 20;
		private final int button;
		
		public MouseClickEvent(int button) {
			this.button = button;
		}
		
		@Override
		public void call(MouseEventListener listener) {
			listener.onMouseClick(button);
		}
	}
	
	class MouseScrollEvent extends CancellableEvent<MouseEventListener> {

		public static final int ID = 21;
		private final int amount;
		
		public MouseScrollEvent(int amount) {
			this.amount = amount;
		}
		
		@Override
		public void call(MouseEventListener listener) {
			listener.onMouseScroll(amount);
		}
	}
}
