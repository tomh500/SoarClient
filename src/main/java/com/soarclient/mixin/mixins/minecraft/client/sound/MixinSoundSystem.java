package com.soarclient.mixin.mixins.minecraft.client.sound;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.soarclient.management.mod.impl.player.OldAnimationsMod;

import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

@Mixin(SoundSystem.class)
public class MixinSoundSystem {

	@Unique
	private final List<Identifier> newPvPSounds = new ArrayList<>() {
		private static final long serialVersionUID = 1L;
		{
			add(SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK.id());
			add(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP.id());
			add(SoundEvents.ENTITY_PLAYER_ATTACK_CRIT.id());
			add(SoundEvents.ENTITY_PLAYER_ATTACK_STRONG.id());
			add(SoundEvents.ENTITY_PLAYER_ATTACK_WEAK.id());
			add(SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE.id());
		}
	};

	@Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At("HEAD"), cancellable = true)
	public void oldAnimations$disableNewPvPSounds(SoundInstance sound, CallbackInfo ci) {

		if (OldAnimationsMod.getInstance().isEnabled() && OldAnimationsMod.getInstance().isOldPvPSounds()
				&& newPvPSounds.contains(sound.getId())) {
			ci.cancel();
			return;
		}
	}
}
