package com.soarclient.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.world.WorldSettings;

public class PlayerUtils {

	private static Minecraft mc = Minecraft.getMinecraft();
	
	public static boolean isSpectator() {
		NetworkPlayerInfo networkplayerinfo = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getGameProfile().getId());
		return networkplayerinfo != null && networkplayerinfo.getGameType() == WorldSettings.GameType.SPECTATOR;
	}

	public static boolean isCreative() {
		NetworkPlayerInfo networkplayerinfo = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getGameProfile().getId());
		return networkplayerinfo != null && networkplayerinfo.getGameType() == WorldSettings.GameType.CREATIVE;
	}

	public static boolean isSurvival() {
		NetworkPlayerInfo networkplayerinfo = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getGameProfile().getId());
		return networkplayerinfo != null && networkplayerinfo.getGameType() == WorldSettings.GameType.SURVIVAL;
	}
}
