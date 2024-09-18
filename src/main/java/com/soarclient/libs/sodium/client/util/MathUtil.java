package com.soarclient.libs.sodium.client.util;

import net.minecraft.util.BlockPos;

public class MathUtil {
	public static boolean isPowerOfTwo(int n) {
		return (n & n - 1) == 0;
	}

	public static long hashPos(BlockPos pos) {
		return cantor((long)pos.getX(), cantor((long)pos.getY(), (long)pos.getZ()));
	}

	private static long cantor(long a, long b) {
		return (a + b + 1L) * (a + b) / 2L + b;
	}

	public static double lerp(double delta, double start, double end) {
		return start + delta * (end - start);
	}
}
