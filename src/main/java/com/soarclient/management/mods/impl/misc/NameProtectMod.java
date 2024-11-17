package com.soarclient.management.mods.impl.misc;

import com.soarclient.management.mods.Mod;
import com.soarclient.management.mods.ModCategory;
import com.soarclient.management.mods.settings.impl.StringSetting;
import com.soarclient.nanovg.font.Icon;

public class NameProtectMod extends Mod {

	private static NameProtectMod instance;
	private StringSetting nameSetting = new StringSetting("setting.name", "setting.name.description", Icon.BADGE, this,
			"You");

	public NameProtectMod() {
		super("mod.nameprotect.name", "mod.nameprotect.description", Icon.HIDE_SOURCE, ModCategory.MISC);

		instance = this;
	}

	public static NameProtectMod getInstance() {
		return instance;
	}

	public StringSetting getNameSetting() {
		return nameSetting;
	}
}