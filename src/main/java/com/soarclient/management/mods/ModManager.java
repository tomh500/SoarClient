package com.soarclient.management.mods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.soarclient.management.mods.api.HUDMod;
import com.soarclient.management.mods.api.design.HUDDesign;
import com.soarclient.management.mods.api.design.impl.SimpleDesign;
import com.soarclient.management.mods.impl.hud.CPSDisplayMod;
import com.soarclient.management.mods.impl.hud.ComboCounterMod;
import com.soarclient.management.mods.impl.hud.CoordsMod;
import com.soarclient.management.mods.impl.hud.DayCounterMod;
import com.soarclient.management.mods.impl.hud.FPSDisplayMod;
import com.soarclient.management.mods.impl.hud.GameModeDisplayMod;
import com.soarclient.management.mods.impl.hud.HealthDisplayMod;
import com.soarclient.management.mods.impl.hud.MusicInfoMod;
import com.soarclient.management.mods.impl.hud.NameDisplayMod;
import com.soarclient.management.mods.impl.hud.PingDisplayMod;
import com.soarclient.management.mods.impl.hud.PitchDisplayMod;
import com.soarclient.management.mods.impl.hud.PlayTimeDisplayMod;
import com.soarclient.management.mods.impl.hud.PlayerCounterMod;
import com.soarclient.management.mods.impl.hud.PotionCounterMod;
import com.soarclient.management.mods.impl.hud.ReachDisplayMod;
import com.soarclient.management.mods.impl.hud.ServerIPDisplayMod;
import com.soarclient.management.mods.impl.hud.StopwatchMod;
import com.soarclient.management.mods.impl.hud.WeatherDisplayMod;
import com.soarclient.management.mods.impl.hud.YawDisplayMod;
import com.soarclient.management.mods.impl.misc.LiquidFixMod;
import com.soarclient.management.mods.impl.misc.NameProtectMod;
import com.soarclient.management.mods.impl.misc.RawInputMod;
import com.soarclient.management.mods.impl.misc.TimeChangerMod;
import com.soarclient.management.mods.impl.misc.WeatherChangerMod;
import com.soarclient.management.mods.impl.player.AutoGGMod;
import com.soarclient.management.mods.impl.player.AutoNextGameMod;
import com.soarclient.management.mods.impl.player.HitDelayFixMod;
import com.soarclient.management.mods.impl.player.NoJumpDelayMod;
import com.soarclient.management.mods.impl.player.QuickSwitchMod;
import com.soarclient.management.mods.impl.player.TaplookMod;
import com.soarclient.management.mods.impl.player.ToggleSneakMod;
import com.soarclient.management.mods.impl.player.ToggleSprintMod;
import com.soarclient.management.mods.impl.player.ZoomMod;
import com.soarclient.management.mods.impl.render.MotionBlurMod;
import com.soarclient.management.mods.impl.settings.ModMenuSetting;
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
		mods.add(new ComboCounterMod());
		mods.add(new CoordsMod());
		mods.add(new CPSDisplayMod());
		mods.add(new DayCounterMod());
		mods.add(new FPSDisplayMod());
		mods.add(new GameModeDisplayMod());
		mods.add(new HealthDisplayMod());
		mods.add(new MusicInfoMod());
		mods.add(new NameDisplayMod());
		mods.add(new PingDisplayMod());
		mods.add(new PitchDisplayMod());
		mods.add(new PlayerCounterMod());
		mods.add(new PlayTimeDisplayMod());
		mods.add(new PotionCounterMod());
		mods.add(new ReachDisplayMod());
		mods.add(new ServerIPDisplayMod());
		mods.add(new StopwatchMod());
		mods.add(new WeatherDisplayMod());
		mods.add(new YawDisplayMod());

		// Misc
		mods.add(new LiquidFixMod());
		mods.add(new NameProtectMod());
		mods.add(new RawInputMod());
		mods.add(new TimeChangerMod());
		mods.add(new WeatherChangerMod());

		// Player
		mods.add(new AutoGGMod());
		mods.add(new AutoNextGameMod());
		mods.add(new HitDelayFixMod());
		mods.add(new NoJumpDelayMod());
		mods.add(new QuickSwitchMod());
		mods.add(new TaplookMod());
		mods.add(new ToggleSneakMod());
		mods.add(new ToggleSprintMod());
		mods.add(new ZoomMod());

		// Render
		mods.add(new MotionBlurMod());
		
		// Settings
		mods.add(new ModMenuSetting());
		
		sort();
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
	
	private void sort() {

		Comparator<Mod> nameComparator = new Comparator<Mod>() {

			@Override
			public int compare(Mod mod1, Mod mod2) {
				return mod1.getName().compareTo(mod2.getName());
			}
		};

		Collections.sort(mods, nameComparator);
	}
}
