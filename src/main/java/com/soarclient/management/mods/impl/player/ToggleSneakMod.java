package com.soarclient.management.mods.impl.player;

import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.KeyEvent;
import com.soarclient.event.impl.PlayerUpdateEvent;
import com.soarclient.management.mods.Mod;
import com.soarclient.management.mods.ModCategory;
import com.soarclient.utils.Icon;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.settings.KeyBinding;

public class ToggleSneakMod extends Mod {

	private boolean toggle;

	public ToggleSneakMod() {
		super("mod.togglesneak.name", "mod.togglesneak.description", Icon.AIRLINE_SEAT_RECLINE_EXTRA,
				ModCategory.PLAYER);

		toggle = false;
	}

	@EventHandler
	public void onKey(KeyEvent event) {
		if (event.getKeyCode() == mc.gameSettings.keyBindSneak.getKeyCode()) {
			toggle = !toggle;
		}
	}

	@EventHandler
	public void onPlayerUpdate(PlayerUpdateEvent event) {
		if (mc.currentScreen instanceof Gui) {
			setSneak(false);
		} else {
			setSneak(toggle);
		}
	}

	@Override
	public void onDisable() {
		super.onDisable();
		toggle = false;
		setSneak(false);
	}

	private void setSneak(boolean state) {
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), state);
	}
}
