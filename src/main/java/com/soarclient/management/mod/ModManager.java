package com.soarclient.management.mod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.soarclient.management.mod.impl.settings.ModMenuSettings;
import com.soarclient.management.mod.settings.Setting;
import com.soarclient.management.mod.settings.impl.KeybindSetting;

public class ModManager {

	private List<Mod> mods = new ArrayList<>();
	private List<Setting> settings = new ArrayList<>();
	
	public void init() {
		initMods();
	}
	
	private void initMods() {
		
		// Settings
		mods.add(new ModMenuSettings());
		
		sortMods();
	}
	
	public List<Mod> getMods() {
		return mods;
	}

	public List<Setting> getSettings() {
		return settings;
	}

	public List<KeybindSetting> getKeybindSettings() {
		return settings.stream().filter(s -> s instanceof KeybindSetting).map(s -> (KeybindSetting) s)
				.collect(Collectors.toList());
	}

	public List<Setting> getSettingsByMod(Mod m) {
		return settings.stream().filter(s -> s.getParent().equals(m)).collect(Collectors.toList());
	}
	
	public void addSetting(Setting setting) {
		settings.add(setting);
	}
	
	private void sortMods() {

		Comparator<Mod> nameComparator = new Comparator<Mod>() {

			@Override
			public int compare(Mod mod1, Mod mod2) {
				return mod1.getName().compareTo(mod2.getName());
			}
		};

		Collections.sort(mods, nameComparator);
	}
}
