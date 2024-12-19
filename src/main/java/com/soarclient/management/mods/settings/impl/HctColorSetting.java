package com.soarclient.management.mods.settings.impl;

import com.soarclient.Soar;
import com.soarclient.libraries.material3.hct.Hct;
import com.soarclient.management.mods.Mod;
import com.soarclient.management.mods.settings.Setting;

public class HctColorSetting extends Setting {

	private Hct hct;
	private Hct defaultHct;

	public HctColorSetting(String name, String description, String icon, Mod parent, Hct hct) {
		super(name, description, icon, parent);
		this.hct = hct;
		this.defaultHct = hct;

		Soar.getInstance().getModManager().addSetting(this);
	}

	@Override
	public void reset() {
		this.hct = defaultHct;
	}

	public Hct getHct() {
		return hct;
	}

	public void setHct(Hct hct) {
		this.hct = hct;
	}

	public Hct getDefaultHct() {
		return defaultHct;
	}
}