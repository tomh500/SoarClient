package com.soarclient.event.impl;

import com.soarclient.event.Event;

public class ZoomFovEvent extends Event {

	private float fov;

	public ZoomFovEvent(float fov) {
		this.fov = fov;
	}

	public float getFov() {
		return fov;
	}

	public void setFov(float fov) {
		this.fov = fov;
	}
}