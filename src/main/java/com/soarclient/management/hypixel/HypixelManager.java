package com.soarclient.management.hypixel;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.soarclient.management.hypixel.api.HypixelUser;

public class HypixelManager {

	private final Cache<String, HypixelUser> cache = Caffeine.newBuilder().maximumSize(200).build();
	
	public void add(HypixelUser user) {
		cache.put(user.getUuid(), user);
	}
	
	public HypixelUser getByUuid(String uuid) {
		return cache.getIfPresent(uuid);
	}
}
