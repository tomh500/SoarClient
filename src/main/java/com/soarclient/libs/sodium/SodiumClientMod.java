package com.soarclient.libs.sodium;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.soarclient.libs.sodium.client.gui.SodiumGameOptions;

import net.minecraft.client.Minecraft;

public class SodiumClientMod  {
	
	public static final String MODID = "oldium";
	public static final String MODNAME = "Oldium";
	private static SodiumGameOptions CONFIG;
	public static Logger LOGGER = LogManager.getLogger("Oldium");

	public static SodiumGameOptions options() {
		if (CONFIG == null) {
			CONFIG = loadConfig();
		}

		return CONFIG;
	}

	public static Logger logger() {
		if (LOGGER == null) {
			LOGGER = LogManager.getLogger("Oldium");
		}

		return LOGGER;
	}

	private static SodiumGameOptions loadConfig() {
		return SodiumGameOptions.load(Minecraft.getMinecraft().mcDataDir.toPath().resolve("config").resolve("oldium-options.json"));
	}

	public static boolean isDirectMemoryAccessEnabled() {
		return options().advanced.allowDirectMemoryAccess;
	}
}
