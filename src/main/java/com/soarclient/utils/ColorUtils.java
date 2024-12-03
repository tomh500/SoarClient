package com.soarclient.utils;

import java.awt.Color;

public class ColorUtils {

	public static Color applyAlpha(Color color, int alpha) {

		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();

		return new Color(red, green, blue, MathUtils.clamp(alpha, 0, 255));
	}

	public static Color applyAlpha(Color color, float alpha) {
		return applyAlpha(color, (int) (alpha * 255));
	}
}
