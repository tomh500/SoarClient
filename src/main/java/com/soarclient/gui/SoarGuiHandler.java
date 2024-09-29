package com.soarclient.gui;

import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.RenderBlurEvent;

public class SoarGuiHandler {

	private SoarGui gui;
	
	public SoarGuiHandler(SoarGui gui) {
		this.gui = gui;
	}
	
	@EventHandler
	public void onRenderBlur(RenderBlurEvent event) {
		gui.drawBlur(event);
	}

	public void setGui(SoarGui gui) {
		this.gui = gui;
	}
}
