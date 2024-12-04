package com.soarclient;

import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.MouseClickEvent;

import net.minecraft.client.Minecraft;

public class SoarHandler {

	private Minecraft mc = Minecraft.getMinecraft();
	
	@EventHandler
	public void onClickMouse(MouseClickEvent event) {
		if (mc.gameSettings.keyBindTogglePerspective.isPressed()) {
			mc.gameSettings.thirdPersonView = (mc.gameSettings.thirdPersonView + 1) % 3;
			mc.renderGlobal.setDisplayListEntitiesDirty();
		}
	}
}
