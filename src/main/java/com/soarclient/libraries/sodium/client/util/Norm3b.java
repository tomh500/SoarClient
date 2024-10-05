package com.soarclient.libraries.sodium.client.util;

import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3i;

public class Norm3b {
	private static final float COMPONENT_RANGE = 127.0F;
	private static final float NORM = 0.007874016F;

	static int pack(Vec3i norm) {
		return pack((float) norm.getX(), (float) norm.getY(), (float) norm.getZ());
	}

	public static int pack(float x, float y, float z) {
		int normX = encode(x);
		int normY = encode(y);
		int normZ = encode(z);
		return normZ << 16 | normY << 8 | normX;
	}

	private static int encode(float comp) {
		return (int) (MathHelper.clamp_float(comp, -1.0F, 1.0F) * 127.0F) & 0xFF;
	}

	public static float unpackX(int norm) {
		return (float) ((byte) (norm & 0xFF)) * 0.007874016F;
	}

	public static float unpackY(int norm) {
		return (float) ((byte) (norm >> 8 & 0xFF)) * 0.007874016F;
	}

	public static float unpackZ(int norm) {
		return (float) ((byte) (norm >> 16 & 0xFF)) * 0.007874016F;
	}
}
