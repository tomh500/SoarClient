package com.soarclient.management.mod.settings.impl;

import com.soarclient.Soar;
import com.soarclient.management.mod.Mod;
import com.soarclient.management.mod.settings.Setting;

public class StringSetting extends Setting {

	private String defaultValue, string;

	public StringSetting(String name, String description, String icon, Mod parent, String string) {
		super(name, description, icon, parent);

		this.defaultValue = string;
		this.string = string;

		Soar.getInstance().getModManager().addSetting(this);
	}

	@Override
	public void reset() {
		this.string = defaultValue;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public String getDefaultValue() {
		return defaultValue;
	}
}