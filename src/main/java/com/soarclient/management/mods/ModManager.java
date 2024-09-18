package com.soarclient.management.mods;

import java.util.ArrayList;
import java.util.List;

import com.soarclient.management.mods.settings.Setting;

public class ModManager {

	private List<Mod> mods = new ArrayList<>();
	private List<Setting> settings = new ArrayList<>();
	
	public void init() {
		addMods();
	}
	
	private void addMods() {
		
	}
	
	public void addSetting(Setting setting) {
		settings.add(setting);
	}
}
