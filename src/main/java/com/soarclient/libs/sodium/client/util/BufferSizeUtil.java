package com.soarclient.libs.sodium.client.util;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.util.EnumWorldBlockLayer;

public class BufferSizeUtil {
	public static final Map<EnumWorldBlockLayer, Integer> BUFFER_SIZES = new HashMap();

	static {
		BUFFER_SIZES.put(EnumWorldBlockLayer.SOLID, 2097152);
		BUFFER_SIZES.put(EnumWorldBlockLayer.CUTOUT, 131072);
		BUFFER_SIZES.put(EnumWorldBlockLayer.CUTOUT_MIPPED, 131072);
		BUFFER_SIZES.put(EnumWorldBlockLayer.TRANSLUCENT, 262144);
	}
}
