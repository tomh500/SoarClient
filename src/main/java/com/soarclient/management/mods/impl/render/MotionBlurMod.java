package com.soarclient.management.mods.impl.render;

import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.ShaderEvent;
import com.soarclient.management.mods.Mod;
import com.soarclient.management.mods.ModCategory;
import com.soarclient.management.mods.settings.impl.NumberSetting;
import com.soarclient.nanovg.font.Icon;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.util.ResourceLocation;

public class MotionBlurMod extends Mod {

	private static MotionBlurMod instance;
	
	private ResourceLocation motion_blur = new ResourceLocation("minecraft:shaders/post/motion_blur.json");
	private ShaderGroup group;
	private float groupBlur;
	private boolean loaded;
	private int prevWidth, prevHeight;
	
	private NumberSetting amountSetting = new NumberSetting("setting.amount", "setting.amount.description",
			Icon.FILTER_5, this, 0.5F, 0.1F, 1.0F, 0.01F);
	
	public MotionBlurMod() {
		super("mod.motionblur.name", "mod.motionblur.description", Icon.MOTION_BLUR, ModCategory.RENDER);

		instance = this;
		loaded = false;
	}

	@EventHandler
	public void onShader(ShaderEvent event) {

		ScaledResolution sr = new ScaledResolution(mc);

		if (group == null || prevWidth != sr.getScaledWidth() || prevHeight != sr.getScaledHeight()) {

			prevWidth = sr.getScaledWidth();
			prevHeight = sr.getScaledHeight();

			groupBlur = amountSetting.getValue();

			try {
				group = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(),
						motion_blur);
				group.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (groupBlur != amountSetting.getValue() || !loaded) {
			loaded = true;
			group.getListShaders().forEach((shader) -> {
				ShaderUniform factor = shader.getShaderManager().getShaderUniform("BlurFactor");
				if (factor != null) {
					factor.set(amountSetting.getValue() - 0.01F);
				}
			});
			groupBlur = amountSetting.getValue();
		}

		event.getGroups().add(group);
	}

	@Override
	public void onEnable() {
		group = null;
		super.onEnable();
	}

	public static MotionBlurMod getInstance() {
		return instance;
	}

	public NumberSetting getAmountSetting() {
		return amountSetting;
	}
}
