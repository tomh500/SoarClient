package com.soarclient.event.impl;

import com.soarclient.event.CancellableEvent;

public class KeyEvent extends CancellableEvent {

	private boolean pressed;
	private int keyCode;

	public KeyEvent(int keyCode) {
		this.keyCode = keyCode;
		this.pressed = false;
	}

	public int getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}

	public boolean isPressed() {
		return pressed;
	}

	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}
}