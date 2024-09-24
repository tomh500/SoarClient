package com.soarclient.stk.styles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Style {
	
	public static float dpToPixel(float dp) {
		
		float factor = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
		
		return dpToPixel(dp, 100 / factor);
	}
	
	private static float dpToPixel(float dp, float dpi) {
		return dp * (dpi / 160);
	}
}
