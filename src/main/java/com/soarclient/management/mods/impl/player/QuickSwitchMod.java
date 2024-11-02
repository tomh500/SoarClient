package com.soarclient.management.mods.impl.player;

import org.lwjgl.input.Keyboard;

import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.ClientTickEvent;
import com.soarclient.management.mods.Mod;
import com.soarclient.management.mods.ModCategory;
import com.soarclient.management.mods.settings.impl.KeybindSetting;
import com.soarclient.nanovg.font.Icon;
import com.soarclient.utils.PlayerUtils;

import net.minecraft.init.Items;

public class QuickSwitchMod extends Mod {

	private KeybindSetting swordSetting = new KeybindSetting("setting.sowrd", "setting.quickswitch.sword.description",
			Icon.KEYBOARD, this, Keyboard.KEY_NONE);
	private KeybindSetting blockSetting = new KeybindSetting("setting.block", "setting.quickswitch.block.description",
			Icon.KEYBOARD, this, Keyboard.KEY_NONE);
	private KeybindSetting rodSetting = new KeybindSetting("setting.rod", "setting.quickswitch.rod.description",
			Icon.KEYBOARD, this, Keyboard.KEY_NONE);
	private KeybindSetting axeSetting = new KeybindSetting("setting.axe", "setting.quickswitch.axe.description",
			Icon.KEYBOARD, this, Keyboard.KEY_NONE);
	private KeybindSetting pickaxeSetting = new KeybindSetting("setting.pickaxe",
			"setting.quickswitch.pickaxe.description", Icon.KEYBOARD, this, Keyboard.KEY_NONE);
	private KeybindSetting bowSetting = new KeybindSetting("setting.bow", "setting.quickswitch.bow.description",
			Icon.KEYBOARD, this, Keyboard.KEY_NONE);

	public QuickSwitchMod() {
		super("mod.quickswitch.name", "mod.quickswitch.description", Icon.SWORDS, ModCategory.PLAYER);
	}

	@EventHandler
	public void onClientTick(ClientTickEvent event) {

		if (swordSetting.isKeyDown()) {
			setCurrentItem(PlayerUtils.getBestSword(mc.thePlayer));
		}

		if (blockSetting.isKeyDown()) {
			setCurrentItem(PlayerUtils.getBestBlock(mc.thePlayer));
		}

		if (rodSetting.isKeyDown()) {
			setCurrentItem(PlayerUtils.getItemSlot(Items.fishing_rod));
		}

		if (axeSetting.isKeyDown()) {
			setCurrentItem(PlayerUtils.getBestAxe(mc.thePlayer));
		}

		if (pickaxeSetting.isKeyDown()) {
			setCurrentItem(PlayerUtils.getBestPickaxe(mc.thePlayer));
		}

		if (bowSetting.isKeyDown()) {
			setCurrentItem(PlayerUtils.getBestBow(mc.thePlayer));
		}
	}

	private void setCurrentItem(int slot) {
		mc.thePlayer.inventory.currentItem = slot;
	}
}
