package com.soarclient.mixin.minecraft.client.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.soarclient.event.EventBus;
import com.soarclient.event.client.RenderSkiaEvent;
import com.soarclient.management.mod.impl.settings.HUDModSettings;
import com.soarclient.shader.impl.KawaseBlur;
import com.soarclient.skia.Skia;
import com.soarclient.skia.context.SkiaContext;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;render(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V", shift = At.Shift.BEFORE))
	public void render(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
		
		if(HUDModSettings.getInstance().getBlurSetting().isEnabled()) {
			KawaseBlur.INGAME_BLUR.draw((int) HUDModSettings.getInstance().getBlurIntensitySetting().getValue());
		}
		
		SkiaContext.draw((context) -> {
			Skia.save();
			Skia.scale((float) MinecraftClient.getInstance().getWindow().getScaleFactor());
			EventBus.getInstance().post(new RenderSkiaEvent());
			Skia.restore();
		});
	}
}
