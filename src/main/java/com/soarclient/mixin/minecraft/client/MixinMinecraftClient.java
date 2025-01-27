package com.soarclient.mixin.minecraft.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.soarclient.Soar;
import com.soarclient.event.EventBus;
import com.soarclient.event.impl.ClientTickEvent;
import com.soarclient.event.impl.GameLoopEvent;
import com.soarclient.skia.context.SkiaContext;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {

	@Shadow
	@Final
	private Window window;

	@Shadow
	public abstract String getWindowTitle();

	@Overwrite
	public void updateWindowTitle() {
		this.window.setTitle(Soar.getInstance().getName() + " Client v" + Soar.getInstance().getVersion() + " for "
				+ getWindowTitle());
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	public void init(CallbackInfo ci) {
		SkiaContext.createSurface(window.getWidth(), window.getHeight());
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void onClientTick(CallbackInfo ci) {
		EventBus.getInstance().post(new ClientTickEvent());
	}
	
	@Inject(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;printCrashReport()V"))
	public void onGameLoop(CallbackInfo ci) {
		EventBus.getInstance().post(new GameLoopEvent());
	}
}
