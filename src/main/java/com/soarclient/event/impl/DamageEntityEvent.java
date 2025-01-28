package com.soarclient.event.impl;

import com.soarclient.event.Event;

import net.minecraft.entity.Entity;

public class DamageEntityEvent extends Event {
	
	private final Entity entity;
	
	public DamageEntityEvent(Entity entity) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}
}
