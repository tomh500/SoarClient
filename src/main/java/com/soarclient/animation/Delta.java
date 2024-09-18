package com.soarclient.animation;

import org.lwjgl.Sys;

import com.soarclient.event.EventBus;
import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.GameLoopEvent;

public class Delta {

	private static double deltaTime;
	private long lastFrame;

	public static void register() {
		EventBus.getInstance().register(new Delta());
	}

	@EventHandler
	public void onGameLoop(GameLoopEvent event) {
		final long currentTime = (Sys.getTime() * 1000) / Sys.getTimerResolution();
		final double deltaTime = (int) (currentTime - lastFrame);
		lastFrame = currentTime;
		Delta.deltaTime = deltaTime;
	}

	public static double getDeltaTime() {
		return deltaTime;
	}
}
