package com.soarclient.utils;

import java.awt.Color;

import com.soarclient.utils.math.MathUtils;

public class ColorUtils {

	public static Color blend(Color color1, Color color2) {

		int r1 = color1.getRed();
		int g1 = color1.getGreen();
		int b1 = color1.getBlue();
		float a1 = color1.getAlpha() / 255.0F;

		int r2 = color2.getRed();
		int g2 = color2.getGreen();
		int b2 = color2.getBlue();
		float a2 = color2.getAlpha() / 255.0F;

		float alpha = a1 + a2 * (1 - a1);
		int red = (int) ((r1 * a1 + r2 * a2 * (1 - a1)) / alpha);
		int green = (int) ((g1 * a1 + g2 * a2 * (1 - a1)) / alpha);
		int blue = (int) ((b1 * a1 + b2 * a2 * (1 - a1)) / alpha);

		return applyAlpha(new Color(red, green, blue), alpha);
	}

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
