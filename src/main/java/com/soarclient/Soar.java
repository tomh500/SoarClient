package com.soarclient;

import com.soarclient.animation.Delta;

public class Soar {

	private static Soar instance = new Soar();
	
	private String name, version;
	
	public Soar() {
		name = "Soar";
		version = "8.0";
	}
	
	public void start() {
		Delta.register();
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
}
