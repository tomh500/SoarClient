package com.soarclient.event.impl;

import com.soarclient.event.CancellableEvent;

public class MouseScrollEvent extends CancellableEvent {

	private int amount;

	public MouseScrollEvent(int amount) {
		this.amount = amount;
	}

	public int getAmount() {
		return amount;
	}
}