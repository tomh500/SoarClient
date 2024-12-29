package com.soarclient.event.impl;

import com.soarclient.event.Event;

public class HurtCameraEvent extends Event {

	private float intensity;
	
	public HurtCameraEvent() {
		this.intensity = 1.0F;
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
}
