package com.soarclient.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtils {

	public static float calculateMaxRadius(float x, float y, float width, float height, int mouseX, int mouseY) {

		float topLeft = (float) Math.sqrt(Math.pow(mouseX - x, 2) + Math.pow(mouseY - y, 2));
		float topRight = (float) Math.sqrt(Math.pow(mouseX - (x + width), 2) + Math.pow(mouseY - y, 2));
		float bottomLeft = (float) Math.sqrt(Math.pow(mouseX - x, 2) + Math.pow(mouseY - (y + height), 2));
		float bottomRight = (float) Math.sqrt(Math.pow(mouseX - (x + width), 2) + Math.pow(mouseY - (y + height), 2));

		return Math.max(Math.max(topLeft, topRight), Math.max(bottomLeft, bottomRight));
	}

	public static float roundToPlace(double value, int places) {

		if (places < 0) {
			throw new IllegalArgumentException();
		}

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.floatValue();
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
