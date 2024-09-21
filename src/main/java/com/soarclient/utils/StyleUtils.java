package com.soarclient.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class StyleUtils {
	
	public static float dpToPixel(float dp) {
		
		float factor = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
		
		return dpToPixel(dp, 125 / factor);
	}
	
	private static float dpToPixel(float dp, float dpi) {
		return dp * (dpi / 160);
	}
}
