package com.soarclient.libraries.sodium.client.util.color;

public class ColorABGR implements ColorU8 {
	public static int pack(int r, int g, int b, int a) {
		return (a & 0xFF) << 24 | (b & 0xFF) << 16 | (g & 0xFF) << 8 | r & 0xFF;
	}

	public static int pack(float r, float g, float b, float a) {
		return pack((int)(r * 255.0F), (int)(g * 255.0F), (int)(b * 255.0F), (int)(a * 255.0F));
	}

	public static int mul(int color, float rw, float gw, float bw) {
		float r = (float)unpackRed(color) * rw;
		float g = (float)unpackGreen(color) * gw;
		float b = (float)unpackBlue(color) * bw;
		return pack((int)r, (int)g, (int)b, 255);
	}

	public static int mul(int color, float w) {
		return mul(color, w, w, w);
	}

	public static int unpackRed(int color) {
		return color & 0xFF;
	}

	public static int unpackGreen(int color) {
		return color >> 8 & 0xFF;
	}

	public static int unpackBlue(int color) {
		return color >> 16 & 0xFF;
	}

	public static int unpackAlpha(int color) {
		return color >> 24 & 0xFF;
	}

	public static int pack(float r, float g, float b) {
		return pack(r, g, b, 255.0F);
	}
}
