package com.soarclient.management.hypixel;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.soarclient.event.EventBus;
import com.soarclient.management.hypixel.api.HypixelUser;
import com.soarclient.management.hypixel.handler.HypixelHandler;

public class HypixelManager {

	private final Cache<String, HypixelUser> cache = Caffeine.newBuilder().maximumSize(200).build();
	
	public HypixelManager() {
		EventBus.getInstance().register(new HypixelHandler());
	}
	
	public void add(HypixelUser user) {
		cache.put(user.getUuid(), user);
	}
	
	public HypixelUser getByUuid(String uuid) {
		return cache.getIfPresent(uuid);
	}
}
