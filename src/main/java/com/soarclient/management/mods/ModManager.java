package com.soarclient.management.mods;

import java.util.ArrayList;
import java.util.List;

import com.soarclient.management.mods.impl.player.HitDelayFixMod;
import com.soarclient.management.mods.impl.player.NoJumpDelayMod;
import com.soarclient.management.mods.settings.Setting;

public class ModManager {

	private List<Mod> mods = new ArrayList<>();
	private List<Setting> settings = new ArrayList<>();

	public void init() {
		addMods();
	}

	private void addMods() {
		
		
		mods.add(new HitDelayFixMod());
		mods.add(new NoJumpDelayMod());
	}

	public List<Mod> getMods() {
		return mods;
	}

	public void addSetting(Setting setting) {
		settings.add(setting);
	}
}
