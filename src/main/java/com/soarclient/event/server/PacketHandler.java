package com.soarclient.event.server;

import com.soarclient.event.EventBus;
import com.soarclient.event.client.ReceivePacketEvent;
import com.soarclient.event.client.SendPacketEvent;
import com.soarclient.event.server.impl.AttackEntityEvent;
import com.soarclient.event.server.impl.DamageEntityEvent;
import com.soarclient.mixin.minecraft.network.packet.PlayerInteractEntityC2SPacketAccessor;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityDamageS2CPacket;

public class PacketHandler {

	public final EventBus.EventListener<SendPacketEvent> onSendPacket = event -> {

		Packet<?> basePacket = event.getPacket();

		if (basePacket instanceof PlayerInteractEntityC2SPacket) {

			PlayerInteractEntityC2SPacket packet = (PlayerInteractEntityC2SPacket) basePacket;
			PlayerInteractEntityC2SPacket.InteractType type = ((PlayerInteractEntityC2SPacketAccessor) packet)
					.getInteractTypeHandler().getType();

			if (type.equals(PlayerInteractEntityC2SPacket.InteractType.ATTACK)) {
				EventBus.getInstance()
						.post(new AttackEntityEvent(((PlayerInteractEntityC2SPacketAccessor) packet).entityId()));
			}
		}
	};

	public final EventBus.EventListener<ReceivePacketEvent> onReceivePacket = event -> {

		Packet<?> basePacket = event.getPacket();

		if (basePacket instanceof EntityDamageS2CPacket) {

			EntityDamageS2CPacket packet = (EntityDamageS2CPacket) basePacket;

			EventBus.getInstance().post(new DamageEntityEvent(packet.entityId()));
		}
	};
}
