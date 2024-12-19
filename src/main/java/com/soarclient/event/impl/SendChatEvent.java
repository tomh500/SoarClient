package com.soarclient.event.impl;

import com.soarclient.event.CancellableEvent;

public class SendChatEvent extends CancellableEvent {

	private String message;

	public SendChatEvent(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}