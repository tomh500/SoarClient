package com.soarclient;

import com.soarclient.management.mods.ModManager;

public class Soar {

	private static Soar instance = new Soar();
	
	private ModManager modManager;
	
	public void start() {
		
		modManager = new ModManager();
		modManager.init();
	}
	
	public void stop() {
		
	}

	public static Soar getInstance() {
		return instance;
	}
}
