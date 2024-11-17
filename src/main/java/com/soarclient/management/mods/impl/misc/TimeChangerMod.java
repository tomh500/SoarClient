package com.soarclient.management.mods.impl.misc;

import com.soarclient.management.mods.Mod;
import com.soarclient.management.mods.ModCategory;
import com.soarclient.management.mods.settings.impl.NumberSetting;
import com.soarclient.nanovg.font.Icon;

public class TimeChangerMod extends Mod {

	private static TimeChangerMod instance;
	private NumberSetting timeSetting = new NumberSetting("setting.time", "setting.time.description", Icon.UPDATE, this,
			12, 0, 24, 1);

	public TimeChangerMod() {
		super("mod.timechanger.name", "mod.timechanger.description", Icon.UPDATE, ModCategory.MISC);

		instance = this;
	}

	public static TimeChangerMod getInstance() {
		return instance;
	}

	public NumberSetting getTimeSetting() {
		return timeSetting;
	}
}