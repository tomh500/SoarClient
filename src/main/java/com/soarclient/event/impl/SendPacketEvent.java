package com.soarclient.event.impl;

import com.soarclient.event.CancellableEvent;

import net.minecraft.network.Packet;

public class SendPacketEvent extends CancellableEvent {

	private Packet<?> packet;

	public SendPacketEvent(Packet<?> packet) {
		this.packet = packet;
	}

	public Packet<?> getPacket() {
		return packet;
	}
}