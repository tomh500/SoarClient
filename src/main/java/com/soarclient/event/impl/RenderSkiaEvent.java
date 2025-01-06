package com.soarclient.event.impl;

import com.soarclient.event.Event;

public class RenderSkiaEvent extends Event {

	private float partialTicks;

	public RenderSkiaEvent(float partialTicks) {
		this.partialTicks = partialTicks;
	}

	public float getPartialTicks() {
		return partialTicks;
	}
}
