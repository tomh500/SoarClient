package com.soarclient.libs.sodium.client.util.color;

public class ColorARGB implements ColorU8 {
	public static int pack(int r, int g, int b, int a) {
		return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
	}

	public static int unpackAlpha(int color) {
		return color >> 24 & 0xFF;
	}

	public static int unpackRed(int color) {
		return color >> 16 & 0xFF;
	}

	public static int unpackGreen(int color) {
		return color >> 8 & 0xFF;
	}

	public static int unpackBlue(int color) {
		return color & 0xFF;
	}

	public static int toABGR(int color, int alpha) {
		return Integer.reverseBytes(color << 8 | alpha);
	}

	public static int toABGR(int color) {
		return Integer.reverseBytes(color << 8);
	}
}
