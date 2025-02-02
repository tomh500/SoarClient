package com.soarclient.mixin.mixins.minecraft.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.soarclient.event.EventBus;
import com.soarclient.event.client.PlayerDirectionChangeEvent;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

@Mixin(Entity.class)
public abstract class MixinEntity {

	@Shadow
	public abstract float getPitch();

	@Shadow
	public abstract float getYaw();

	@Inject(method = "changeLookDirection", at = @At("HEAD"))
	private void onPlayerDirectionChange(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {

		float prevPitch = getPitch();
		float prevYaw = getYaw();
		float pitch = prevPitch + (float) (cursorDeltaY * .15);
		float yaw = prevYaw + (float) (cursorDeltaX * .15);
		pitch = MathHelper.clamp(pitch, -90.0F, 90.0F);

		EventBus.getInstance().post(new PlayerDirectionChangeEvent(prevPitch, prevYaw, pitch, yaw));
	}
}
