package com.soarclient.libraries.sodium.client.render.chunk.format;

public class ModelVertexUtil {
	public static short denormalizeVertexPositionFloatAsShort(float value) {
		return (short)((int)(value * 2048.0F));
	}

	public static short denormalizeVertexTextureFloatAsShort(float value) {
		return (short)(Math.round(value * 32768.0F) & 65535);
	}

	public static int encodeLightMapTexCoord(int light) {
		int sl = light >> 16 & 0xFF;
		sl = (sl << 8) + 2048;
		int bl = light & 0xFF;
		bl = (bl << 8) + 2048;
		return sl << 16 | bl;
	}
}
