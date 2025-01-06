package com.soarclient.event.impl;

import com.soarclient.event.Event;

import net.minecraft.entity.Entity;

public class AttackEntityEvent extends Event {

	private Entity entity;

	public AttackEntityEvent(Entity entity) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}
}