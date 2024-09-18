package com.soarclient.libs.sodium.common.util;

import net.minecraft.util.EnumFacing;

public class DirectionUtil {
	public static final EnumFacing[] ALL_DIRECTIONS = EnumFacing.values();
	public static final int DIRECTION_COUNT = ALL_DIRECTIONS.length;
	public static final EnumFacing[] HORIZONTAL_DIRECTIONS = new EnumFacing[]{EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST};
}
