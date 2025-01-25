package com.soarclient.management.mod.settings.impl;

import com.soarclient.Soar;
import com.soarclient.management.mod.Mod;
import com.soarclient.management.mod.settings.Setting;

public class KeybindSetting extends Setting {

	private int defaultKeyCode, keyCode;
	private boolean keyDown;
	private int pressTime;

	public KeybindSetting(String name, String description, String icon, Mod parent, int keyCode) {
		super(name, description, icon, parent);

		this.defaultKeyCode = keyCode;
		this.keyCode = keyCode;

		Soar.getInstance().getModManager().addSetting(this);
	}

	@Override
	public void reset() {
		this.keyCode = this.defaultKeyCode;
	}

	public int getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}

	public int getDefaultKeyCode() {
		return defaultKeyCode;
	}

	public boolean isKeyDown() {
		return keyDown;
	}
	
	public void setKeyDown(boolean keyDown) {
		this.keyDown = keyDown;
	}

	public boolean isPressed() {
		this.pressTime--;
		return pressTime >= 0;
	}

	public void setPressed() {
		this.pressTime = 1;
	}
}