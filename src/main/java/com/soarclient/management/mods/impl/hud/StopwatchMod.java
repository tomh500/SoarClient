package com.soarclient.management.mods.impl.hud;

import java.text.DecimalFormat;

import org.lwjgl.input.Keyboard;

import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.ClientTickEvent;
import com.soarclient.management.mods.api.SimpleHUDMod;
import com.soarclient.management.mods.settings.impl.KeybindSetting;
import com.soarclient.nanovg.font.Icon;
import com.soarclient.utils.TimerUtils;

public class StopwatchMod extends SimpleHUDMod {

	private TimerUtils timer = new TimerUtils();
	private int pressCount;
	private float currentTime;
	private DecimalFormat timeFormat = new DecimalFormat("0.00");

	private KeybindSetting keybindSetting = new KeybindSetting("setting.keybind", "setting.keybind.description",
			Icon.KEYBOARD, this, Keyboard.KEY_P);

	public StopwatchMod() {
		super("mod.stopwatch.name", "mod.stopwatch.description", Icon.TIMER);
	}

	@EventHandler
	public void onClientTick(ClientTickEvent event) {

		if (keybindSetting.isPressed()) {
			pressCount++;
		}

		switch (pressCount) {
		case 0:
			timer.reset();
			break;
		case 1:
			currentTime = (timer.getElapsedTime() / 1000F);
			break;
		case 3:
			timer.reset();
			currentTime = 0;
			pressCount = 0;
			break;
		}
	}

	@Override
	public String getText() {
		return timeFormat.format(currentTime) + " s";
	}

	@Override
	public String getIcon() {
		return Icon.TIMER;
	}

	@Override
	public void onEnable() {

		super.onEnable();

		if (timer != null) {
			timer.reset();
		}

		pressCount = 0;
		currentTime = 0;
	}
}
