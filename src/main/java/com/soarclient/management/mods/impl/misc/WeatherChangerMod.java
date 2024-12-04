package com.soarclient.management.mods.impl.misc;

import java.util.Arrays;

import com.soarclient.management.mods.Mod;
import com.soarclient.management.mods.ModCategory;
import com.soarclient.management.mods.settings.impl.ComboSetting;
import com.soarclient.nanovg.font.Icon;

public class WeatherChangerMod extends Mod {

	private static WeatherChangerMod instance;

	private ComboSetting weatherSetting = new ComboSetting("setting.weather", "setting.weather.description", Icon.CLOUD,
			this, Arrays.asList("setting.clear", "setting.rain", "setting.snow", "setting.thunder"), "setting.clear");

	public WeatherChangerMod() {
		super("mod.weatherchanger.name", "mod.weatherchanger.description", Icon.PARTLY_CLOUDY_DAY, ModCategory.MISC);

		instance = this;
	}

	public static WeatherChangerMod getInstance() {
		return instance;
	}

	public boolean isRaining() {
		return !weatherSetting.getOption().equals("setting.clear");
	}

	public boolean isThundering() {
		return weatherSetting.getOption().equals("setting.thunder");
	}

	public boolean isSnowing() {
		return weatherSetting.getOption().equals("setting.snow");
	}
}