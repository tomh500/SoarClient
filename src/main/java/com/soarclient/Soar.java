package com.soarclient;

import com.soarclient.animation.Delta;
import com.soarclient.event.EventBus;
import com.soarclient.management.color.ColorManager;
import com.soarclient.management.mods.ModManager;
import com.soarclient.management.music.MusicManager;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.utils.file.FileLocation;
import com.soarclient.utils.language.I18n;
import com.soarclient.utils.language.Language;

public class Soar {

	private static Soar instance = new Soar();

	private ModManager modManager;
	private ColorManager colorManager;
	private MusicManager musicManager;

	public void start() {

		FileLocation.init();

		NanoVGHelper.getInstance().start();

		modManager = new ModManager();
		modManager.init();
		colorManager = new ColorManager();
		musicManager = new MusicManager();

		Delta.register();
		I18n.setLanguage(Language.ENGLISH);
		
		EventBus.getInstance().post(new SoarHandler());
	}

	public void stop() {

	}
	
	public static Soar getInstance() {
		return instance;
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
