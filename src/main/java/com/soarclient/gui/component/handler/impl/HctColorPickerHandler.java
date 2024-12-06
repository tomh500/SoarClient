package com.soarclient.gui.component.handler.impl;

import com.soarclient.gui.component.handler.ComponentHandler;
import com.soarclient.libraries.material3.hct.Hct;

public abstract class HctColorPickerHandler extends ComponentHandler {
	public abstract void onPicking(Hct hct);
}