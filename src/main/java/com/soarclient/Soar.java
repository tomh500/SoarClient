package com.soarclient;

import com.soarclient.animation.Delta;
import com.soarclient.management.mods.ModManager;
import com.soarclient.nanovg.NanoVGHelper;

public class Soar {

	private static Soar instance = new Soar();
	
	private ModManager modManager;
	
	public void start() {
		
		NanoVGHelper.getInstance().start();
		
		modManager = new ModManager();
		modManager.init();
		
		Delta.register();
	}
	
	public void stop() {
		
	}

	public static Soar getInstance() {
		return instance;
	}
}
