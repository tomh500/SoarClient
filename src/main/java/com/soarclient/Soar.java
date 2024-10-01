package com.soarclient;

import org.lwjgl.input.Keyboard;

import com.soarclient.animation.Delta;
import com.soarclient.event.EventBus;
import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.KeyEvent;
import com.soarclient.gui.SoarGui;
import com.soarclient.gui.modmenu.GuiModMenu;
import com.soarclient.management.mods.ModManager;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.utils.language.I18n;
import com.soarclient.utils.language.Language;

import net.minecraft.client.Minecraft;

public class Soar {

	private static Soar instance = new Soar();
	
	private ModManager modManager;
	
	public void start() {
		
		NanoVGHelper.getInstance().start();
		
		modManager = new ModManager();
		modManager.init();
		
		Delta.register();
		I18n.setLanguage(Language.ENGLISH);
		
		EventBus.getInstance().register(this);
	}
	
	public void stop() {
		
	}
	
	@EventHandler
	public void onKey(KeyEvent event) {
		if(event.getKeyCode() == Keyboard.KEY_RSHIFT) {
			Minecraft.getMinecraft().displayGuiScreen(SoarGui.create(new GuiModMenu()));
		}
	}

	public static Soar getInstance() {
		return instance;
	}

	public ModManager getModManager() {
		return modManager;
	}
}
