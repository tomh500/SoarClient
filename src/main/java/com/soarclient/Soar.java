package com.soarclient;

import org.lwjgl.input.Keyboard;

import com.soarclient.animation.Delta;
import com.soarclient.event.EventBus;
import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.KeyEvent;
import com.soarclient.management.mods.ModManager;
import com.soarclient.management.music.MusicManager;
import com.soarclient.management.video.VideoManager;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.utils.file.FileLocation;
import com.soarclient.utils.language.I18n;
import com.soarclient.utils.language.Language;

public class Soar {

	private static Soar instance = new Soar();

	private ModManager modManager;
	private MusicManager musicManager;
	private VideoManager videoManager;

	public void start() {

		FileLocation.init();
		
		NanoVGHelper.getInstance().start();

		modManager = new ModManager();
		modManager.init();
		musicManager = new MusicManager();
		videoManager = new VideoManager();

		Delta.register();
		I18n.setLanguage(Language.ENGLISH);

		EventBus.getInstance().register(this);
	}

	public void stop() {

	}
	
	@EventHandler
	public void onKey(KeyEvent event) {
		if (event.getKeyCode() == Keyboard.KEY_RSHIFT) {
		}
	}

	public static Soar getInstance() {
		return instance;
	}

	public ModManager getModManager() {
		return modManager;
	}

	public MusicManager getMusicManager() {
		return musicManager;
	}

	public VideoManager getVideoManager() {
		return videoManager;
	}
}
