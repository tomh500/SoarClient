package com.soarclient.event.impl;

import com.soarclient.event.CancellableEvent;

public class MouseClickEvent extends CancellableEvent {

	private int button;

	public MouseClickEvent(int button) {
		this.button = button;
	}

	public int getButton() {
		return button;
	}
}