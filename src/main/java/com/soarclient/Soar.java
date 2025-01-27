package com.soarclient;

import com.soarclient.animation.Delta;
import com.soarclient.event.EventBus;
import com.soarclient.management.color.ColorManager;
import com.soarclient.management.mod.ModManager;

public class Soar {

	private final static Soar instance = new Soar();
	
	private final String name = "Soar";
	private final String version = "8.0";
	
	private ModManager modManager;
	private ColorManager colorManager;
	
	public void start() {
		
		modManager = new ModManager();
		modManager.init();
		colorManager = new ColorManager();
		
		EventBus.getInstance().register(new SoarHandler());
		EventBus.getInstance().register(new Delta());
	}

	public static Soar getInstance() {
		return instance;
	}
	
	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public ModManager getModManager() {
		return modManager;
	}

	public ColorManager getColorManager() {
		return colorManager;
	}
}
