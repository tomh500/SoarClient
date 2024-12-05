package com.soarclient.gui.component.handler.impl;

import com.soarclient.gui.component.handler.ComponentHandler;

public abstract class KeybindHandler extends ComponentHandler {
	public abstract void onBinded(int keyCode);
}