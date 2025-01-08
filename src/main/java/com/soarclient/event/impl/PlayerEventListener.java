package com.soarclient.event.impl;

import com.soarclient.event.api.AbstractEvent;
import com.soarclient.event.api.CancellableEvent;

import net.minecraft.entity.Entity;

public interface PlayerEventListener {
	
	void onUpdate();
	void onSendChat(String message);
	void onMotionUpdate();
	void onJump();
	void onAttackEntity(Entity entity);
	void onDamageEntity(Entity entity);
	
	class UpdateEvent extends AbstractEvent<PlayerEventListener> {

		public static final int ID = 10;
		
		@Override
		public void call(PlayerEventListener listener) {
			listener.onUpdate();
		}
	}
	
	class SendChatEvent extends CancellableEvent<PlayerEventListener> {

		public static final int ID = 11;
		private final String message;
		
		public SendChatEvent(String message) {
			this.message = message;
		}
		
		@Override
		public void call(PlayerEventListener listener) {
			listener.onSendChat(message);
		}
	}
	
	class MotionUpdateEvent extends AbstractEvent<PlayerEventListener> {

		public static final int ID = 12;
		
		@Override
		public void call(PlayerEventListener listener) {
			listener.onMotionUpdate();
		}
	}
	
	class JumpEvent extends AbstractEvent<PlayerEventListener> {

		public static final int ID = 13;
		
		@Override
		public void call(PlayerEventListener listener) {
			listener.onJump();
		}
	}
	
	class AttackEntityEvent extends AbstractEvent<PlayerEventListener> {

		public static final int ID = 14;
		private final Entity entity;
		
		public AttackEntityEvent(Entity entity) {
			this.entity = entity;
		}
		
		@Override
		public void call(PlayerEventListener listener) {
			listener.onAttackEntity(entity);
		}
	}
	
	class DamageEntityEvent extends AbstractEvent<PlayerEventListener> {

		public static final int ID = 15;
		private final Entity entity;
		
		public DamageEntityEvent(Entity entity) {
			this.entity = entity;
		}
		
		@Override
		public void call(PlayerEventListener listener) {
			listener.onDamageEntity(entity);
		}
	}
}
