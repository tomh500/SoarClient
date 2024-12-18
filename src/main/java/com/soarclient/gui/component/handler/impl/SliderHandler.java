package com.soarclient.gui.component.handler.impl;

import com.soarclient.gui.component.handler.ComponentHandler;

public abstract class SliderHandler extends ComponentHandler {
	public abstract void onClicked(float value);

	public abstract void onReleased(float value);

	public abstract void onValueChanged(float value);
}
