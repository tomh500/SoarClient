package com.soarclient.management.mod.impl.settings;

import java.util.Arrays;

import com.soarclient.management.mod.Mod;
import com.soarclient.management.mod.ModCategory;
import com.soarclient.management.mod.settings.impl.ComboSetting;
import com.soarclient.skia.font.Icon;

public class SystemSettings extends Mod {

	private static SystemSettings instance;
	private ComboSetting blurTypeSetting = new ComboSetting("setting.blurtype", "setting.blurtype.description",
			Icon.BLUR_MEDIUM, this, Arrays.asList("setting.fastblur", "setting.normalblur"), "setting.normalblur");

	public SystemSettings() {
		super("mod.systemsettings.name", "mod.systemsettings.description", Icon.SETTINGS, ModCategory.MISC);
		instance = this;
		this.setHidden(true);
		this.setEnabled(true);
	}

	@Override
	public void onDisable() {
		this.setEnabled(true);
	}
	
	public static SystemSettings getInstance() {
		return instance;
	}

	public boolean isFastBlur() {
		return blurTypeSetting.getOption().contains("fastblur");
	}
}
