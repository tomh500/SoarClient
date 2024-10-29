package com.soarclient.management.mods.settings.impl;

import com.soarclient.Soar;
import com.soarclient.management.mods.Mod;
import com.soarclient.management.mods.settings.Setting;
import com.soarclient.utils.math.MathUtils;

public class NumberSetting extends Setting {

	private float defaultValue, value, minValue, maxValue;
	private boolean integer;

	public NumberSetting(String name, String description, String icon, Mod parent, float defaultValue, float minValue,
			float maxValue, boolean integer) {
		super(name, description, icon, parent);

		this.value = defaultValue;
		this.defaultValue = defaultValue;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.integer = integer;

		Soar.getInstance().getModManager().addSetting(this);
	}

	@Override
	public void reset() {
		setValue(defaultValue);
	}

	public float getValue() {
		return value;
	}

	public float getMinValue() {
		return minValue;
	}

	public float getMaxValue() {
		return maxValue;
	}

	public void setValue(float value) {
		value = MathUtils.clamp(value, this.minValue, this.maxValue);
		this.value = value;
	}

	public boolean isInteger() {
		return integer;
	}

	public void setInteger(boolean integer) {
		this.integer = integer;
	}

	public float getDefaultValue() {
		return defaultValue;
	}
}