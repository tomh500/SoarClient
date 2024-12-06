package com.soarclient;

import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.ClientTickEvent;
import com.soarclient.event.impl.MouseClickEvent;
import com.soarclient.event.impl.PreClientTickEvent;
import com.soarclient.libraries.patcher.font.EnhancementManager;

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
	
	@EventHandler
	public void onClientTick(ClientTickEvent event) {
		Soar.getInstance().getColorManager().onTick();
	}

	@EventHandler
	public void onPreClientTick(PreClientTickEvent event) {
		EnhancementManager.getInstance().tick();
	}
}
