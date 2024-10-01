package com.soarclient.ui.math;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Style {

	public static float DPI = 100;
	
	public static float px(float px) {
	    ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
	    return px * (160 / DPI) * ((DPI / sr.getScaleFactor()) / 160);
	}
}
