package com.soarclient;

import org.lwjgl.input.Keyboard;

import com.soarclient.animation.Delta;
import com.soarclient.event.EventBus;
import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.KeyEvent;
import com.soarclient.gui.GuiTest;
import com.soarclient.management.mods.ModManager;
import com.soarclient.nanovg.NanoVGHelper;

import net.minecraft.client.Minecraft;

public class Soar {

	private static Soar instance = new Soar();
	
	private ModManager modManager;
	
	public void start() {
		
		NanoVGHelper.getInstance().start();
		
		modManager = new ModManager();
		modManager.init();
		
		Delta.register();
		EventBus.getInstance().register(this);
	}
	
	public void stop() {
		
	}
	
	@EventHandler
	public void onKey(KeyEvent event) {
		if(event.getKeyCode() == Keyboard.KEY_RSHIFT) {
			Minecraft.getMinecraft().displayGuiScreen(GuiTest.getTestGui());
		}
	}

	public static Soar getInstance() {
		return instance;
	}

	public ModManager getModManager() {
		return modManager;
	}
}
