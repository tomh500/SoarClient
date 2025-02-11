package com.soarclient.mixin.mixins.minecraft.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.soarclient.management.mod.impl.hud.JumpResetIndicatorMod;
import com.soarclient.management.mod.impl.player.NoJumpDelayMod;
import com.soarclient.mixin.interfaces.IMixinLivingEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity implements IMixinLivingEntity {

	@Shadow
	private int jumpingCooldown;

	@Shadow
	public int handSwingTicks;

	@Shadow
	public boolean handSwinging;

	@Shadow
	public InteractionHand preferredHand;

	@Shadow
	public abstract int getHandSwingDuration();

	@Inject(method = "tickMovement", at = @At("HEAD"))
	public void onNoJumpDelay(CallbackInfo ci) {
		if (NoJumpDelayMod.getInstance().isEnabled()) {
			jumpingCooldown = 0;
		}
	}

	@Inject(method = "jump", at = @At("HEAD"))
	private void onJump(CallbackInfo info) {

		JumpResetIndicatorMod mod = JumpResetIndicatorMod.getInstance();
		Minecraft client = Minecraft.getInstance();

		if ((LivingEntity) (Object) this == client.player) {
			mod.setJumpAge(client.player.tickCount);
			mod.setLastTime(System.currentTimeMillis());
		}
	}

	@Inject(method = "onDamaged", at = @At("HEAD"))
	private void onDamage(CallbackInfo info) {

		JumpResetIndicatorMod mod = JumpResetIndicatorMod.getInstance();
		Minecraft client = Minecraft.getInstance();

		if ((LivingEntity) (Object) this == client.player) {
			mod.setHurtAge(client.player.tickCount);
		}
	}

	@Override
	public void fakeSwingHand(InteractionHand hand) {
		if (!this.handSwinging || this.handSwingTicks >= this.getHandSwingDuration() / 2 || this.handSwingTicks < 0) {
			this.handSwingTicks = -1;
			this.handSwinging = true;
			this.preferredHand = hand;
		}
	}
}
