package com.soarclient.shaders.blur;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NanoVGGL2;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.soarclient.event.EventBus;
import com.soarclient.event.impl.RenderBlurEvent;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.shaders.Shader;
import com.soarclient.shaders.ShaderUtils;
import com.soarclient.utils.TimerUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;

public class GaussianBlur {

	private Minecraft mc = Minecraft.getMinecraft();

	private Shader blurShader = new Shader(new ResourceLocation("soar/shader/gaussian.frag"));
	private Framebuffer framebuffer = new Framebuffer(1, 1, false);
	private Framebuffer secondFramebuffer = new Framebuffer(1, 1, false);
	private float[] cachedBuffer = new float[200];
	private float prevRadius;
	private int texture = -1;
	private TimerUtils updateTimer = new TimerUtils();
	private boolean nvgMode;

	public GaussianBlur(boolean nvgMode) {
		this.nvgMode = nvgMode;
		blurShader.setupUniform("textureIn");
		blurShader.setupUniform("maskTexture");
		blurShader.setupUniform("texelSize");
		blurShader.setupUniform("direction");
		blurShader.setupUniform("radius");
		blurShader.setupUniform("weights");
	}

	private void setupUniforms(float dir1, float dir2, float radius, float quality) {
		quality = (float) Math.max(quality, 1.0);
		GL20.glUniform1i(blurShader.getUniform("textureIn"), 0);
		GL20.glUniform1i(blurShader.getUniform("maskTexture"), 16);
		GL20.glUniform2f(blurShader.getUniform("texelSize"), 1.0F / (float) (mc.displayWidth / quality),
				1.0F / (float) (mc.displayHeight / quality));
		GL20.glUniform2f(blurShader.getUniform("direction"), dir1, dir2);
		GL20.glUniform1f(blurShader.getUniform("radius"), radius);

		FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(256);

		for (int i = 0; i <= radius; i++) {
			if (radius != prevRadius) {

				float val = ShaderUtils.calculateGaussianValue(i, radius / 2);

				cachedBuffer[i] = val;
				weightBuffer.put(val);
			} else {
				weightBuffer.put(cachedBuffer[i]);
			}
		}

		prevRadius = radius;

		weightBuffer.rewind();
		GL20.glUniform1fv(blurShader.getUniform("weights"), weightBuffer);
	}

	public void draw(float radius) {

		if (nvgMode) {
			GlStateManager.enableBlend();
			GlStateManager.color(1, 1, 1, 1);
			OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
		}

		framebuffer = ShaderUtils.createFramebuffer(framebuffer);
		secondFramebuffer = ShaderUtils.createFramebuffer(secondFramebuffer);

		if (updateTimer.delay(16)) {

			GlStateManager.enableBlend();
			GlStateManager.color(1, 1, 1, 1);
			OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

			framebuffer.framebufferClear();
			framebuffer.bindFramebuffer(true);
			blurShader.init();
			setupUniforms(1, 0, Math.min(radius, 25F), radius * 0.04F);

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.getFramebuffer().framebufferTexture);

			blurShader.bind();
			framebuffer.unbindFramebuffer();
			blurShader.finish();

			secondFramebuffer.framebufferClear();
			secondFramebuffer.bindFramebuffer(true);
			blurShader.init();
			setupUniforms(0, 1, Math.min(radius, 25F), radius * 0.04F);

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebuffer.framebufferTexture);

			blurShader.bind();
			secondFramebuffer.unbindFramebuffer();
			blurShader.finish();

			updateTimer.reset();

			GlStateManager.color(1, 1, 1, 1);
			GlStateManager.bindTexture(0);
		}

		mc.getFramebuffer().bindFramebuffer(true);

		if (nvgMode) {

			NanoVGHelper nvg = NanoVGHelper.getInstance();

			nvg.setupAndDraw(() -> {

				NVGPaint paint = NVGPaint.calloc();

				if (texture == -1) {
					texture = NanoVGGL2.nvglCreateImageFromHandle(nvg.getContext(),
							secondFramebuffer.framebufferTexture, mc.displayWidth, -mc.displayHeight, 0);
				}

				EventBus.getInstance().post(new RenderBlurEvent(texture, paint));
				paint.free();
			}, false);
		} else {
			secondFramebuffer.bindFramebufferTexture();
			blurShader.bind();
			GlStateManager.color(1, 1, 1, 1);
			GlStateManager.bindTexture(0);
		}
	}
}
