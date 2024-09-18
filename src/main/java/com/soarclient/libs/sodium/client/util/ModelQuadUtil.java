package com.soarclient.libs.sodium.client.util;

import com.soarclient.libs.sodium.common.util.DirectionUtil;

import net.minecraft.util.EnumFacing;

public class ModelQuadUtil {
	public static final int POSITION_INDEX = 0;
	public static final int COLOR_INDEX = 3;
	public static final int TEXTURE_INDEX = 4;
	public static final int NORMAL_INDEX = 6;
	public static final int VERTEX_SIZE = 7;
	public static final int VERTEX_SIZE_BYTES = 28;
	static final int[] NORMALS = new int[DirectionUtil.ALL_DIRECTIONS.length];

	public static int getFacingNormal(EnumFacing facing) {
		return NORMALS[facing.ordinal()];
	}

	public static int getFacingNormal(EnumFacing facing, int bakedNormal) {
		return !hasNormal(bakedNormal) ? NORMALS[facing.ordinal()] : bakedNormal;
	}

	public static boolean hasNormal(int n) {
		return (n & 16777215) != 0;
	}

	public static int vertexOffset(int vertexIndex) {
		return vertexIndex * 7;
	}

	public static int mergeBakedLight(int packedLight, int calcLight) {
		if (packedLight == 0) {
			return calcLight;
		} else {
			int psl = packedLight >> 16 & 0xFF;
			int csl = calcLight >> 16 & 0xFF;
			int pbl = packedLight & 0xFF;
			int cbl = calcLight & 0xFF;
			int bl = Math.max(pbl, cbl);
			int sl = Math.max(psl, csl);
			return sl << 16 | bl;
		}
	}

	static {
		for (int i = 0; i < NORMALS.length; i++) {
			NORMALS[i] = Norm3b.pack(DirectionUtil.ALL_DIRECTIONS[i].getDirectionVec());
		}
	}
}
