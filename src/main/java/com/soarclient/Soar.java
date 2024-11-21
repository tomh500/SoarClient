package com.soarclient;

import com.soarclient.animation.Delta;
import com.soarclient.event.EventBus;
import com.soarclient.management.account.AccountManager;
import com.soarclient.management.color.ColorManager;
import com.soarclient.management.mods.ModManager;
import com.soarclient.management.music.MusicManager;
import com.soarclient.management.proxy.ProxyManager;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.utils.file.FileLocation;
import com.soarclient.utils.language.I18n;
import com.soarclient.utils.language.Language;
import com.soarclient.viasoar.ViaSoar;
import com.soarclient.viasoar.common.ViaSoarCommon;

public class Soar {

	private static Soar instance = new Soar();
	
	private String name, version;

	private AccountManager accountManager;
	private ModManager modManager;
	private ColorManager colorManager;
	private MusicManager musicManager;
	private ProxyManager proxyManager;
	
	private long launchTime;

	public Soar() {
		name = "Soar";
		version = "8.0";
	}
	
	public void start() {

		FileLocation.init();
		launchTime = System.currentTimeMillis();
		NanoVGHelper.getInstance().start();

		ViaSoarCommon.init(ViaSoar.PLATFORM);
		
		accountManager = new AccountManager();
		modManager = new ModManager();
		modManager.init();
		colorManager = new ColorManager();
		musicManager = new MusicManager();
		proxyManager = new ProxyManager();

		Delta.register();
		I18n.setLanguage(Language.ENGLISH);
		
		EventBus.getInstance().post(new SoarHandler());
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

	public AccountManager getAccountManager() {
		return accountManager;
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

	public ProxyManager getProxyManager() {
		return proxyManager;
	}
}
