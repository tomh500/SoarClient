package com.soarclient;

import com.soarclient.event.EventBus;
import com.soarclient.event.impl.RenderSkiaEventListener.RenderSkiaEvent;

public class Soar {

	private final static Soar instance = new Soar();
	private final String name, version;
	
	public Soar() {
		name = "Soar Client";
		version = "8.0";
	}
	
	public void start() {
		
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
