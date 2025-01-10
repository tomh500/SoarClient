package com.soarclient;

import com.soarclient.animation.Delta;
import com.soarclient.event.EventBus;
import com.soarclient.event.impl.GameLoopEventListener.GameLoopEvent;
import com.soarclient.management.color.ColorManager;
import com.soarclient.management.mod.ModManager;
import com.soarclient.utils.language.I18n;
import com.soarclient.utils.language.Language;

public class Soar {

	private final static Soar instance = new Soar();
	private final String name, version;

	private ModManager modManager;
	private ColorManager colorManager;

	public Soar() {
		name = "Soar Client";
		version = "8.0";
	}

	public void start() {

		I18n.setLanguage(Language.ENGLISH);

		modManager = new ModManager();
		modManager.init();
		colorManager = new ColorManager();

		EventBus.getInstance().register(GameLoopEvent.ID, new Delta());
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

	public ColorManager getColorManager() {
		return colorManager;
	}
}
