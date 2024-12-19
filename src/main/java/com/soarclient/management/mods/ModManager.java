package com.soarclient.management.mods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
import com.soarclient.management.mods.impl.player.ZoomMod;
import com.soarclient.management.mods.impl.render.MotionBlurMod;
import com.soarclient.management.mods.impl.settings.ModMenuSetting;
import com.soarclient.management.mods.settings.Setting;
import com.soarclient.management.mods.settings.impl.KeybindSetting;

public class ModManager {

	private List<Mod> mods = new ArrayList<>();
	private List<Setting> settings = new ArrayList<>();

	public void init() {
		addMods();
	}

	private void addMods() {

		// HUD
		
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
		//mods.add(new ToggleSprintMod());
		mods.add(new ZoomMod());

		// Render
		mods.add(new MotionBlurMod());
		
		// Settings
		mods.add(new ModMenuSetting());
		
		sort();
	}

	public List<Mod> getMods() {
		return mods;
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
