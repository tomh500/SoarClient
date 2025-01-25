package com.soarclient;

import com.soarclient.gui.SimpleSoarGui;

import net.minecraft.client.MinecraftClient;

public class Soar {

	private static Soar instance = new Soar();
	
	public void start() {
	}

	public static Soar getInstance() {
		return instance;
	}
}
