package com.soarclient.management.mods.impl.hud;

import com.soarclient.management.mods.api.SimpleHUDMod;
import com.soarclient.nanovg.font.Icon;

import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class WeatherDisplayMod extends SimpleHUDMod {

	public WeatherDisplayMod() {
		super("mod.weatherdisplay.name", "mod.weatherdisplay.description", Icon.SUNNY);
	}

	@Override
	public String getText() {

		String biome = "";
		String prefix = "Weather: ";
		Chunk chunk = mc.theWorld.getChunkFromBlockCoords(new BlockPos(mc.thePlayer));
		biome = chunk.getBiome(new BlockPos(mc.thePlayer), this.mc.theWorld.getWorldChunkManager()).biomeName;

		if (mc.theWorld.isRaining()) {
			if (biome.contains("Extreme Hills") && mc.thePlayer.posY > 100) {
				return prefix + "Snowing";
			} else {
				return prefix + "Raining";
			}
		}

		if (mc.theWorld.isThundering()) {
			return prefix + "Thundering";
		}

		return prefix + "Cleaning";
	}

	@Override
	public String getIcon() {

		String biome = "";
		Chunk chunk = mc.theWorld.getChunkFromBlockCoords(new BlockPos(mc.thePlayer));
		biome = chunk.getBiome(new BlockPos(mc.thePlayer), this.mc.theWorld.getWorldChunkManager()).biomeName;

		String iconFont = Icon.SUNNY;

		if (mc.theWorld.isRaining()) {
			if (biome.contains("Extreme Hills") && mc.thePlayer.posY > 100) {
				iconFont = Icon.WEATHER_SNOWY;
			} else {
				iconFont = Icon.RAINY;
			}
		}

		if (mc.theWorld.isThundering()) {
			iconFont = Icon.THUNDERSTORM;
		}

		return iconFont;
	}
}
