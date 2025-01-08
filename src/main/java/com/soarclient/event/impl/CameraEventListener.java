package com.soarclient.event.impl;

import com.soarclient.event.api.AbstractEvent;

public interface CameraEventListener {
	
	void onHurtCamera(HurtCameraEvent event);
	
	class HurtCameraEvent extends AbstractEvent<CameraEventListener> {

		public static final int ID = 19;
		private float intensity;
		
		public HurtCameraEvent() {
			this.intensity = 1.0F;
		}
		
		@Override
		public void call(CameraEventListener listener) {
			listener.onHurtCamera(this);
		}
		
		public float getIntensity() {
			return intensity;
		}

		public void setIntensity(float intensity) {
			this.intensity = intensity;
		}
	}
}
