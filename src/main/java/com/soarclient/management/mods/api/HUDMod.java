package com.soarclient.management.mods.api;

import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.RenderBlurEvent;
import com.soarclient.management.mods.Mod;
import com.soarclient.management.mods.ModCategory;

public class HUDMod extends Mod {

	protected Position position;
	protected HUDRenderer renderer;

	public HUDMod(String name, String description, String icon) {
		super(name, description, icon, ModCategory.HUD);
		this.position = new Position(100, 100, 0, 0);
		this.renderer = new HUDRenderer(position);
		this.setMovable(true);
	}

	@EventHandler
	public void onRenderBlur(RenderBlurEvent event) {
		event.setupAndDraw(() -> event.drawRoundedRect(position.getX(), position.getY(),
				position.getWidth(), position.getHeight(), 6 * position.getScale() /* TOOD: Radius Setting */, 1F));
	}

	public Position getPosition() {
		return position;
	}
}