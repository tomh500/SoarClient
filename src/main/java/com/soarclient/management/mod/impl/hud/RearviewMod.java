package com.soarclient.management.mod.impl.hud;

import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.HurtCameraEvent;
import com.soarclient.event.impl.RenderFireOverlayEvent;
import com.soarclient.event.impl.RenderPumpkinOverlayEvent;
import com.soarclient.event.impl.RenderSkiaEvent;
import com.soarclient.event.impl.RenderTickEvent;
import com.soarclient.event.impl.RenderWaterOverlayEvent;
import com.soarclient.management.mod.api.hud.HUDMod;
import com.soarclient.management.mod.impl.hud.rearview.RearviewCamera;
import com.soarclient.management.mod.settings.impl.NumberSetting;
import com.soarclient.skia.Skia;
import com.soarclient.skia.font.Icon;

import io.github.humbleui.skija.SurfaceOrigin;

public class RearviewMod extends HUDMod {

	private NumberSetting widthSetting = new NumberSetting("setting.width", "setting.width.description", Icon.WIDTH,
			this, 150, 10, 180, 1);
	private NumberSetting heightSetting = new NumberSetting("setting.height", "setting.height.description", Icon.HEIGHT,
			this, 75, 10, 180, 1);
	private NumberSetting alphaSetting = new NumberSetting("setting.alpha", "setting.alpha.description", Icon.OPACITY,
			this, 1F, 0.0F, 1F, 0.1F);
	
	private RearviewCamera camera = new RearviewCamera();
	
	public RearviewMod() {
		super("", "", Icon.PHOTO_CAMERA_BACK);
	}
	
	@EventHandler
	public void onRenderTick(RenderTickEvent event) {
		camera.updateMirror();
	}

	@EventHandler
	public void onRenderSkia(RenderSkiaEvent event) {
		this.begin();
		this.drawBackground(getX(), getY(), widthSetting.getValue(), heightSetting.getValue());
		Skia.drawRoundedImage(camera.getTexture(), getX(), getY(), widthSetting.getValue(),
				heightSetting.getValue(), getRadius(), alphaSetting.getValue(), SurfaceOrigin.BOTTOM_LEFT);
		this.finish();
	}
	
	@EventHandler
	public void onRenderFireOverlay(RenderFireOverlayEvent event) {
		if(camera.isRecording()) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onRenderWaterOverlay(RenderWaterOverlayEvent event) {
		if(camera.isRecording()) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onRenderPumpkinOverlay(RenderPumpkinOverlayEvent event) {
		if(camera.isRecording()) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onHurtCamera(HurtCameraEvent event) {
		if(camera.isRecording()) {
			event.setIntensity(0);
		}
	}

	@Override
	public float getRadius() {
		return 6;
	}
}
