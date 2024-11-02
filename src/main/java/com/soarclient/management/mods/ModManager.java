package com.soarclient.management.mods;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.soarclient.management.mods.api.HUDMod;
import com.soarclient.management.mods.api.design.HUDDesign;
import com.soarclient.management.mods.api.design.impl.SimpleDesign;
import com.soarclient.management.mods.impl.hud.ArmorStatusMod;
import com.soarclient.management.mods.impl.misc.LiquidFixMod;
import com.soarclient.management.mods.impl.player.HitDelayFixMod;
import com.soarclient.management.mods.impl.player.NoJumpDelayMod;
import com.soarclient.management.mods.impl.settings.GlobalSettings;
import com.soarclient.management.mods.settings.Setting;
import com.soarclient.management.mods.settings.impl.KeybindSetting;

public class ModManager {

	private List<Mod> mods = new ArrayList<>();
	private List<HUDDesign> designs = new ArrayList<>();
	private List<Setting> settings = new ArrayList<>();

	private HUDDesign currentDesign;

	public void init() {
		initDesigns();
		addMods();
	}

	private void addMods() {

		// HUD
		mods.add(new ArmorStatusMod());

		// Misc
		mods.add(new LiquidFixMod());

		// Player
		mods.add(new HitDelayFixMod());
		mods.add(new NoJumpDelayMod());

		// Settings
		mods.add(new GlobalSettings());
	}

	private void initDesigns() {

		designs.add(new SimpleDesign());

		setCurrentDesign("design.simple");
	}

	public List<Mod> getMods() {
		return mods;
	}

	public List<HUDMod> getHUDMods() {
		return mods.stream().filter(m -> m instanceof HUDMod).map(m -> (HUDMod) m).collect(Collectors.toList());
	}

	public List<KeybindSetting> getKeybindSettings() {
		return settings.stream().filter(s -> s instanceof KeybindSetting).map(s -> (KeybindSetting) s)
				.collect(Collectors.toList());
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
}
