package com.soarclient.utils.math;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class MathUtils {

	public static float dpToPixel(float dp) {
		
		float factor = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
		
		return dpToPixel(dp, 100 / factor);
	}
	
	private static float dpToPixel(float dp, float dpi) {
		return dp * (dpi / 160);
	}
	
	public static int clamp(int number, int min, int max) {
		return number < min ? min : Math.min(number, max);
	}

	public static float clamp(float number, float min, float max) {
		return number < min ? min : Math.min(number, max);
	}

	public static double clamp(double number, double min, double max) {
		return number < min ? min : Math.min(number, max);
	}
}
