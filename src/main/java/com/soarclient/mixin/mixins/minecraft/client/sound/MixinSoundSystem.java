package com.soarclient.mixin.mixins.minecraft.client.sound;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.soarclient.management.mod.impl.player.OldAnimationsMod;

@Mixin(SoundEngine.class)
public class MixinSoundSystem {

	@Unique
	private final List<ResourceLocation> newPvPSounds = new ArrayList<>() {
		private static final long serialVersionUID = 1L;
		{
			add(SoundEvents.PLAYER_ATTACK_KNOCKBACK.location());
			add(SoundEvents.PLAYER_ATTACK_SWEEP.location());
			add(SoundEvents.PLAYER_ATTACK_CRIT.location());
			add(SoundEvents.PLAYER_ATTACK_STRONG.location());
			add(SoundEvents.PLAYER_ATTACK_WEAK.location());
			add(SoundEvents.PLAYER_ATTACK_NODAMAGE.location());
		}
	};

	@Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At("HEAD"), cancellable = true)
	public void oldAnimations$disableNewPvPSounds(SoundInstance sound, CallbackInfo ci) {

		if (OldAnimationsMod.getInstance().isEnabled() && OldAnimationsMod.getInstance().isOldPvPSounds()
				&& newPvPSounds.contains(sound.getLocation())) {
			ci.cancel();
			return;
		}
	}
}
