package com.soarclient.mixin.minecraft.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.soarclient.event.EventBus;
import com.soarclient.event.impl.DamageEntityEvent;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {

	@Inject(method = "applyDamage", at = @At("HEAD"))
	public void damage(ServerWorld world, DamageSource source, float amount, CallbackInfo ci) {
		if (source.getAttacker() instanceof PlayerEntity) {
			EventBus.getInstance().post(new DamageEntityEvent((LivingEntity) (Object) this));
		}
	}
}
