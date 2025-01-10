package com.soarclient.management.mod.impl.settings;

import org.lwjgl.input.Keyboard;

import com.soarclient.event.EventBus;
import com.soarclient.event.impl.ClientTickEventListener;
import com.soarclient.gui.modmenu.GuiModMenu;
import com.soarclient.libraries.material3.hct.Hct;
import com.soarclient.management.mod.Mod;
import com.soarclient.management.mod.ModCategory;
import com.soarclient.management.mod.settings.impl.BooleanSetting;
import com.soarclient.management.mod.settings.impl.HctColorSetting;
import com.soarclient.management.mod.settings.impl.KeybindSetting;
import com.soarclient.skia.font.Icon;

import net.minecraft.client.gui.GuiScreen;

public class ModMenuSettings extends Mod implements ClientTickEventListener {

	private static ModMenuSettings instance;

	private KeybindSetting keybindSetting = new KeybindSetting("setting.keybind", "setting.keybind.description",
			Icon.KEYBOARD, this, Keyboard.KEY_RSHIFT);
	private BooleanSetting darkModeSetting = new BooleanSetting("setting.darkmode", "setting.darkmode.description",
			Icon.DARK_MODE, this, false);
	private HctColorSetting hctColorSetting = new HctColorSetting("setting.color", "setting.color.description",
			Icon.PALETTE, this, Hct.from(220, 26, 6));

	private GuiScreen modMenu;

	public ModMenuSettings() {
		super("mod.modmenu.name", "mod.modmenu.description", Icon.MENU, ModCategory.MISC);

		instance = this;
		this.setHidden(true);
		this.setEnabled(true);
	}

	@Override
	public void onClientTick() {
		if (keybindSetting.isPressed()) {

			if (modMenu == null) {
				modMenu = new GuiModMenu().build();
			}
			
			mc.displayGuiScreen(modMenu);
		}
	}
	
	@Override
	public void onEnable() {
		EventBus.getInstance().register(ClientTickEvent.ID, this);
	}

	@Override
	public void onDisable() {
		EventBus.getInstance().unregister(ClientTickEvent.ID, this);
	}
	
	public static ModMenuSettings getInstance() {
		return instance;
	}

	public BooleanSetting getDarkModeSetting() {
		return darkModeSetting;
	}

	public HctColorSetting getHctColorSetting() {
		return hctColorSetting;
	}
}
