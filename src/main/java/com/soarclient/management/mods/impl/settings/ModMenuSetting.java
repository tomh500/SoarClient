package com.soarclient.management.mods.impl.settings;

import org.lwjgl.input.Keyboard;

import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.ClientTickEvent;
import com.soarclient.gui.GuiTest;
import com.soarclient.libraries.material3.hct.Hct;
import com.soarclient.management.mods.Mod;
import com.soarclient.management.mods.ModCategory;
import com.soarclient.management.mods.settings.impl.BooleanSetting;
import com.soarclient.management.mods.settings.impl.HctColorSetting;
import com.soarclient.management.mods.settings.impl.KeybindSetting;
import com.soarclient.management.mods.settings.impl.NumberSetting;
import com.soarclient.utils.Icon;

public class ModMenuSetting extends Mod {

	private static ModMenuSetting instance;
	
	private KeybindSetting keybindSetting = new KeybindSetting("setting.keybind", "setting.keybind.description",
			Icon.KEYBOARD, this, Keyboard.KEY_RSHIFT);
	private BooleanSetting darkModeSetting = new BooleanSetting("setting.darkmode", "setting.darkmode.description",
			Icon.DARK_MODE, this, false);
	private HctColorSetting hctColorSetting = new HctColorSetting("setting.color", "setting.color.description",
			Icon.PALETTE, this, Hct.from(220, 26, 6));
	private BooleanSetting blurSetting = new BooleanSetting("setting.blur", "setting.blur.description", Icon.LENS_BLUR,
			this, true);
	private NumberSetting blurIntensitySetting = new NumberSetting("setting.intensity", "setting.intensity.blur",
			Icon.LENS_BLUR, this, 24, 1, 60, 1);
	
	public ModMenuSetting() {
		super("mod.modmenu.name", "mod.modmenu.description", Icon.MENU, ModCategory.MISC);
		
		instance = this;
		this.setHidden(true);
		this.setEnabled(true);
	}
	
	@EventHandler
	public void onClientTick(ClientTickEvent event) {

		if (keybindSetting.isPressed()) {
			mc.displayGuiScreen(new GuiTest().build());
		}
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		this.setEnabled(true);
	}

	public static ModMenuSetting getInstance() {
		return instance;
	}

	public KeybindSetting getKeybindSetting() {
		return keybindSetting;
	}

	public BooleanSetting getDarkModeSetting() {
		return darkModeSetting;
	}

	public HctColorSetting getHctColorSetting() {
		return hctColorSetting;
	}

	public BooleanSetting getBlurSetting() {
		return blurSetting;
	}

	public NumberSetting getBlurIntensitySetting() {
		return blurIntensitySetting;
	}
}
