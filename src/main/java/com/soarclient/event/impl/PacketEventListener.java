package com.soarclient.event.impl;

import com.soarclient.event.api.CancellableEvent;

import net.minecraft.network.Packet;

public interface PacketEventListener {
	
	void onSendPacket(Packet<?> packet);
	void onReceivePacket(Packet<?> packet);
	
	class SendPacketEvent extends CancellableEvent<PacketEventListener> {

		public static final int ID = 7;
		private final Packet<?> packet;
		
		public SendPacketEvent(Packet<?> packet) {
			this.packet = packet;
		}
		
		@Override
		public void call(PacketEventListener listener) {
			listener.onSendPacket(packet);
		}
	}
	
	class ReceivePacketEvent extends CancellableEvent<PacketEventListener> {

		public static final int ID = 8;
		private final Packet<?> packet;
		
		public ReceivePacketEvent(Packet<?> packet) {
			this.packet = packet;
		}
		
		@Override
		public void call(PacketEventListener listener) {
			listener.onReceivePacket(packet);
		}
	}
}
