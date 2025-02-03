package com.soarclient.management.mod;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.soarclient.management.mod.api.hud.HUDMod;
import com.soarclient.management.mod.api.hud.design.HUDDesign;
import com.soarclient.management.mod.api.hud.design.impl.ClassicDesign;
import com.soarclient.management.mod.api.hud.design.impl.ClearDesign;
import com.soarclient.management.mod.api.hud.design.impl.MaterialYouDesign;
import com.soarclient.management.mod.api.hud.design.impl.SimpleDesign;
import com.soarclient.management.mod.impl.hud.ComboCounterMod;
import com.soarclient.management.mod.impl.hud.CoordsMod;
import com.soarclient.management.mod.impl.hud.DayCounterMod;
import com.soarclient.management.mod.impl.hud.FPSDisplayMod;
import com.soarclient.management.mod.impl.hud.GameModeDisplayMod;
import com.soarclient.management.mod.impl.hud.HealthDisplayMod;
import com.soarclient.management.mod.impl.hud.JumpResetIndicatorMod;
import com.soarclient.management.mod.impl.hud.KeystrokesMod;
import com.soarclient.management.mod.impl.hud.MouseStrokesMod;
import com.soarclient.management.mod.impl.hud.MusicInfoMod;
import com.soarclient.management.mod.impl.hud.NameDisplayMod;
import com.soarclient.management.mod.impl.hud.PingDisplayMod;
import com.soarclient.management.mod.impl.hud.PitchDisplayMod;
import com.soarclient.management.mod.impl.hud.PlayTimeDisplayMod;
import com.soarclient.management.mod.impl.hud.PlayerCounterMod;
import com.soarclient.management.mod.impl.hud.ProtocolVersionMod;
import com.soarclient.management.mod.impl.hud.ReachDisplayMod;
import com.soarclient.management.mod.impl.hud.ServerIPDisplayMod;
import com.soarclient.management.mod.impl.hud.StopwatchMod;
import com.soarclient.management.mod.impl.hud.WeatherDisplayMod;
import com.soarclient.management.mod.impl.hud.YawDisplayMod;
import com.soarclient.management.mod.impl.player.HitDelayFixMod;
import com.soarclient.management.mod.impl.player.NoJumpDelayMod;
import com.soarclient.management.mod.impl.player.SnapTapMod;
import com.soarclient.management.mod.impl.player.TaplookMod;
import com.soarclient.management.mod.impl.settings.HUDModSettings;
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
		mods.add(new ComboCounterMod());
		mods.add(new CoordsMod());
		mods.add(new DayCounterMod());
		mods.add(new FPSDisplayMod());
		mods.add(new GameModeDisplayMod());
		mods.add(new HealthDisplayMod());
		mods.add(new JumpResetIndicatorMod());
		mods.add(new KeystrokesMod());
		mods.add(new MouseStrokesMod());
		mods.add(new MusicInfoMod());
		mods.add(new NameDisplayMod());
		mods.add(new PingDisplayMod());
		mods.add(new PitchDisplayMod());
		mods.add(new PlayerCounterMod());
		mods.add(new PlayTimeDisplayMod());
		mods.add(new ProtocolVersionMod());
		mods.add(new ReachDisplayMod());
		mods.add(new ServerIPDisplayMod());
		mods.add(new StopwatchMod());
		mods.add(new WeatherDisplayMod());
		mods.add(new YawDisplayMod());
		
		// Player
		mods.add(new HitDelayFixMod());
		mods.add(new NoJumpDelayMod());
		mods.add(new SnapTapMod());
		mods.add(new TaplookMod());
		
		// Settings
		mods.add(new HUDModSettings());
		mods.add(new ModMenuSettings());
		
		sortMods();
	}

	private void initDesigns() {
		designs.add(new ClassicDesign());
		designs.add(new ClearDesign());
		designs.add(new MaterialYouDesign());
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
		return designs.stream().filter(design -> design.getName().equals(name)).findFirst()
				.orElseGet(() -> getDesignByName("design.simple"));
	}

	private void sortMods() {
		mods.sort((mod1, mod2) -> mod1.getName().compareTo(mod2.getName()));
	}
}
