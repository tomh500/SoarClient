package com.soarclient.mixin.minecraft.entity.player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.soarclient.event.EventBus;
import com.soarclient.event.impl.AttackEntityEvent;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(PlayerEntity.class)
public class MixinPlayerEntity {

	@Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D"))
	public void onAttack(Entity target, CallbackInfo ci) {

		MinecraftClient client = MinecraftClient.getInstance();

		if ((PlayerEntity) (Object) this == client.player) {
			EventBus.getInstance().post(new AttackEntityEvent(target));
		}
	}
}
