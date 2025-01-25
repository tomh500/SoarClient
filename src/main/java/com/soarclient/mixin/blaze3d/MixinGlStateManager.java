package com.soarclient.mixin.blaze3d;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.blaze3d.platform.GlStateManager;
import com.soarclient.skia.context.States;

@Mixin(GlStateManager.class)
public class MixinGlStateManager {

    @Inject(method = "_genTexture", at = @At("RETURN"), remap = false)
    private static void genTexture(CallbackInfoReturnable<Integer> cir) {
        States.getInstance().getTextures().push(cir.getReturnValue());
    }
}
