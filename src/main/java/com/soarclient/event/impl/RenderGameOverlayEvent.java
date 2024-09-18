package com.soarclient.event.impl;

import com.soarclient.event.Event;

public class RenderGameOverlayEvent extends Event {
	
	private float partialTicks;

	public RenderGameOverlayEvent(float partialTicks) {
		this.partialTicks = partialTicks;
	}

	public float getPartialTicks() {
		return partialTicks;
	}
}
