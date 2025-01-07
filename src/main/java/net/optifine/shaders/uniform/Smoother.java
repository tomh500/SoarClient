package net.optifine.shaders.uniform;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.optifine.util.CounterInt;
import net.optifine.util.SmoothFloat;

public class Smoother {
	private static final Int2ObjectOpenHashMap<SmoothFloat> mapSmoothValues = new Int2ObjectOpenHashMap<>();
	private static final CounterInt counterIds = new CounterInt(1);

	public static float getSmoothValue(int id, float value, float timeFadeUpSec, float timeFadeDownSec) {
		synchronized (mapSmoothValues) {
			SmoothFloat smoothfloat = mapSmoothValues.get(id);

			if (smoothfloat == null) {
				smoothfloat = new SmoothFloat(value, timeFadeUpSec, timeFadeDownSec);
				mapSmoothValues.put(id, smoothfloat);
			}

			float f = smoothfloat.getSmoothValue(value, timeFadeUpSec, timeFadeDownSec);
			return f;
		}
	}

	public static int getNextId() {
		synchronized (counterIds) {
			return counterIds.nextValue();
		}
	}

	public static void resetValues() {
		synchronized (mapSmoothValues) {
			mapSmoothValues.clear();
		}
	}
}
