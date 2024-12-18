package com.soarclient.libraries.sodium.client.gui.options.storage;

import com.soarclient.libraries.sodium.SodiumClientMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

public class MinecraftOptionsStorage implements OptionStorage<GameSettings> {
	private final Minecraft client = Minecraft.getMinecraft();

	public GameSettings getData() {
		return this.client.gameSettings;
	}

	@Override
	public void save() {
		this.getData().saveOptions();
		SodiumClientMod.logger().info("Flushed changes to Minecraft configuration");
	}
}
