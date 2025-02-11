package com.soarclient.mixin.mixins.minecraft.client.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import com.soarclient.management.mod.impl.render.OverlayEditorMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ScreenEffectRenderer;

@Mixin(ScreenEffectRenderer.class)
public class MixinInGameOverlayRenderer {

	@Inject(method = "renderUnderwaterOverlay", at = @At("HEAD"), cancellable = true)
	private static void renderUnderwaterOverlay(Minecraft client, PoseStack matrices,
			MultiBufferSource vertexConsumers, CallbackInfo ci) {

		if (OverlayEditorMod.getInstance().isEnabled() && OverlayEditorMod.getInstance().isClearWater()) {
			ci.cancel();
		}
	}

	@Inject(method = "renderFireOverlay", at = @At("HEAD"), cancellable = true)
	private static void renderFireOverlay(PoseStack matrices, MultiBufferSource vertexConsumers,
			CallbackInfo ci) {

		if (OverlayEditorMod.getInstance().isEnabled() && OverlayEditorMod.getInstance().isClearFire()) {
			ci.cancel();
		}
	}
}
