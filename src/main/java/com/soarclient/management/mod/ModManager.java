package com.soarclient.management.mod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.soarclient.management.mod.api.hud.HUDMod;
import com.soarclient.management.mod.api.hud.design.HUDDesign;
import com.soarclient.management.mod.api.hud.design.impl.SimpleDesign;
import com.soarclient.management.mod.impl.hud.FPSDisplayMod;
import com.soarclient.management.mod.impl.settings.ModMenuSettings;
import com.soarclient.management.mod.settings.Setting;
import com.soarclient.management.mod.settings.impl.KeybindSetting;

public class ModManager {

	private List<Mod> mods = new ArrayList<>();
	private List<Setting> settings = new ArrayList<>();
	private List<HUDDesign> designs = new ArrayList<>();
	
	private HUDDesign currentDesign;
	
	public void init() {
		initMods();
		initDesigns();
	}
	
	private void initMods() {
		
		// HUD
		mods.add(new FPSDisplayMod());
		
		// Settings
		mods.add(new ModMenuSettings());
		
		sortMods();
	}
	
	private void initDesigns() {
		designs.add(new SimpleDesign());
		setCurrentDesign("design.simple");
	}
	
	public List<Mod> getMods() {
		return mods;
	}

	public List<Setting> getSettings() {
		return settings;
	}

	public List<HUDMod> getHUDMods() {
		return mods.stream().filter(m -> m instanceof HUDMod).map(m -> (HUDMod) m).collect(Collectors.toList());
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
	
	public HUDDesign getCurrentDesign() {
		return currentDesign;
	}

	public void setCurrentDesign(String name) {
		this.currentDesign = getDesignByName(name);
	}
	
	public HUDDesign getDesignByName(String name) {

		for (HUDDesign design : designs) {
			if (design.getName().equals(name)) {
				return design;
			}
		}

		return getDesignByName("design.simple");
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
