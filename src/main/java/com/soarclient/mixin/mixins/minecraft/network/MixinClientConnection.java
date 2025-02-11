package com.soarclient.mixin.mixins.minecraft.network;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.soarclient.event.EventBus;
import com.soarclient.event.client.ReceivePacketEvent;
import com.soarclient.event.client.SendPacketEvent;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.server.RunningOnDifferentThreadException;

@Mixin(Connection.class)
public abstract class MixinClientConnection {

	@Shadow
	protected static <T extends PacketListener> void handlePacket(Packet<T> packet, PacketListener listener) {
	}

	@Inject(method = "send(Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"), cancellable = true)
	private void onSendPacket(Packet<?> packet, CallbackInfo ci) {

		SendPacketEvent event = new SendPacketEvent(packet);

		EventBus.getInstance().post(event);

		if (event.isCancelled()) {
			ci.cancel();
		}
	}

	@Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true, require = 1)
	private static void onRecievePacket(Packet<?> packet, PacketListener listener, CallbackInfo ci) {

		if (packet instanceof ClientboundBundlePacket bundleS2CPacket) {
			ci.cancel();

			for (Packet<?> packetInBundle : bundleS2CPacket.subPackets()) {
				try {
					handlePacket(packetInBundle, listener);
				} catch (RunningOnDifferentThreadException ignored) {
				}
			}
			return;
		}

		ReceivePacketEvent event = new ReceivePacketEvent(packet);
		EventBus.getInstance().post(event);

		if (event.isCancelled()) {
			ci.cancel();
		}
	}
}
