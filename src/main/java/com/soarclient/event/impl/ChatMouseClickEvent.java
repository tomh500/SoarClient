package com.soarclient.event.impl;

import com.soarclient.event.Event;

public class ChatMouseClickEvent extends Event {

	private final int mouseX, mouseY;
	
	public ChatMouseClickEvent(int mouseX, int mouseY) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
	}

	public int getMouseX() {
		return mouseX;
	}

	public int getMouseY() {
		return mouseY;
	}
}
