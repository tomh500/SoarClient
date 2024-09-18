package com.soarclient.libraries.sodium.client.gl.compat;

import com.soarclient.libraries.sodium.SodiumClientMod;
import com.soarclient.libraries.sodium.client.render.chunk.shader.ChunkFogMode;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.FogState;
import net.minecraft.util.MathHelper;

public class FogHelper {
	private static final float FAR_PLANE_THRESHOLD_EXP = (float)Math.log(526.3158F);
	private static final float FAR_PLANE_THRESHOLD_EXP2 = MathHelper.sqrt_float(FAR_PLANE_THRESHOLD_EXP);

	private static FogState fogState() {
		return GlStateManager.getFogState();
	}

	public static float getFogEnd() {
		return fogState().end;
	}

	public static float getFogStart() {
		return fogState().start;
	}

	public static float getFogDensity() {
		return fogState().density;
	}

	public static ChunkFogMode getFogMode() {
		if (!SodiumClientMod.options().quality.enableFog) {
			return ChunkFogMode.NONE;
		} else {
			int mode = fogState().mode;
			if (mode != 0 && fogState().fog.isCurrentState()) {
				switch (mode) {
					case 2048:
					case 2049:
						return ChunkFogMode.EXP2;
					case 9729:
						return ChunkFogMode.LINEAR;
					default:
						throw new UnsupportedOperationException("Unknown fog mode: " + mode);
				}
			} else {
				return ChunkFogMode.NONE;
			}
		}
	}

	public static float getFogCutoff() {
		int mode = fogState().mode;
		switch (mode) {
			case 2048:
				return FAR_PLANE_THRESHOLD_EXP / getFogDensity();
			case 2049:
				return FAR_PLANE_THRESHOLD_EXP2 / getFogDensity();
			case 9729:
				return getFogEnd();
			default:
				return 0.0F;
		}
	}

	public static float[] getFogColor() {
		EntityRenderer entityRenderer = Minecraft.getMinecraft().entityRenderer;
		return new float[]{entityRenderer.getFogColorRed(), entityRenderer.getFogColorGreen(), entityRenderer.getFogColorBlue(), 1.0F};
	}
}
