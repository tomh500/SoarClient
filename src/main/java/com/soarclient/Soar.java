package com.soarclient;

import com.soarclient.management.mod.ModManager;

public class Soar {

	private final static Soar instance = new Soar();
	private final String name, version;
	
	private ModManager modManager;
	
	public Soar() {
		name = "Soar Client";
		version = "8.0";
	}
	
	public void start() {
		modManager = new ModManager();
	}
	
	public void stop() {
		
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
}
