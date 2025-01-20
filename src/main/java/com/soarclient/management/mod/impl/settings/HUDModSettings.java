package com.soarclient.management.mod.impl.settings;

import com.soarclient.management.mod.Mod;
import com.soarclient.management.mod.ModCategory;
import com.soarclient.management.mod.settings.impl.BooleanSetting;
import com.soarclient.skia.font.Icon;

public class HUDModSettings extends Mod {

	private static HUDModSettings instance;
	
	private BooleanSetting blurSetting = new BooleanSetting("setting.blur", "setting.blur.description", Icon.LENS_BLUR,
			this, true);

	public HUDModSettings() {
		super("mod.hudsettings.name", "mod.hudsettings.description", Icon.BROWSE_ACTIVITY, ModCategory.MISC);
		this.setHidden(true);
		
		instance = this;
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	public static HUDModSettings getInstance() {
		return instance;
	}

	public BooleanSetting getBlurSetting() {
		return blurSetting;
	}
}
