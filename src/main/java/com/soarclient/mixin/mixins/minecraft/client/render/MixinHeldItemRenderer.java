package com.soarclient.mixin.mixins.minecraft.client.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import com.soarclient.management.mod.impl.player.OldAnimationsMod;
import com.soarclient.management.mod.impl.render.CustomHandMod;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;

@Mixin(ItemInHandRenderer.class)
public abstract class MixinHeldItemRenderer {

	@Shadow
	protected abstract void applySwingOffset(PoseStack matrices, HumanoidArm arm, float swingProgress);

	@Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = ("Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"), ordinal = 1))
	private void renderFirstPersonItem(AbstractClientPlayer player, float tickDelta, float pitch, InteractionHand hand,
			float swingProgress, ItemStack item, float equipProgress, PoseStack matrices,
			MultiBufferSource vertexConsumers, int light, CallbackInfo ci) {

		OldAnimationsMod mod = OldAnimationsMod.getInstance();

		if (item.getItem() instanceof BowItem && mod.isEnabled() && mod.isOldBow()) {
			matrices.translate(0f, 0.05f, 0.04f);
			matrices.scale(0.93f, 1f, 1f);
		} else if (item.getItem() instanceof FishingRodItem && mod.isEnabled() && mod.isOldRod()) {
			matrices.translate(0.08f, -0.027f, -0.33f);
			matrices.scale(0.93f, 1f, 1f);
		}
	}
	
	@Inject(method = "renderFirstPersonItem", at = @At("HEAD"))
	private void applyCustomHand(AbstractClientPlayer player, float tickDelta, float pitch, InteractionHand hand,
			float swingProgress, ItemStack item, float equipProgress, PoseStack matrices,
			MultiBufferSource vertexConsumers, int light, CallbackInfo ci) {
		
		CustomHandMod mod = CustomHandMod.getInstance();
		
		if(mod.isEnabled()) {
			matrices.translate(mod.getX(), mod.getY(), mod.getZ());
			matrices.scale(mod.getScale(), mod.getScale(), mod.getScale());
		}
	}
	
	@Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V", ordinal = 2, shift = At.Shift.AFTER))
	private void applyFoodSwingOffset(AbstractClientPlayer player, float tickDelta, float pitch, InteractionHand hand,
			float swingProgress, ItemStack item, float equipProgress, PoseStack matrices,
			MultiBufferSource vertexConsumers, int light, CallbackInfo ci) {
		applyOldSwingOffset(player, hand, swingProgress, matrices);
	}

	@Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V", ordinal = 3, shift = At.Shift.AFTER))
	private void applyBlockingSwingOffset(AbstractClientPlayer player, float tickDelta, float pitch, InteractionHand hand,
			float swingProgress, ItemStack item, float equipProgress, PoseStack matrices,
			MultiBufferSource vertexConsumers, int light, CallbackInfo ci) {
		applyOldSwingOffset(player, hand, swingProgress, matrices);
	}

	@Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V", ordinal = 4, shift = At.Shift.AFTER))
	private void applyBowSwingOffset(AbstractClientPlayer player, float tickDelta, float pitch, InteractionHand hand,
			float swingProgress, ItemStack item, float equipProgress, PoseStack matrices,
			MultiBufferSource vertexConsumers, int light, CallbackInfo ci) {
		applyOldSwingOffset(player, hand, swingProgress, matrices);
	}

	@Unique
	private void applyOldSwingOffset(AbstractClientPlayer player, InteractionHand hand, float swingProgress,
			PoseStack matrices) {
		if (OldAnimationsMod.getInstance().isEnabled() && OldAnimationsMod.getInstance().isOldBreaking()) {
			final HumanoidArm arm = hand == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
			applySwingOffset(matrices, arm, swingProgress);
		}
	}
}
