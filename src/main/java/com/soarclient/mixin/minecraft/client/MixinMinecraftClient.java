package com.soarclient.mixin.minecraft.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.soarclient.skia.context.SkiaContext;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {

	@Shadow
	@Final
	private Window window;
	
	@Inject(method = "<init>", at = @At("TAIL"))
	public void init(CallbackInfo ci) {
		SkiaContext.createSurface(window.getWidth(), window.getHeight());
	}
}
