package com.soarclient.event.impl;

import com.soarclient.event.Event;

public class KeyEvent extends Event {

	private final int keyCode;
	
	public KeyEvent(int keyCode){
		this.keyCode = keyCode;
	}

	public int getKeyCode() {
		return keyCode;
	}
}
