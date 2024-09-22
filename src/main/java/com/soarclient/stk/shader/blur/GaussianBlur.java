package com.soarclient.stk.shader.blur;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.soarclient.stk.shader.Shader;
import com.soarclient.stk.shader.ShaderUtils;
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
	private float[] cachedBuffer = new float[60];
	private TimerUtils updateTimer = new TimerUtils();
	private float prevRadius;

	public GaussianBlur() {
		blurShader.setupUniform("textureIn");
		blurShader.setupUniform("texelSize");
		blurShader.setupUniform("direction");
		blurShader.setupUniform("radius");
		blurShader.setupUniform("weights");
	}

	private void setupUniforms(float dir1, float dir2, float radius) {

		GL20.glUniform1i(blurShader.getUniform("textureIn"), 0);
		GL20.glUniform2f(blurShader.getUniform("texelSize"), 1.0F / (float) mc.displayWidth,
				1.0F / (float) mc.displayHeight);
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

		GlStateManager.enableBlend();
		GlStateManager.color(1, 1, 1, 1);
		OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

		framebuffer = ShaderUtils.createFramebuffer(framebuffer);
		secondFramebuffer = ShaderUtils.createFramebuffer(secondFramebuffer);

		if (updateTimer.delay(16)) {

			framebuffer.framebufferClear();
			framebuffer.bindFramebuffer(true);
			blurShader.init();
			setupUniforms(1, 0, radius);

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.getFramebuffer().framebufferTexture);

			blurShader.bind();
			framebuffer.unbindFramebuffer();
			blurShader.finish();

			secondFramebuffer.framebufferClear();
			secondFramebuffer.bindFramebuffer(true);
			blurShader.init();
			setupUniforms(0, 1, radius);

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebuffer.framebufferTexture);
			blurShader.bind();
			secondFramebuffer.unbindFramebuffer();
			blurShader.finish();

			updateTimer.reset();
		}

		mc.getFramebuffer().bindFramebuffer(true);
		secondFramebuffer.bindFramebufferTexture();
		blurShader.bind();

		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.bindTexture(0);
	}
}
