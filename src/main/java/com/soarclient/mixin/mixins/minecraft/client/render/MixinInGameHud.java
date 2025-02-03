package com.soarclient.mixin.mixins.minecraft.client.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.soarclient.event.EventBus;
import com.soarclient.event.client.RenderGameOverlayEvent;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;

@Mixin(InGameHud.class)
public class MixinInGameHud {

	@Inject(method = "renderMainHud", at = @At("TAIL"))
	private void renderMainHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		EventBus.getInstance().post(new RenderGameOverlayEvent());
	}
}
