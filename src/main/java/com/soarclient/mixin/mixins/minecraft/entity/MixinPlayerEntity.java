package com.soarclient.mixin.mixins.minecraft.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.soarclient.management.mod.impl.player.OldAnimationsMod;
import net.minecraft.world.entity.player.Player;

@Mixin(Player.class)
public class MixinPlayerEntity {

	@Inject(method = "getAttackCooldownProgress", at = @At("HEAD"), cancellable = true)
	public void disableCooldown(CallbackInfoReturnable<Float> cir) {
		if (OldAnimationsMod.getInstance().isEnabled() && OldAnimationsMod.getInstance().isDisableAttackCooldown()) {
			cir.setReturnValue(1F);
		}
	}
}
