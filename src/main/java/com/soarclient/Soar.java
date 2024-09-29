package com.soarclient;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import com.soarclient.animation.Delta;
import com.soarclient.event.EventBus;
import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.KeyEvent;
import com.soarclient.management.mods.ModManager;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.utils.ColorUtils;

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
		// 120, 120, 120, 
		//System.out.println(ColorUtils.blend(new Color(0, 0, 0, 26), new Color(255, 255, 255, 26)).getAlpha());
		if(event.getKeyCode() == Keyboard.KEY_RSHIFT) {
			//Minecraft.getMinecraft().displayGuiScreen(GuiTest.getTestGui());
		}
	}

	public static Soar getInstance() {
		return instance;
	}

	public ModManager getModManager() {
		return modManager;
	}
}
