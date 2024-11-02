package com.soarclient.shaders.screen;

import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL2;
import org.lwjgl.opengl.GL11;

import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.shaders.ShaderUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;

public class ScreenWrapper {

	private Minecraft mc = Minecraft.getMinecraft();
	private Framebuffer framebuffer = new Framebuffer(1, 1, true);
	private int texture = -1;

	public void wrap(Runnable task, float x, float y, float width, float height, float radius, float scale,
			float alpha) {

		NanoVGHelper nvg = NanoVGHelper.getInstance();

		GlStateManager.pushMatrix();

		GlStateManager.color(1, 1, 1, 1);
		OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

		framebuffer = ShaderUtils.createFramebuffer(framebuffer);

		framebuffer.framebufferClear();
		framebuffer.bindFramebuffer(true);
		GL11.glClearColor(0, 0, 0, 0);
		task.run();
		framebuffer.unbindFramebuffer();

		mc.getFramebuffer().bindFramebuffer(true);

		nvg.setupAndDraw(() -> {

			nvg.save();
			nvg.setAlpha(Math.min(alpha, 1.0F));
			nvg.scale(x, y, width, height, scale);

			NVGPaint paint = NVGPaint.calloc();

			if (texture == -1) {
				texture = NanoVGGL2.nvglCreateImageFromHandle(nvg.getContext(), framebuffer.framebufferTexture,
						mc.displayWidth, -mc.displayHeight, 0);
			}

			NanoVG.nvgBeginPath(nvg.getContext());
			NanoVG.nvgRoundedRect(nvg.getContext(), x, y, width, height, radius);
			NanoVG.nvgFillPaint(nvg.getContext(), NanoVG.nvgImagePattern(nvg.getContext(), 0, mc.displayHeight,
					mc.displayWidth, -mc.displayHeight, 0, texture, 1, paint));
			NanoVG.nvgFill(nvg.getContext());
			nvg.restore();
			paint.free();
		}, false);

		GlStateManager.popMatrix();
	}
}