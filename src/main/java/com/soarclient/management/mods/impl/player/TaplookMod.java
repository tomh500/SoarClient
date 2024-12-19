package com.soarclient.management.mods.impl.player;

import java.util.Arrays;

import org.lwjgl.input.Keyboard;

import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.ClientTickEvent;
import com.soarclient.management.mods.Mod;
import com.soarclient.management.mods.ModCategory;
import com.soarclient.management.mods.settings.impl.ComboSetting;
import com.soarclient.management.mods.settings.impl.KeybindSetting;
import com.soarclient.utils.Icon;

public class TaplookMod extends Mod {

	private boolean active;
	private int prevPerspective;

	private ComboSetting perspectiveSetting = new ComboSetting("setting.perspective", "setting.perspective.description",
			Icon.CAMERASWITCH, this, Arrays.asList("setting.front", "setting.behind"), "setting.front");
	private KeybindSetting keybindSetting = new KeybindSetting("setting.keybind", "setting.keybind.description",
			Icon.KEYBOARD, this, Keyboard.KEY_V);

	public TaplookMod() {
		super("mod.taplook.name", "mod.taplook.description", Icon.TOUCH_APP, ModCategory.PLAYER);
	}

	@EventHandler
	public void onClientTick(ClientTickEvent event) {
		if (keybindSetting.isKeyDown()) {
			if (!active) {
				this.start();
			}
		} else if (active) {
			this.stop();
		}
	}

	private void start() {

		String option = perspectiveSetting.getOption();
		int perspective = option.equals("setting.front") ? 2 : 1;

		active = true;
		prevPerspective = mc.gameSettings.thirdPersonView;
		mc.gameSettings.thirdPersonView = perspective;
		mc.renderGlobal.setDisplayListEntitiesDirty();
	}

	private void stop() {
		active = false;
		mc.gameSettings.thirdPersonView = prevPerspective;
		mc.renderGlobal.setDisplayListEntitiesDirty();
	}
}
