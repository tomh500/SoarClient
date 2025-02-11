package com.soarclient.mixin.mixins.minecraft.client;

import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.main.GameConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.mojang.blaze3d.platform.Window;
import com.soarclient.Soar;
import com.soarclient.event.EventBus;
import com.soarclient.event.client.ClientTickEvent;
import com.soarclient.event.client.GameLoopEvent;
import com.soarclient.management.mod.impl.player.HitDelayFixMod;
import com.soarclient.management.mod.impl.player.OldAnimationsMod;
import com.soarclient.mixin.interfaces.IMixinLivingEntity;
import com.soarclient.mixin.interfaces.IMixinMinecraftClient;
import com.soarclient.shader.impl.KawaseBlur;
import com.soarclient.skia.context.SkiaContext;

@Mixin(value = Minecraft.class, priority = 300)
public abstract class MixinMinecraftClient implements IMixinMinecraftClient {

	@Shadow
	@Final
	private Window window;

	@Shadow
	public int attackCooldown;

    @Shadow
    public MultiPlayerGameMode interactionManager;

    @Final
    @Shadow
    public ParticleEngine particleManager;

    @Shadow
    public Options options;
    
    @Shadow
    public HitResult crosshairTarget;
    
    @Shadow
    public ClientLevel world;
    
    @Shadow
	public LocalPlayer player;
    
	@Shadow
	public abstract String getWindowTitle();

	@Unique
	private File assetDir;

	@Inject(method = "<init>(Lnet/minecraft/client/RunArgs;)V", at = @At("TAIL"))
	public void onInit(GameConfig args, CallbackInfo ci) {
		assetDir = args.location.assetDirectory;
	}
	
	@Inject(method = "handleBlockBreaking", at = @At("HEAD"), cancellable = true)
	private void handleBlockBreaking(boolean breaking, CallbackInfo ci) {
		
		if (OldAnimationsMod.getInstance().isEnabled() && OldAnimationsMod.getInstance().isOldBreaking()) {
			if (this.options.keyAttack.isDown() && this.options.keyUse.isDown()) {
				
				if (breaking && this.crosshairTarget != null && this.crosshairTarget.getType() == Type.BLOCK) {
					
					BlockHitResult blockHitResult = (BlockHitResult)this.crosshairTarget;
					BlockPos blockPos = blockHitResult.getBlockPos();
					
					if (!this.world.getBlockState(blockPos).isAir()) {
						Direction direction = blockHitResult.getDirection();
						this.particleManager.crack(blockPos, direction);
						((IMixinLivingEntity)player).fakeSwingHand(InteractionHand.MAIN_HAND);
					}
				}
			}
		}
	}

	@Inject(method = "doAttack", at = @At("HEAD"))
	private void onHitDelayFix(CallbackInfoReturnable<Boolean> cir) {
		if (HitDelayFixMod.getInstance().isEnabled()) {
			attackCooldown = 0;
		}
	}

	@Overwrite
	public void updateWindowTitle() {
		this.window.setTitle(Soar.getInstance().getName() + " Client v" + Soar.getInstance().getVersion() + " for "
				+ getWindowTitle());
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	public void init(CallbackInfo ci) {
		SkiaContext.createSurface(window.getScreenWidth(), window.getScreenHeight());
		Soar.getInstance().start();
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void onClientTick(CallbackInfo ci) {
		EventBus.getInstance().post(new ClientTickEvent());
	}

	@Inject(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;printCrashReport()V"))
	public void onGameLoop(CallbackInfo ci) {
		EventBus.getInstance().post(new GameLoopEvent());
	}

	@Inject(method = "onResolutionChanged", at = @At("TAIL"))
	public void onResolutionChanged(CallbackInfo info) {
		KawaseBlur.GUI_BLUR.resize();
		KawaseBlur.INGAME_BLUR.resize();
	}

	@Override
	public File getAssetDir() {
		return this.assetDir;
	}
}
