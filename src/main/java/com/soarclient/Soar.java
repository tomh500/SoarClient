package com.soarclient;

import com.soarclient.animation.Delta;
import com.soarclient.event.EventBus;
import com.soarclient.management.color.ColorManager;
import com.soarclient.management.mod.ModManager;
import com.soarclient.management.music.MusicManager;
import com.soarclient.utils.file.FileLocation;
import com.soarclient.utils.language.I18n;
import com.soarclient.utils.language.Language;

public class Soar {

	private static Soar instance = new Soar();

	private String name, version;

	private ModManager modManager;
	private ColorManager colorManager;
	private MusicManager musicManager;

	private long launchTime;

	public Soar() {
		name = "Soar";
		version = "8.0";
	}

	public void start() {

		FileLocation.init();
		launchTime = System.currentTimeMillis();
		I18n.setLanguage(Language.ENGLISH);
		Delta.register();

		modManager = new ModManager();
		modManager.init();
		colorManager = new ColorManager();
		musicManager = new MusicManager();

		EventBus.getInstance().register(new SoarHandler());
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

	public long getLaunchTime() {
		return launchTime;
	}

	public ModManager getModManager() {
		return modManager;
	}

	public ColorManager getColorManager() {
		return colorManager;
	}

	public MusicManager getMusicManager() {
		return musicManager;
	}
}
