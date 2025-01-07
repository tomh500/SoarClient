package net.minecraft.util;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

public class IntegerCache {
    private static final int CACHE_SIZE = 65535;
    private static final LoadingCache<Integer, Integer> CACHE = Caffeine.newBuilder()
            .maximumSize(CACHE_SIZE)
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build(key -> Integer.valueOf(key));

    public static int getInteger(int value) {
        return CACHE.get(value);
    }

    static {
        for (int i = 0; i < CACHE_SIZE; i++) {
            CACHE.put(i, Integer.valueOf(i));
        }
    }
}